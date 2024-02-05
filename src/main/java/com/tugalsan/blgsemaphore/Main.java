package com.tugalsan.blgsemaphore;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;

//https://www.javatpoint.com/java-semaphore
public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.semaphore
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        TGS_UnSafe.run(() -> {
            var await = TS_ThreadAsyncAwait.callParallel(Common.THEAD_KILLER, Common.UNTIL,
                    ThreadRunner.of("incrementor0", ThreadRunner.TYPE.INCREMENTOR),
                    ThreadRunner.of("incrementor1", ThreadRunner.TYPE.INCREMENTOR),
                    ThreadRunner.of("decrementor0", ThreadRunner.TYPE.DECREMENTOR),
                    ThreadRunner.of("decrementor1", ThreadRunner.TYPE.DECREMENTOR)
            );
            d.cr("main", "await.hasError: %b".formatted(await.hasError()));
            d.cr("main", "count: %d".formatted(Common.COUNT));
        }, e -> d.ce("main", e));
    }
}
