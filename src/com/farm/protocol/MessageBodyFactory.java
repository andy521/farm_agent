package com.farm.protocol;

import com.farm.util.ByteUtil;
import com.farm.util.ClassUtils;

/**
 * @author: taoroot
 * @date: 2017/11/3
 * @description: 根据数据包类型, 反射出消息体对象
 */
public class MessageBodyFactory {

    public static IMessage Create(byte messageType, byte[] messageBodyBytes) {

        String nameSpace = MessageBodyFactory.class.getPackage().getName();
        String className = nameSpace + ".impl.PayLoad_" + ByteUtil.bytesToHexString(messageType);

        Object messageBody = ClassUtils.getBean(className);
        if (messageBody != null) {
            IMessage msg = (IMessage) messageBody;

            msg.ReadFromBytes(messageBodyBytes);
            return msg;
        }
        return null;
    }

}
