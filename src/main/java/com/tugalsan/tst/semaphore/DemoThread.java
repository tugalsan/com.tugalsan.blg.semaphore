package com.tugalsan.tst.semaphore;

import java.util.concurrent.Semaphore;

public class DemoThread extends Thread {

    public static enum TYPE {
        INCREMENTOR, DECREMENTOR
    };

    Semaphore semaphore;
    String threadName;
    TYPE type;

    public DemoThread(Semaphore semaphore, TYPE type, String threadName) {
        super(threadName);
        this.semaphore = semaphore;
        this.type = type;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        if (this.type == TYPE.INCREMENTOR) {
            System.out.println("Starting thread " + threadName);
            try {
                System.out.println(threadName + " is waiting for a permit.");
                semaphore.acquire();
                System.out.println(threadName + " gets a permit.");
                for (int i = 0; i < 5; i++) {
                    Shared.count++;
                    System.out.println(threadName + ": " + Shared.count);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Thread " + threadName + " releases the permit.");
                semaphore.release();
            }
        } else if (this.type == TYPE.DECREMENTOR) {  
            System.out.println("Starting thread " + threadName);
            try {
                System.out.println("Thread " + threadName + " is waiting for a permit.");
                semaphore.acquire();
                System.out.println("Thread " + threadName + " gets a permit.");
                for (int i = 0; i < 5; i++) {
                    Shared.count--;
                    System.out.println(threadName + ": " + Shared.count);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Thread " + threadName + " releases the permit.");
                semaphore.release();
            }
        }
    }
}
