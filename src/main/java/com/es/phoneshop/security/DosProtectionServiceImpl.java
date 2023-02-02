package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DosProtectionServiceImpl implements DosProtectionService {

    private static final DosProtectionService INSTANCE = new DosProtectionServiceImpl();
    private static final Long THRESHOLD = 20L;
    private static final Long TIME = 60 * 1000L;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, Long> timerMap = new ConcurrentHashMap<>();

    private DosProtectionServiceImpl() {

    }

    @Override
    public boolean isAllowed(String ip) {
        readLock.lock();
        try {
            Long count = countMap.get(ip);
            Long time = System.currentTimeMillis();

            if (count == null) {
                count = 1L;
                timerMap.put(ip, time);
            } else {
                if (time - timerMap.get(ip) > TIME) {
                    count = 0L;
                    timerMap.put(ip, time);
                }
                count++;
                if (count > THRESHOLD) {
                    return false;
                }
            }

            countMap.put(ip, count);
            return true;
        } finally {
            readLock.unlock();
        }
    }

    public static DosProtectionService getInstance() {
        return INSTANCE;
    }
}
