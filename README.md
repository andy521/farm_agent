# 农业监控代理端

## 目录结构
```bash
  .gitignore
│  pom.xml
│  README.md
│
└─src
    │  config.properties      // 默认配置   
    │  log4j.properties       // 日志配置
    │
    ├─com
    │  └─farm
    │      │
    │      ├─protocol   //协议目录
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
    │      ├─redis    // redis 相关              
    │      │      JedisConnectionPool.java
    │      │      Publisher.java
    │      │      Subscriber.java
    │      │
    │      ├─server   // Mina 相关
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
    │      └─util    // 工具类
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


## 终端通讯协议设计

> 数据包格式

|名称|偏移位置|长度|值范围|
|--------|--------|--------|--------|
|包头|0|1|固定字符‘*’|
|流水号|1|4|0|0x0000-0xffff|
|类型|5|1|0-127|
|数据长度|6|2|数据字节数|
|数据|8|n|协议类型决定|
|校验码|n+8|2|类型、数据长度、数据三部分的所有字节的CRC-16码|

注 1:其中字符'\'为转意符，所有除头尾外的字节，如果是'*'，'#'，'\'在通讯时换成"\*"，"\#"，"\\"

注 2:凡涉及多字节数据均为低字节在前

> 心跳包格式

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none'>
 <tr>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><a name="_Hlk507614714"><span style='font-family:"Songti SC";
  color:black'>包名称</span></a></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border:solid windowtext 1.0pt;
  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>心跳包</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>发送方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服务端或客户端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>接收方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端或服务端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>类型码</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>00H</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据长度</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0</span><span
  style='font-family:"Songti SC";color:black'>字节</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>无</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端和服务端之间无通讯每隔两分钟互发该数据包，通知对方连接正常</span></p>
  </td>
 </tr>
</table>

> 注册包格式


<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none'>
 <tr>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><a name="_Hlk507614873"><span style='font-family:"Songti SC";
  color:black'>包名称</span></a></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border:solid windowtext 1.0pt;
  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>注册包</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>发送方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服客户端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>接收方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服务端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>类型码</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>01H</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据长度</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>13</span><span
  style='font-family:"Songti SC";color:black'>字节</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
   style='border-collapse:collapse;border:none'>
   <tr>
    <td width=98 valign=top style='width:73.8pt;border:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>偏移位置</span></p>
    </td>
    <td width=102 valign=top style='width:76.35pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>大小</span></p>
    </td>
    <td width=215 valign=top style='width:161.15pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
    </td>
   </tr>
   <tr>
    <td width=98 valign=top style='width:73.8pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0</span></p>
    </td>
    <td width=102 valign=top style='width:76.35pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=215 valign=top style='width:161.15pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>设备型号</span></p>
    </td>
   </tr>
   <tr>
    <td width=98 valign=top style='width:73.8pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=102 valign=top style='width:76.35pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>12</span></p>
    </td>
    <td width=215 valign=top style='width:161.15pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>设备</span><span
    lang=EN-US style='font-family:Times;color:black'>ID</span></p>
    </td>
   </tr>
  </table>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端与服务端建立连接以后，发送本包内容，在服务端未收到服务端回应包以前，不发送其他数据包，间隔</span><span
  lang=EN-US style='font-family:Times;color:black'>10</span><span
  style='font-family:"Songti SC";color:black'>秒发送本包</span></p>
  </td>
 </tr>
</table>

> 注册包应答格式


<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none'>
 <tr>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><a name="_Hlk507615256"><span style='font-family:"Songti SC";
  color:black'>包名称</span></a></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border:solid windowtext 1.0pt;
  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>注册包</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>发送方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服务端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>接收方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>类型码</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:150%;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>F1H</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据长度</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>13</span><span
  style='font-family:"Songti SC";color:black'>字节</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
   style='border-collapse:collapse;border:none'>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>偏移位置</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>大小</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0-</span><span
    style='font-family:"Songti SC";color:black'>成功；</span><span lang=EN-US
    style='font-family:Times;color:black'> 1-</span><span style='font-family:
    "Songti SC";color:black'>失败</span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>12</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>回应</span><span
    lang=EN-US style='font-family:Times;color:black'>token</span><span
    style='font-family:"Songti SC";color:black'>码</span></p>
    </td>
   </tr>
  </table>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>当接受到客户端注册包以后，服务端代理与后端交互，将得到的注册结果发回给客户端。如果注册结果为失败，发送以后，主动断开网络连接。</span></p>
  </td>
 </tr>
</table>

> 温度数据格式

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none'>
 <tr>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>包名称</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border:solid windowtext 1.0pt;
  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>湿度包</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>发送方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>接收方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服务端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>类型码</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:150%;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>05H</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据长度</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>2</span><span
  style='font-family:"Songti SC";color:black'>字节</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
   style='border-collapse:collapse;border:none'>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>偏移位置</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>大小</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>整数部分</span><span
    style='font-family:Times;color:black'> </span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>小数部分</span></p>
    </td>
   </tr>
  </table>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>温度数据包</span></p>
  </td>
 </tr>
</table>


> 温度数据格式

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style='border-collapse:collapse;border:none'>
 <tr>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>包名称</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border:solid windowtext 1.0pt;
  border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>温度包</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>发送方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>客户端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>接收方</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>服务端</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>类型码</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:150%;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>06H</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据长度</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>2</span><span
  style='font-family:"Songti SC";color:black'>字节</span></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>数据</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
   style='border-collapse:collapse;border:none'>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>偏移位置</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>大小</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border:solid windowtext 1.0pt;
    border-left:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>0</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>整数部分</span><span
    style='font-family:Times;color:black'> </span></p>
    </td>
   </tr>
   <tr>
    <td width=96 valign=top style='width:72.15pt;border:solid windowtext 1.0pt;
    border-top:none;padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=69 valign=top style='width:51.9pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span lang=EN-US style='font-family:Times;color:black'>1</span></p>
    </td>
    <td width=250 valign=top style='width:187.25pt;border-top:none;border-left:
    none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
    padding:0cm 5.4pt 0cm 5.4pt'>
    <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
    text-autospace:none'><span style='font-family:"Songti SC";color:black'>小数部分</span></p>
    </td>
   </tr>
  </table>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'></p>
  </td>
 </tr>
 <tr style='height:14.85pt'>
  <td width=123 valign=top style='width:91.9pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>说明</span></p>
  </td>
  <td width=430 valign=top style='width:322.6pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0cm 5.4pt 0cm 5.4pt;height:14.85pt'>
  <p class=MsoNormal align=left style='text-align:left;line-height:14.0pt;
  text-autospace:none'><span style='font-family:"Songti SC";color:black'>温度数据包</span></p>
  </td>
 </tr>
</table>