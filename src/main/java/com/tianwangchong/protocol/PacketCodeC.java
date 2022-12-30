package com.tianwangchong.protocol;

import com.tianwangchong.protocol.request.LoginRequestPacket;
import com.tianwangchong.protocol.request.MessageRequestPacket;
import com.tianwangchong.protocol.response.LoginResponsePacket;
import com.tianwangchong.protocol.response.MessageResponsePacket;
import com.tianwangchong.serialize.Serializer;
import com.tianwangchong.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.tianwangchong.protocol.command.Command.LOGIN_REQUEST;
import static com.tianwangchong.protocol.command.Command.LOGIN_RESPONSE;
import static com.tianwangchong.protocol.command.Command.MESSAGE_REQUEST;
import static com.tianwangchong.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * 解编码
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class PacketCodeC {

    // 魔数
    public static final int MAGIC_NUMBER = 0x12345678;

    // 单例
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        // 指令=>数据包的map
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        // 序列化=>序列化类的map
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 和下面这个方法相比, 减少了创建 ByteBuf 对象
     * @param byteBuf
     * @param packet
     */
    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 编码：封装成二进制的过程
     *
     * @param packet 数据包
     * @return
     */
    public ByteBuf encode(Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        // 魔数-4个字节(0x12345678)
        byteBuf.writeInt(MAGIC_NUMBER);
        // 版本号-1个字节
        byteBuf.writeByte(packet.getVersion());
        // 序列化算法-1个字节
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 指令-1个字节
        byteBuf.writeByte(packet.getCommand());
        // 数据长度-4个字节
        byteBuf.writeInt(bytes.length);
        // 数据-n个字节
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码：解析 Java 对象的过程
     *
     * @param byteBuf ByteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number(4个字节)
        byteBuf.skipBytes(4);

        // 跳过版本号(1个字节)
        byteBuf.skipBytes(1);

        // 序列化算法(1个字节)
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令(1个字节)
        byte command = byteBuf.readByte();

        // 数据包长度(4个字节)
        int length = byteBuf.readInt();

        // 真实数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
