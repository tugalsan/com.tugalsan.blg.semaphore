package com.tugalsan.blg.semaphore;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.stream.IntStream;

public class Caller implements TGS_CallableType1<Void, TS_ThreadSyncTrigger> {

    private static final TS_Log d = TS_Log.of(Caller.class);

    @Override
    public Void call(TS_ThreadSyncTrigger threadKiller) {
        act(threadKiller);
        return null;
    }

    private Caller(String threadName) {
        this.threadName = threadName;
    }
    final private String threadName;

    public static Caller of(String threadName) {
        return new Caller(threadName);
    }

    private void act(TS_ThreadSyncTrigger threadKiller/*, TYPE type*/) {
        IntStream.range(0, 5).sequential().forEach(i -> {
            if (threadKiller.hasTriggered()) {
                return;
            }
            TGS_UnSafe.run(() -> {
                d.cr("act", "%s: running...".formatted(threadName));
                TS_ThreadWait.of(threadName, threadKiller, WORK_LOAD);
            });
        });
    }
    final private static Duration WORK_LOAD = Duration.ofSeconds(1);//A SMALL AMOUNT
}
