package com.farm.util;

import java.util.ResourceBundle;

/**
 * @author: taoroot
 * @date: 2017/10/31
 * @description: 配置文件读取
 */
public class ConfigUtil {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    /**
     * 通过参数名获取值
     *
     * @param key
     * @return
     */
    public static final String get(String key) {
        return bundle.getString(key);
    }

}