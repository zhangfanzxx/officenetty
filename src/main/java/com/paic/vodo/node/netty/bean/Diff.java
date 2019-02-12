package com.paic.vodo.node.netty.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: paoffice
 * @BelongsPackage: com.paic.paoffice.bean.vo
 * @Author: zff
 * @CreateTime: 2018-10-17 13:55
 * @Description: ${Description}
 */
@Getter
@Setter
public class Diff implements Serializable {
    List<Map<String,Object>> oldv;
    List<Map<String,Object>> newv;
}
