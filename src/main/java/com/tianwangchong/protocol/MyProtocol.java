package com.tianwangchong.protocol;

/**
 * 私有协议-消息类
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MyProtocol {

    /**
     * 消息的开头的信息标志
     */
    private String head = "CYRC";

    /**
     * 消息的长度
     */
    private int contentLength;

    /**
     * 消息的内容
     */
    private byte[] content;

    public MyProtocol(int contentLength, byte[] content) {
        this.contentLength = contentLength;
        this.content = content;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String byteToHex(byte[] bytes, int cnt) {
        String strHex;
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < cnt; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return "MyProtocol [head=" + head + ", contentLength="
                + contentLength + ", content=" + byteToHex(content, contentLength) + "]";
    }
}
