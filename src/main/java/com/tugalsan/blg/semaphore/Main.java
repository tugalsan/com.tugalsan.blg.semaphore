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
            var await = TS_ThreadAsyncAwait.callParallel(threadKiller, threadLimitor, threadUntil,
                    Caller.of("#1"),
                    Caller.of("#2"),
                    Caller.of("#3"),
                    Caller.of("#4")
            );
            d.cr("main", "await.hasError: %b".formatted(await.hasError()));
        }, e -> d.ce("main", e));
    }
}
