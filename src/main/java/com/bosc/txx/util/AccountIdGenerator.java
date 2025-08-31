package com.bosc.txx.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AccountIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final long EPOCH = 1625132800000L;  // 自定义起始时间（例如，2021年7月1日）

    public static String generateAccountId() {
        long currentTimeMillis = System.currentTimeMillis() - EPOCH; // 当前时间的毫秒数
        int seq = counter.incrementAndGet(); // 自增序列

        // 合成唯一 ID
        Long result = (currentTimeMillis << 16) | (seq & 0xFFFF); // 将时间戳和序列号合并，保证全局唯一
        return String.valueOf(result);
    }
}


