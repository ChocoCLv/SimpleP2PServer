package com.choco.server;

import com.choco.client.ClientInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseToClient {


    @Expose
    private String token;
    @Expose
    private int code;
    @Expose
    private List<ClientInfo> userInfoList = new ArrayList<>();

    public ResponseToClient(int code, String token, Collection<ClientInfo> userInfoList){
        this.code = code;
        this.token = token;
        this.userInfoList.addAll(userInfoList);
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ClientInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<ClientInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String toJson(){
        //Gson gson = new Gson();//
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
