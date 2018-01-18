package com.farm.server;

import com.farm.protocol.impl.FarmMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 协议解码器, 将二进制数据包转为对象
 */
public class MyDecoder extends CumulativeProtocolDecoder {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 是否有可读取内容
        if (in.remaining() < 1) {
            return false;
        }
        byte[] data = new byte[in.remaining()];
        in.get(data);

        FarmMessage message = new FarmMessage();
        message.ReadFromBytes(data);
        out.write(message);
        return false;
    }
}
