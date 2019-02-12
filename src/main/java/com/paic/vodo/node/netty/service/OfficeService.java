package com.paic.vodo.node.netty.service;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty
 * @Author: zff
 * @CreateTime: 2018-12-31 13:12
 * @Description: ${Description}
 */
public interface OfficeService {
    String getOfficeByid(String id,String cookie);

    void saveOffice(String id,String userId,String cookie) throws Exception;
}
