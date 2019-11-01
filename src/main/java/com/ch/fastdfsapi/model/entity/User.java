package com.ch.fastdfsapi.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ch
 */
@Entity
@Data
public class User {

    @Id
    @Column(name = "ID")
    private String id;
    /**
     * 用户名称
     */
    @Column(name = "USERNAME")
    private String username;
    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;
    /**
     * 邮箱
     */
    @Column(name = "MAILBOX")
    private String mailbox;

    /**
     * token
     */
    @Column(name = "TOKEN")
    private String token;

}
