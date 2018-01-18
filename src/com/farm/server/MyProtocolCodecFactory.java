package com.farm.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author: taoroot
 * @date: 2018/1/17
 * @description: 协议解析器, 负责将序列号对象,反序列对象
 */
public class MyProtocolCodecFactory implements ProtocolCodecFactory {

    private final MyDecoder decoder;
    private final MyEncoder encoder;

    public MyProtocolCodecFactory() {
        this.decoder = new MyDecoder();
        this.encoder = new MyEncoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
