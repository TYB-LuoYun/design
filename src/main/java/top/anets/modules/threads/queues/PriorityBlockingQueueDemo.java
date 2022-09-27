package top.anets.modules.threads.queues;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author ftm
 * @date 2022/9/23 0023 10:51
 *
 * 无界的基于数组的优先级阻塞队列,每次出队都返 回优先级别最高的或者最低的元素 ,不能保证同优先级元素的顺序。
 * 场景 ： 电商抢购活动，会员级别高的用户优先抢购到商品 银行办理业务，vip客户插队
 *
 *
 *
 * PriorityBlockingQueue是一个无界的基于数组的优先级阻塞队列，数组的默认长度是
 * 11，虽然指定了数组的长度，但是可以无限的扩充，直到资源消耗尽为止，每次出队都返
 * 回优先级别最高的或者最低的元素。默认情况下元素采用自然顺序升序排序，当然我们也可以
 * 通过构造函数来指定Comparator来对元素进行排序。需要注意的是PriorityBlockingQueue不能
 * 保证同优先级元素的顺序。
 * 优先级队列PriorityQueue： 队列中每个元素都有一个优先级，出队的时候，优先级最高的
 * 先出。
 */
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        //创建优先级阻塞队列  Comparator为null,自然排序
        PriorityBlockingQueue<Integer> queue=new PriorityBlockingQueue<Integer>(5);
//         自定义Comparator
//        PriorityBlockingQueue queue=new PriorityBlockingQueue<Integer>(
//                5, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o2-o1;
//            }
//        });


        Random random = new Random();
        System.out.println("put:");
        for (int i = 0; i < 5; i++) {
            int j = random.nextInt(100);
            System.out.print(j+"  ");
            queue.put(j);
        }

        System.out.println("\ntake:");
        for (int i = 0; i < 5; i++) {
            System.out.print(queue.take()+"  ");
        }


    }
}
