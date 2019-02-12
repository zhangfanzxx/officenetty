package com.paic.vodo.node.netty.service.impl;

import com.paic.vodo.node.netty.bean.ResponseData;
import com.paic.vodo.node.netty.service.OfficeService;
import com.paic.vodo.node.netty.utlis.JsonUtil;
import com.paic.vodo.node.netty.utlis.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty.service.impl
 * @Author: zff
 * @CreateTime: 2018-12-31 13:13
 * @Description: ${Description}
 */
@Component
public class OfficeServiceImpl implements OfficeService {

    @Value("${url.getOfficeById}")
    private String getById;
    @Value("${url.saveOffice}")
    private String saveOffice;

    @Override
    public String getOfficeByid(String id, String cookie) {
        Map map = new HashMap(4);
        map.put("id", id);
        map.put("cookie", cookie);
        String s = OkHttpUtil.postJsonParams(getById, JsonUtil.objectToJson(map));
        ResponseData data = JsonUtil.jsonToPojo(s, ResponseData.class);
        if (data.isResult()) {
            System.out.println(s);
            String s1 = OkHttpUtil.get(("" + data.getData()).replace("\\", "/"), null);
            System.out.println("getById" + s1);
            return s1;
        }
        return null;
    }

    @Override
    public void saveOffice(String id, String userId, String cookie) throws Exception {
        Map map = new HashMap(5);
        map.put("id", id);
        map.put("userId", userId);
        map.put("cookie", cookie);
        try {
             OkHttpUtil.postJsonParams(saveOffice, JsonUtil.objectToJson(map));
        } catch (Exception e) {
            throw new Exception("error");
        }
    }
}
