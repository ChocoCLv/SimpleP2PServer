package com.choco.server;

import com.choco.client.ClientInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseToClient {
    private String result;
    private String msg;
    private List<ClientInfo> userInfoList = new ArrayList<>();

    public ResponseToClient(String result, String msg, Collection<ClientInfo> userInfoList){
        this.result = result;
        this.msg = msg;
        this.userInfoList.addAll(userInfoList);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ClientInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<ClientInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
