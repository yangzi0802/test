package com.test.thread;

/**
 * @author : yangfq
 * @version V3.4
 * @Project: test
 * @Package com.test.thread
 * @Description: TODO
 * @date Date : 2020年09月23日 11:29
 */
public class ThreadInterruptTest {

    private static class MyRunnable implements Runnable{

        public void run() {
            try {
                System.out.println("thread interrupt flag = " + Thread.currentThread().isInterrupted());
                while (Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                    System.out.println("MyRunnable is running....");
                }
                System.out.println("thread interrupt after flag = " + Thread.currentThread().isInterrupted());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
                System.out.println("thread interrupt catch flag = " + Thread.currentThread().isInterrupted());
            }

        }
    }


    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
        thread.interrupt();
    }
}
