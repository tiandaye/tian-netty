package com.tianwangchong.clinet.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台命令接口
 * <p>
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public interface ConsoleCommand {
    /**
     * 执行命令
     *
     * @param scanner
     * @param channel
     */
    void exec(Scanner scanner, Channel channel);
}
