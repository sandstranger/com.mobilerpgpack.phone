package com.zxw.bingtranslateapi.entity;

import lombok.Data;

import java.util.Date;

/**
 * 翻译配置
 */
@Data
public class TranslateConfig {
    private String IG;
    private String IID;
    private String cookie;
    private Long key;
    private String token;
    private Long tokenTs;
    private Long tokenExpiryInterval;
    private Integer count;

    public boolean isTokenExpired() {
        return new Date().getTime() - tokenTs > tokenExpiryInterval;
    }
}
