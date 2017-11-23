package com.choco.server;

import com.choco.client.ClientController;
import com.choco.client.ClientInfo;
import com.choco.Log;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class P2PHttpHandler implements HttpHandler {

    ClientController clientController;
    private final static int OPERATION_SUCCESS = 200;
    private final static int OPERATION_FAILED = 201;

    public void handle(HttpExchange exchange) throws IOException{
        String requestMethod = exchange.getRequestMethod();
        String address = exchange.getRemoteAddress().getHostString();
        if(requestMethod.equalsIgnoreCase("POST")){
            Log.i("connetct with: " +address );
            ResponseToClient response;
            OutputStream responseBody = exchange.getResponseBody();

            ClientInfo client = ClientInfo.fromRequestBody(exchange.getRequestBody());
            client.setAddress(address);
            Log.i("取出的注册者信息: " + client.getName() + client.getPort());

            if(clientController.addClient(client)){
                response = new ResponseToClient(OPERATION_SUCCESS,client.getToken(), clientController.getClientInfoList());
            }else {
                response = new ResponseToClient(OPERATION_FAILED,"", null);
            }

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            Log.i(response.toJson());
            byte[] bytes = response.toJson().getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            responseBody.write(bytes);
            exchange.close();
        }
    }

    public P2PHttpHandler() {
        clientController = new ClientController();

        ClientInfo client1 = new ClientInfo("陈律", "192.168.1.102", 102);
        ClientInfo client2 = new ClientInfo("周裔欢", "192.168.1.69", 102);

        clientController.addClient(client1);
        clientController.addClient(client2);
    }
}
