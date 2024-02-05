package com.tugalsan.tst.semaphore;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.Semaphore;

//https://www.javatpoint.com/java-semaphore
public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.semaphore
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.semaphore-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        TGS_UnSafe.run(() -> {
            var semaphore = new Semaphore(1);
            var await = TS_ThreadAsyncAwait.callParallel(null, null,
                    new ThreadRunner(semaphore, "incrementor0", ThreadRunner.TYPE.INCREMENTOR),
                    new ThreadRunner(semaphore, "incrementor1", ThreadRunner.TYPE.INCREMENTOR),
                    new ThreadRunner(semaphore, "decrementor0", ThreadRunner.TYPE.DECREMENTOR),
                    new ThreadRunner(semaphore, "decrementor1", ThreadRunner.TYPE.DECREMENTOR)
            );
            System.out.println("await.hasError: " + await.hasError());
            System.out.println("count: " + Shared.count);
        }, e -> e.printStackTrace());
    }

}
