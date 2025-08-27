package com.bosc.txx.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static String generateAccountId() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int seq = counter.incrementAndGet(); // 简单实现，生产上可用Redis或DB代替
        return String.format("ACC%s%04d", date, seq);
    }
}

