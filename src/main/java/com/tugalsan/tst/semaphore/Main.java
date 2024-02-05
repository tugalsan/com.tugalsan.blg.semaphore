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
            var incrementor0 = new DemoThread(semaphore,DemoThread.TYPE.INCREMENTOR, "incrementor0");
            var incrementor1 = new DemoThread(semaphore, DemoThread.TYPE.INCREMENTOR, "incrementor1");
            var decrementor0 = new DemoThread(semaphore, DemoThread.TYPE.DECREMENTOR, "decrementor0");
            var decrementor1 = new DemoThread(semaphore, DemoThread.TYPE.DECREMENTOR, "decrementor1");
            incrementor0.start();
            incrementor1.start();
            decrementor0.start();
            decrementor1.start();
            incrementor0.join();
            incrementor1.join();
            decrementor0.join();
            decrementor1.join();
            System.out.println("count: " + Shared.count);
        }, e -> e.printStackTrace());
    }

}
