# 农业监控代理端

## 目录结构
```bash
  .gitignore
│  pom.xml
│  README.md
│
└─src
    │  config.properties
    │  log4j.properties
    │
    ├─com
    │  └─farm
    │      │
    │      ├─protocol
    │      │  │  IMessage.java
    │      │  │  MessageBodyFactory.java
    │      │  │  MyAdditionalData.java
    │      │  │  MyBuffer.java
    │      │  │
    │      │  └─impl
    │      │          FarmMessage.java
    │      │          HeadMessage.java
    │      │          PayLoad_00.java
    │      │          PayLoad_01.java
    │      │          PayLoad_05.java
    │      │          PayLoad_06.java
    │      │          PayLoad_F0.java
    │      │
    │      ├─redis
    │      │      JedisConnectionPool.java
    │      │      Publisher.java
    │      │      Subscriber.java
    │      │
    │      ├─server
    │      │      CmdOptionHandler.java
    │      │      MinaServer.java
    │      │      MyDecoder.java
    │      │      MyEncoder.java
    │      │      MyIoHandler.java
    │      │      MyKeepAliveFilter.java
    │      │      MyKeepAliveMessageFactory.java
    │      │      MyKeepAliveRequestTimeoutHandler.java
    │      │      MyProtocolCodecFactory.java
    │      │
    │      └─util
    │              ByteUtil.java
    │              ClassUtils.java
    │              ConfigUtil.java
    │              Crc16Util.java
    │              IoSessionUtil.java
    │              MD5Util.java
    │
    └─META-INF
            MANIFEST.MF
```        
