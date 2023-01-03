package com.tianwangchong.protocol.response;

import com.tianwangchong.protocol.Packet;
import com.tianwangchong.session.Session;
import lombok.Data;

import java.util.List;

import static com.tianwangchong.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;

/**
 * 群组列表响应数据包
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
