package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.tianwangchong.protocol.command.Command.CREATE_GROUP_RESPONSE;

/**
 * 创建群组响应
 *
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class CreateGroupResponsePacket extends Packet {
    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_RESPONSE;
    }
}
