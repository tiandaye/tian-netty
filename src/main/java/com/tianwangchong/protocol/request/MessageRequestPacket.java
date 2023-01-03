package com.tianwangchong.protocol.request;

import com.tianwangchong.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tianwangchong.protocol.command.Command.MESSAGE_REQUEST;

/**
 * 客户端发送至服务端的消息
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}