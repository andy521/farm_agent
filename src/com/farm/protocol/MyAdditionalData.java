package com.farm.protocol;


/**
 * @author: taoroot
 * @date: 2018/1/19
 * @description: 附加信息
 */
public class MyAdditionalData {
    private String macAddress;
    private byte model;

    public String getMacAddress() {
        return macAddress;
    }

    public MyAdditionalData setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public byte getModel() {
        return model;
    }

    public MyAdditionalData setModel(byte model) {
        this.model = model;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("\"macAddress\":\"")
                .append(macAddress).append('\"');
        sb.append(",\"model\":\"")
                .append(model).append('\"');
        return sb.toString();
    }
}
