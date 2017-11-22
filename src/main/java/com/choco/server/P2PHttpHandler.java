package com.choco.server;

import com.choco.ClientInfo;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class P2PHttpHandler implements HttpHandler {
    private Map<String, ClientInfo> clientMap;
    public void handle(HttpExchange exchange) throws IOException{
        String requestMethod = exchange.getRequestMethod();
        if(requestMethod.equalsIgnoreCase("POST")){

            ResponseToClient response = new ResponseToClient("200"," ", clientMap.values());
            parseRequestBody(exchange.getRequestBody());
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");

            OutputStream responseBody = exchange.getResponseBody();
            byte[] bytes = response.toJson().getBytes();

            exchange.sendResponseHeaders(200, bytes.length);
            responseBody.write(bytes);

            exchange.close();
        }
    }

    private void parseRequestBody(InputStream body){
        BufferedReader reader = new BufferedReader(new InputStreamReader(body));
        String request = "";
        String inputLine;

        try {
            while((inputLine = reader.readLine()) != null){
                request += inputLine;
            }
            ClientInfo client = new Gson().fromJson(request, ClientInfo.class);
            clientMap.put(client.getUsername(), client);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public P2PHttpHandler(){
        clientMap = new HashMap<>();
        ClientInfo client1 = new ClientInfo();
        client1.setUsername("陈律");
        client1.setAddress("192.168.1.102");
        client1.setPort(102);
        clientMap.put(client1.getUsername(),client1);
        ClientInfo client2 = new ClientInfo();
        client2.setUsername("欢神");
        client2.setAddress("192.168.1.69");
        client2.setPort(102);
        clientMap.put(client2.getUsername(),client2);
    }
}
