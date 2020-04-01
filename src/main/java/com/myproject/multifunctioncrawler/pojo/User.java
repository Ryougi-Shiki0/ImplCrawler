package com.myproject.multifunctioncrawler.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Arthas
 */
@Getter
@Setter
public class User {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
