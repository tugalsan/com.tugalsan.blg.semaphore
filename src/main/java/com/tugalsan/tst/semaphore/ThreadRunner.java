package com.tugalsan.tst.semaphore;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.stream.IntStream;

public class ThreadRunner implements TGS_CallableType1<Void, TS_ThreadSyncTrigger> {

    private static final TS_Log d = TS_Log.of(ThreadRunner.class);

    @Override
    public Void call(TS_ThreadSyncTrigger threadKiller) {
        if (threadKiller.hasTriggered()) {
            return null;
        }
        TGS_UnSafe.run(() -> {
            d.cr("run", "Starting thread %s".formatted(threadName));
            d.cr("run", "%s is waiting for a permit.".formatted(threadName));
            Common.SEMAPHORE.acquire();
            d.cr("run", "%s gets a permit.".formatted(threadName));
            act(threadKiller, type);
        }, e -> {
            d.ct("run.%s".formatted(threadName), e);
        }, () -> {
            d.cr("run", "Thread %s releases the permit.".formatted(threadName));
            Common.SEMAPHORE.release();
        });
        return null;
    }

    public static enum TYPE {
        INCREMENTOR, DECREMENTOR
    };

    private ThreadRunner(String threadName, TYPE type) {
        this.threadName = threadName;
        this.type = type;
    }
    final private String threadName;
    final private TYPE type;

    public static ThreadRunner of(String threadName, TYPE type) {
        return new ThreadRunner(threadName, type);
    }

    private void act(TS_ThreadSyncTrigger threadKiller, TYPE type) {
        IntStream.range(0, 5).sequential().forEach(i -> {
            if (threadKiller.hasTriggered()) {
                return;
            }
            TGS_UnSafe.run(() -> {
                if (type == TYPE.INCREMENTOR) {
                    Common.MAX_SIMILTANEOUS_COUNT++;
                } else if (type == TYPE.DECREMENTOR) {
                    Common.MAX_SIMILTANEOUS_COUNT--;
                }
                d.cr("act", "%s: %d".formatted(threadName, Common.MAX_SIMILTANEOUS_COUNT));
                TS_ThreadWait.of(threadName, Common.THEAD_KILLER, Common.WORK_LOAD);
            });
        });
    }
}
