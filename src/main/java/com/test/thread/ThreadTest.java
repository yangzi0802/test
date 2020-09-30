package com.test.thread;

/**
 * @author : yangfq
 * @version V3.4
 * @Project: test
 * @Package com.test.thread
 * @Description: TODO
 * @date Date : 2020年09月23日 11:24
 */
public class ThreadTest {

    private static class MyThread extends Thread{
        @Override
        public void run() {
            while (true) {
                System.out.println("myThread running....");
            }
        }
    }

    private static class MyRunnable implements Runnable{
        public void run() {
            while (true) {
                System.out.println("MyRunnable running....");
            }
        }
    }

    public static void main(String[] args) {
        // 1、继承Thread类
        /*MyThread myThread = new MyThread();
        myThread.start();*/


        // 2、实现Runnable接口
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

}
