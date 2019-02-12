package com.paic.vodo.node.office.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @BelongsProject: netty
 * @BelongsPackage: com.paic.vodo.node.netty.bean
 * @Author: zff
 * @CreateTime: 2018-12-31 13:41
 * @Description: ${Description}
 */
@Getter
@Setter
public class User implements Serializable {
    private static  final Long serialVersionUID=1L;
    private Long id;
    private String username;
    private String password;
    private String usercode;
    private String email;
    private String telephone;
    private String oldPassword;
}
