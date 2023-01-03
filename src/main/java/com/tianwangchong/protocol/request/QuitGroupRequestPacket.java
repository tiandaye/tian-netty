package com.tianwangchong.protocol.request;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.QUIT_GROUP_REQUEST;

/**
 * 退出群组数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_REQUEST;
    }
}
