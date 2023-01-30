package com.es.phoneshop.security;

import java.util.Map;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {

    private static final DosProtectionService INSTANCE = new DosProtectionServiceImpl();
    private static final long THRESHOLD = 20;
    private static final Long TIME = 60 * 1000L;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private Map<String, Timer> timers = new HashMap<>();

    private DosProtectionServiceImpl() {

    }
    private String currentIp;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            countMap.put(currentIp, 0L);

        }
    };

    private void setTimerForIp(String ip) {
        Timer timer = timers.get(ip);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(task, 0L, TIME);
            timers.put(ip, timer);
        }
    }

    @Override
    public synchronized boolean isAllowed(String ip) {
        currentIp = ip;
        setTimerForIp(ip);

        Long count = countMap.get(ip);

        if (count == null || count.equals(0L)) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }

        countMap.put(ip, count);
        return true;
    }

    private static Long resetCount() {
        return Long.valueOf(0);
    }

    public static DosProtectionService getInstance() {
        return INSTANCE;
    }
}
