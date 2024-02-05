package com.tugalsan.tst.semaphore;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.socket.server.TS_SocketClient;
import com.tugalsan.api.socket.server.TS_SocketServer;
import com.tugalsan.api.socket.server.TS_SocketUtils;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.util.stream.IntStream;

public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.socket
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.socket-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.socket-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        var killTrigger = TS_ThreadSyncTrigger.of();
        var port = 8282;
        if (!TS_SocketUtils.available(8282)) {
            d.ce("main", "ERROR: Port in use already!", port);
            return;
        }
        var server = TS_SocketServer.of(killTrigger, port, forEachReceivedLine -> {
            return forEachReceivedLine.toUpperCase();
        }).start();
        var client = TS_SocketClient.of(killTrigger, port, forEachReceivedLine -> {
            d.cr("client", "server sent", forEachReceivedLine);
        }).start();

        IntStream.range(0, 10).forEachOrdered(i -> {
            TS_ThreadWait.milliseconds100();
            client.addToQueue("ĞÜğüŞİşiÖçöçIıı " + i);
        });
        TS_ThreadWait.seconds(null, killTrigger, 15);
    }

}
