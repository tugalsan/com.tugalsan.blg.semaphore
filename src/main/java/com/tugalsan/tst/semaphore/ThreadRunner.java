package com.tugalsan.tst.semaphore;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class ThreadRunner implements TGS_CallableType1<Void, TS_ThreadSyncTrigger> {

    private static final TS_Log d = TS_Log.of(ThreadRunner.class);

    @Override
    public Void call(TS_ThreadSyncTrigger result) {
        TGS_UnSafe.run(() -> {
            d.cr("run", "Starting thread %s".formatted(threadName));
            d.cr("run", "%s is waiting for a permit.".formatted(threadName));
            semaphore.acquire();
            d.cr("run", "%s gets a permit.".formatted(threadName));
            act(type);
        }, e -> {
            d.ct("run.%s".formatted(threadName), e);
        }, () -> {
            d.cr("run", "Thread %s releases the permit.".formatted(threadName));
            semaphore.release();
        });
        return null;
    }

    public static enum TYPE {
        INCREMENTOR, DECREMENTOR
    };

    public ThreadRunner(Semaphore semaphore, String threadName, TYPE type) {
        this.semaphore = semaphore;
        this.threadName = threadName;
        this.type = type;
    }
    final private Semaphore semaphore;
    final private String threadName;
    final private TYPE type;

    private void act(TYPE type) {
        IntStream.range(0, 5).sequential().forEach(i -> {
            TGS_UnSafe.run(() -> {
                if (type == TYPE.INCREMENTOR) {
                    Shared.count++;
                } else if (type == TYPE.DECREMENTOR) {
                    Shared.count--;
                }
                d.cr("act", "%s: %d".formatted(threadName, Shared.count));
                Thread.sleep(1000);
            });
        });
    }
}
