package com.farm.protocol;

/**
 * @author: taoroot
 * @date: 2017/11/4
 * @description:  序列号和反序列接口
 */
public interface IMessage {

    /**
     * 对象属性 转 数组
     *
     * 讲本对象属性转成字节数据
     *
     */
    byte[] WriteToBytes();

    /**
     * 数组 转 对象属性
     *
     * 讲原始数据解析为本对象属性
     *
     * @param messageBytes 数据包
     */
    void ReadFromBytes(byte[] messageBytes);

}
