package com.tianwangchong.util;

import java.util.UUID;

/**
 * Copyright (c) 2023, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
