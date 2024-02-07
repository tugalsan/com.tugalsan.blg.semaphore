package com.tugalsan.blg.semaphore;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class Caller implements TGS_CallableType1<Void, TS_ThreadSyncTrigger> {

    private static final TS_Log d = TS_Log.of(Caller.class);

    @Override
    public Void call(TS_ThreadSyncTrigger threadKiller) {
        if (threadKiller.hasTriggered()) {
            return null;
        }
        TGS_UnSafe.run(() -> {
            d.cr("run", "Starting thread %s".formatted(threadName));
            d.cr("run", "%s is waiting for a permit.".formatted(threadName));
            threadLimitor.acquire();
            d.cr("run", "%s gets a permit.".formatted(threadName));
            act(threadKiller/*, type*/);
        }, e -> {
            d.ct("run.%s".formatted(threadName), e);
        }, () -> {
            d.cr("run", "Thread %s releases the permit.".formatted(threadName));
            threadLimitor.release();
        });
        return null;
    }

    private Caller(String threadName, Semaphore threadLimitor) {
        this.threadName = threadName;
        this.threadLimitor = threadLimitor;
    }
    final private String threadName;
    final private Semaphore threadLimitor;

    public static Caller of(String threadName, Semaphore threadLimitor) {
        return new Caller(threadName, threadLimitor);
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
