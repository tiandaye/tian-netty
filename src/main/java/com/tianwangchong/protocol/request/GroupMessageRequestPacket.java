package com.tianwangchong.protocol.request;

import com.tianwangchong.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tianwangchong.protocol.command.Command.GROUP_MESSAGE_REQUEST;

/**
 * 群聊消息请求数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {
    private String toGroupId;
    private String message;

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}