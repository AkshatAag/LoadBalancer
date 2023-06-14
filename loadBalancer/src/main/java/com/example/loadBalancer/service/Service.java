package com.example.loadBalancer.service;

import com.example.loadBalancer.entity.CallFromControlLayer;

import java.util.List;

import com.example.loadBalancer.entity.FreeswitchMediaLayerLoad;
import com.example.loadBalancer.repository.LoadRedis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private LoadRedis loadRedis;
    static Logger logger = LogManager.getLogger(Service.class);

    public String getMediaLayerNumber(CallFromControlLayer callFromControlLayer) {
        String conversationId = callFromControlLayer.getConversationId();

        int mediaLayerNumber = loadRedis.findMediaLayer(conversationId);

        if (mediaLayerNumber != -1) 
            return Integer.toString(mediaLayerNumber);
        else {
            String res = assignNewLayer();
            loadRedis.setMediaLayer(conversationId,res);
            return res;
        }
    }

    private String assignNewLayer() {
        List<FreeswitchMediaLayerLoad> freeswitchMediaLayerLoadList = loadRedis.findAllLoads();
        int minIdx = -1;
        int curLoad = 9999;
        for (int i = 0; i < freeswitchMediaLayerLoadList.size(); i++) {
            FreeswitchMediaLayerLoad temp = freeswitchMediaLayerLoadList.get(i);
            int currentLoad=temp.getCurrentLoad();
            if (currentLoad< curLoad) {
                curLoad = freeswitchMediaLayerLoadList.get(i).getCurrentLoad();
                minIdx = i;
            }
        }
        if (minIdx == -1) return "UNABLE TO ASSIGN";
        else {
            int newLoad = freeswitchMediaLayerLoadList.get(minIdx).getCurrentLoad() + 1;
            int layerNumber = freeswitchMediaLayerLoadList.get(minIdx).getLayerNumber();
            loadRedis.setLoad(new FreeswitchMediaLayerLoad(layerNumber, newLoad));
            return Integer.toString(layerNumber);
        }
    }

    public String addNewMediaLayer(FreeswitchMediaLayerLoad freeswitchMediaLayerLoad) {
        return loadRedis.setLoad(freeswitchMediaLayerLoad).toString();
    }
}
