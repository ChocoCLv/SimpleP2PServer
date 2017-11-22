package com.choco.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class P2PServer {


    public void start() throws IOException {
        InetSocketAddress addr = new InetSocketAddress(1234);
        HttpServer server = HttpServer.create(addr,0);

        server.createContext("/", new P2PHttpHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 1234");
    }

    public static void main(String[] args) throws IOException {
        P2PServer server = new P2PServer();
        server.start();
    }
}
