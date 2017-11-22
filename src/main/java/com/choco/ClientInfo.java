package com.choco;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import com.google.gson.Gson;

public class ClientInfo {
    private String username;
    private String address;
    private int port;

    public ClientInfo() {
        username = null;
        address = null;
        port = -1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void parseMsgStream(InputStream msgStream){
        byte[] buffer = new byte[1024];
        try {
            msgStream.read(buffer);
            String msg = new String(buffer);
            JSONObject json = JSONObject.fromObject(msg);
            this.username = json.getString("name");
            this.address = json.getString("ip");
            this.port = json.getInt("port");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
