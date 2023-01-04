package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import com.tianwangchong.session.Session;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

/**
 * 群聊消息响应数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}
