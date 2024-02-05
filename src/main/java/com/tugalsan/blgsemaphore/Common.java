package com.tugalsan.blgsemaphore;

import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;
import java.util.concurrent.Semaphore;

public class Common {

    protected static int COUNT = 0;
    final protected static Semaphore SEMAPHORE = new Semaphore(1);
    final protected static TS_ThreadSyncTrigger THEAD_KILLER = TS_ThreadSyncTrigger.of();//NOT USED IN THIS PROJECT
    final protected static Duration UNTIL = Duration.ofMinutes(5);//AN EXXECCESSIVE AMOUNT
    final protected static Duration WORK_LOAD = Duration.ofSeconds(1);//A SMALL AMOUNT
}
