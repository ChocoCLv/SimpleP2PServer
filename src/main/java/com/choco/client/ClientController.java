package com.choco.client;

import com.choco.Log;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by choco on 2017/11/23.
 */
public class ClientController {
    private Map<String, ClientInfo> clientInfoMap;
    private Runnable runnable;

    public ClientController(){
        clientInfoMap = new HashMap<>();

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(new Date().toString()+" : timer task start");
                Iterator<Map.Entry<String, ClientInfo>> iterator = clientInfoMap.entrySet().iterator();
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

    //
    public boolean addClient(ClientInfo client){
        if(clientInfoMap.containsKey(client.getName())){
            if(clientInfoMap.get(client.getName()).getToken().equals(client.getToken())){
                clientInfoMap.get(client.getName()).setUpdateTime(new Date());
                return true;
            }else{
                return false;
            }
        }
        client.setUpdateTime(new Date());
        clientInfoMap.put(client.getName(), client);
        client.genToken();
        return true;
    }

    public List<ClientInfo> getClientInfoList(){
        List<ClientInfo> list = new ArrayList<>();
        list.addAll(clientInfoMap.values());
        return list;
    }
}
