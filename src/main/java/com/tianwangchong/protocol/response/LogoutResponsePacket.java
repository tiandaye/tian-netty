package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.LOGOUT_RESPONSE;

/**
 * 登出响应
 *
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}
