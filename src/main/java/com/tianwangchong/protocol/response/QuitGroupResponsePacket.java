package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.QUIT_GROUP_RESPONSE;

/**
 * 退出群组响应数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_RESPONSE;
    }
}
