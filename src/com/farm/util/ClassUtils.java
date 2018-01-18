package com.farm.util;


import org.apache.log4j.Logger;

/**
 * @author: taoroot
 * @date: 2017/11/3
 * @description: 通过名称找到需要的类
 */
public class ClassUtils {
    public static Logger logger = Logger.getLogger(ClassUtils.class);

    public static Object getBean(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception ex) {
            logger.info("找不到指定的类");
            throw new IllegalStateException("illegal payload type");
        }
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception ex) {
                logger.info("找不到指定的类");
                throw new IllegalStateException("illegal payload type");
            }
        }
        return null;
    }
}