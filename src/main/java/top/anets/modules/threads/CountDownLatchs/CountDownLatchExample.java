package top.anets.modules.threads.CountDownLatchs;

import java.util.concurrent.CountDownLatch;

/**
 * @author ftm
 * @date 2022/10/18 0018 10:54
 *
 *
 * CountDownLatch允许一个或者多个线程去等待其他线程完成操作。做计数器用，如果没达到一个值，后面的代码就一直等待--await效果
 *
 * CountDownLatch接收一个int型参数，表示要等待的工作线程的个数。
 *
 * 当然也不一定是多线程，在单线程中可以用这个int型参数表示多个操作步骤。
 *
 *
 *
 * await()   使当前线程进入同步队列进行等待，直到latch的值被减到0或者当前线程被中断，当前线程就会被唤醒。
 * countDown()	使latch的值减1，如果减到了0，则会唤醒所有等待在这个latch上的线程。
 *
 */
public class CountDownLatchExample {


    public static void main(String[] args) throws InterruptedException {
        // 让2个线程去等待3个三个工作线程执行完成
        CountDownLatch c = new CountDownLatch(3);

        // 2 个等待线程
        WaitThread waitThread1 = new WaitThread("wait-thread-1", c);
        WaitThread waitThread2 = new WaitThread("wait-thread-2", c);

        // 3个工作线程
        Worker worker1 = new Worker("worker-thread-1", c);
        Worker worker2 = new Worker("worker-thread-2", c);
        Worker worker3 = new Worker("worker-thread-3", c);

        // 启动所有线程
        waitThread1.start();
        waitThread2.start();
        Thread.sleep(1000);
        worker1.start();
        worker2.start();
        worker3.start();
    }
}


/**
 * 等待线程
 */
class WaitThread extends Thread {

    private String name;
    private CountDownLatch c;

    public WaitThread(String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        try {
            // 等待
            System.out.println(this.name + " wait...");
            c.await();
            System.out.println(this.name + " continue running...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 工作线程
 */
class Worker extends Thread {

    private String name;
    private CountDownLatch c;

    public Worker(String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        System.out.println(this.name + " is running...");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " is end.");
        c.countDown();
    }
}
