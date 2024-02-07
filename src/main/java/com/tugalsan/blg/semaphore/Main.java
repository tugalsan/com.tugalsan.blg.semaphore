package com.tugalsan.blg.semaphore;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.time.Duration;
import java.util.concurrent.Semaphore;

//https://www.javatpoint.com/java-semaphore
public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.semaphore
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        TGS_UnSafe.run(() -> {
            var threadKiller = TS_ThreadSyncTrigger.of();//not used
            var threadLimitor = new Semaphore(2);
            var threadUntil = Duration.ofMinutes(5);//AN EXXECCESSIVE AMOUNT
            var await = TS_ThreadAsyncAwait.callParallel(threadKiller, threadUntil,
                    Caller.of("#1", threadLimitor),//Runner.of("incrementor0", Runner.TYPE.INCREMENTOR),
                    Caller.of("#2", threadLimitor),//Runner.of("incrementor1", Runner.TYPE.INCREMENTOR),
                    Caller.of("#3", threadLimitor),//Runner.of("decrementor0", Runner.TYPE.DECREMENTOR),
                    Caller.of("#4", threadLimitor)//Runner.of("decrementor1", Runner.TYPE.DECREMENTOR)
            );
            d.cr("main", "await.hasError: %b".formatted(await.hasError()));
//            d.cr("main", "count: %d".formatted(Common.COUNT));
        }, e -> d.ce("main", e));
    }
}
