package com.choco.server;

import com.choco.ClientInfo;
import com.choco.Log;
import com.google.gson.GsonBuilder;
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
    private Map<String, ClientInfo> clientMap;
    private Runnable runnable;
    public void handle(HttpExchange exchange) throws IOException{
        String requestMethod = exchange.getRequestMethod();
        if(requestMethod.equalsIgnoreCase("POST")){
            //TODO 是否要区分注册和keep alive 两种类型都需要向客户端返回已有的客户端列表
            // 服务器将所有客户端信息返回给请求的客户端  包括自身的信息 由客户端自己判断是否要显示
            // 建议增加密码字段 根据密码判断是否注册成功·

            int code = 201;
            ResponseToClient response = new ResponseToClient("200"," ", clientMap.values());
            if(addClient(exchange.getRequestBody())){
                code = 200;
            }
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");

            OutputStream responseBody = exchange.getResponseBody();
            response.setCode(code+"");
            byte[] bytes = response.toJson().getBytes();

            exchange.sendResponseHeaders(code, bytes.length);
            responseBody.write(bytes);

            exchange.close();
        }
    }

    private boolean addClient(InputStream body){
        BufferedReader reader = new BufferedReader(new InputStreamReader(body));
        String request = "";
        String inputLine;

        try {
            while((inputLine = reader.readLine()) != null){
                request += inputLine;
            }
            ClientInfo client = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
                    create().fromJson(request, ClientInfo.class);
            Log.i("client "+client.getUsername()+" request to register");
            if(clientMap.containsKey(client.getUsername())){
                Log.i("register failed ");
                return false;
            }
            Log.i("register successfully ");
            clientMap.put(client.getUsername(), client);
        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public P2PHttpHandler(){
        clientMap = new HashMap<>();
        ClientInfo client1 = new ClientInfo("陈律","192.168.1.102",102);
        clientMap.put(client1.getUsername(),client1);
        ClientInfo client2 = new ClientInfo("周裔欢","192.168.1.69",102);
        clientMap.put(client2.getUsername(),client2);

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(new Date().toString()+" : timer task start");
                Iterator<Map.Entry<String, ClientInfo>> iterator = clientMap.entrySet().iterator();
                while(iterator.hasNext()){

                    Map.Entry<String, ClientInfo> entry = iterator.next();
                    if(entry.getValue().isTimeOut()){
                        Log.i(entry.getKey()+" is time out");
                        iterator.remove();
                    }else{
                        Log.i(entry.getKey()+" is not time out");
                    }
                }
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 5,5, TimeUnit.SECONDS);
    }
}
