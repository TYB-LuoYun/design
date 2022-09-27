package top.anets.modules.threads.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2022/9/23 0023 13:20
 */
public class PriorityThreadPoolExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("同步请求");
        ThreadPoolUtils.getThreadPoolPriority().submit(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());

                Future<?> submit = ThreadPoolUtils.getThreadPoolPriority().submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                        System.out.println("开始解析......");
                    }
                }, 20);

                Future<?> beforesubmit = ThreadPoolUtils.getThreadPoolPriority().submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                        System.out.println("解析前的处理");
                    }
                }, 50);

                Future<?> datas = ThreadPoolUtils.getThreadPoolPriority().submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                        System.out.println("数据存储");
                    }
                }, 10);


                try {

                    System.out.println("等待解析完成");
                    Object o = submit.get();
                    System.out.println("解析完成......");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("同步返回");
    }

    public void testDefault() throws InterruptedException, ExecutionException {
        PriorityThreadPoolExecutor pool = new PriorityThreadPoolExecutor(1, 1000, 1, TimeUnit.MINUTES);

        Future[] futures = new Future[20];
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < futures.length; i++) {
            int index = i;
            futures[i] = pool.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    Thread.sleep(10000);
                    buffer.append(index + ", ");
                    return null;
                }
            });
        }
        // 等待所有任务结束
        for (int i = 0; i < futures.length; i++) {
            System.out.println("等待所有任务结束"+i);
            futures[i].get();
            System.out.println("所有任务都结束了");
        }
        System.out.println("执行结束");
        System.out.println(buffer);
//        assertEquals("0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, ", buffer.toString());
    }

    public void testSamePriority() throws InterruptedException, ExecutionException {
        PriorityThreadPoolExecutor pool = new PriorityThreadPoolExecutor(1, 1000, 1, TimeUnit.MINUTES);

        Future[] futures = new Future[10];
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < futures.length; i++) {
            futures[i] = pool.submit(new TenSecondTask(i, 1, buffer), 1);
        }
        // 等待所有任务结束
        for (int i = 0; i < futures.length; i++) {
            futures[i].get();
        }
        System.out.println(buffer);
//        assertEquals("01@00, 01@01, 01@02, 01@03, 01@04, 01@05, 01@06, 01@07, 01@08, 01@09, ", buffer.toString());
    }


    public void testRandomPriority() throws InterruptedException, ExecutionException {
        PriorityThreadPoolExecutor pool = new PriorityThreadPoolExecutor(1, 1000, 1, TimeUnit.MINUTES);

        Future[] futures = new Future[20];
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < futures.length; i++) {
            int r = (int) (Math.random() * 100);
            futures[i] = pool.submit(new TenSecondTask(i, r, buffer), r);
        }
        // 等待所有任务结束
        for (int i = 0; i < futures.length; i++) {
            System.out.println("等待所有任务结束"+i);
            futures[i].get();
            System.out.println("所有任务都结束了");
        }

        buffer.append("01@00");
        System.out.println(buffer);
        String[] split = buffer.toString().split(", ");
        // 从 2 开始, 因为前面的任务可能已经开始
        for (int i = 2; i < split.length - 1; i++) {
            String s = split[i].split("@")[0];
//            assertTrue(Integer.valueOf(s) >= Integer.valueOf(split[i + 1].split("@")[0]));
        }
    }

    public static class TenSecondTask<T> implements Callable<T> {
        private StringBuffer buffer;
        int index;
        int priority;

        public TenSecondTask(int index, int priority, StringBuffer buffer) {
            this.index = index;
            this.priority = priority;
            this.buffer = buffer;
        }

        @Override
        public T call() throws Exception {
            Thread.sleep(10000);
            System.out.println("优先级别："+priority);
            buffer.append(String.format("%02d@%02d", this.priority, index)).append(", ");
            return null;
        }
    }
}
