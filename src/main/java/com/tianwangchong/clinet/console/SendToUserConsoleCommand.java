package com.tianwangchong.clinet.console;

import com.tianwangchong.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 发送消息给某个用户
 *
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个某个用户：");

        String toUserId = scanner.next();
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }
}
