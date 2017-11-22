package com.choco;

import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.google.gson.Gson;

public class ClientInfo {
    private String username;
    private String address;
    private int port;
    private final long timeoutMillSeconds = 10 * 1000;

    //TODO 多线程访问 需要加锁保护
    @Expose private Date updateTime;

    public boolean isTimeOut(){
        return new Date().after(new Date(updateTime.getTime() + timeoutMillSeconds));
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ClientInfo(String username, String address, int port) {
        this.username = username;
        this.address = address;
        this.port = port;
        updateTime = new Date();
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
