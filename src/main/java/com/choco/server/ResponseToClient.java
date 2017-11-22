package com.choco.server;

import com.choco.ClientInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseToClient {
    private String code;
    private String msg;
    private List<ClientInfo> userInfoList = new ArrayList<>();

    public ResponseToClient(String code, String msg, Collection<ClientInfo> userInfoList){
        this.code = code;
        this.msg = msg;

        this.userInfoList.addAll(userInfoList);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
