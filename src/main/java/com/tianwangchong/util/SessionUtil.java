package com.tianwangchong.util;

import com.tianwangchong.attribute.Attributes;
import com.tianwangchong.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class SessionUtil {

    // userId -> channel 的映射
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    // groupId -> channelGroup 的映射
    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    /**
     * 绑定 userId -> channel 的映射
     *
     * @param session
     * @param channel
     */
    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 解除绑定 userId -> channel 的映射
     *
     * @param channel
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
            System.out.println(session + " 退出登录!");
        }
    }

    /**
     * 判断是否登录
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {

        return getSession(channel) != null;
    }

    /**
     * 拿到对应 channel 的会话信息
     *
     * @param channel
     * @return
     */
    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    /**
     * 通过 userId 拿到对应的 channel
     *
     * @param userId
     * @return
     */
    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

    /**
     * 绑定 groupId -> channelGroup
     *
     * @param groupId
     * @param channelGroup
     */
    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    /**
     * 通过groupId 获得 channelGroup
     *
     * @param groupId
     * @return
     */
    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }
}
