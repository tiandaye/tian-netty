package com.tianwangchong.protocol.request;

import com.tianwangchong.protocol.Packet;
import lombok.Data;

import static com.tianwangchong.protocol.command.Command.LOGOUT_REQUEST;

/**
 * 登出请求数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return LOGOUT_REQUEST;
    }
}
