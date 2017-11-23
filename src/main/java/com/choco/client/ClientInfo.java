package com.choco.client;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Date;

import com.google.gson.Gson;
import sun.misc.Cleaner;

public class ClientInfo {
    @Expose
    private String name;
    @Expose
    private int port;
    @Expose(deserialize = false) private String address = "255.255.255.0";
    @Expose(serialize = false) private String token;
    @Expose(serialize = false,deserialize = false)
    private final long timeoutMillSeconds = 10 * 1000;
    @Expose(serialize = false,deserialize = false)
    private Date updateTime;

    public boolean isTimeOut(){
        return new Date().after(new Date(updateTime.getTime() + timeoutMillSeconds));
    }

    public static ClientInfo fromRequestBody(InputStream body){
        BufferedReader reader = new BufferedReader(new InputStreamReader(body));
        String request = "";
        String inputLine;

        try {
            while((inputLine = reader.readLine()) != null){
                request += inputLine;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
                create().fromJson(request, ClientInfo.class);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ClientInfo(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        updateTime = new Date();
    }
    public ClientInfo(String name, int port){
        this.name = name;
        this.port = port;
        updateTime = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void genToken(){
        String t = name +address+port;
        token = new String(Base64.getEncoder().encode(t.getBytes()));
    }

    public String getToken(){
        return token;
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
            this.name = json.getString("name");
            this.address = json.getString("ip");
            this.port = json.getInt("port");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
