package com.tianwangchong.protocol.request;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.tianwangchong.protocol.command.Command.CREATE_GROUP_REQUEST;

/**
 * 创建群组请求数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_REQUEST;
    }
}
