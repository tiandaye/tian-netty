package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * 服务端发送至客户端的消息
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}