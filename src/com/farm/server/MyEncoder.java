package com.farm.server;

import com.farm.protocol.impl.FarmMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 协议编码器, 将对象转为二进制数据包
 */
public class MyEncoder extends ProtocolEncoderAdapter {
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(500).setAutoExpand(true);
        buf.put(((FarmMessage) message).WriteToBytes());
        buf.flip();
        out.write(buf);
        out.flush();
        buf.free();
    }
}
