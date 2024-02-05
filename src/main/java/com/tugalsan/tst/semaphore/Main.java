package com.tugalsan.tst.semaphore;

import com.tugalsan.api.log.server.TS_Log;
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
            var incrementor = new DemoThread(semaphore, "A");
            var decrementor = new DemoThread(semaphore, "B");
            incrementor.start();
            decrementor.start();
            incrementor.join();
            decrementor.join();
            System.out.println("count: " + Shared.count);
        }, e -> e.printStackTrace());
    }

}
