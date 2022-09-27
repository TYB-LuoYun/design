package top.anets.modules.threads.queues;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author ftm
 * @date 2022/9/23 0023 10:53
 *
 * 由优先级堆支持的，基于时间的调度队列
 *
 * 场景
 * 商城订单超时关闭、异步短信通知功能、关闭空链接、缓存失效、任务超时处理。
 *
 *
 * DelayQueue 是一个支持延时获取元素的阻塞队列， 内部采用优先队列 PriorityQueue 存储
 * 元素，同时元素必须实现 Delayed 接口；在创建元素时可以指定多久才可以从队列中获取当前元
 * 素，只有在延迟期满时才能从队列中提取元素。延迟队列的特点是：不是先进先出，而是会按照
 * 延迟时间的长短来排序，下一个即将执行的任务会排到队列的最前面。
 * 它是无界队列，放入的元素必须实现 Delayed 接口，而 Delayed 接口又继承了
 * Comparable 接口，所以自然就拥有了比较和排序的能力
 */


/**
 * 模拟订单支付，创建订单
 */
public class DelayQueueDemo {
    static String[] str = new String[]{ "成功", "支付中", "订单初始化" };

    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String currentTime = formatter.format(date);
        return currentTime;
    }

    public static void main(String[] args) throws InterruptedException {
        OrderOverTimeClose.getInstance().init();

        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    // 创建初始订单
                    long createTime = System.currentTimeMillis();
                    String currentTime = getTime(createTime);
                    //设置订单过期时间
                    String overTime = getTime(createTime + 10000);// 10s后超时
                    String orderNo = String.valueOf(new Random().nextLong());
                    OrderInfo order = new OrderInfo();
                    order.setOrderNo(orderNo);
                    order.setExpTime(overTime);
                    int random_index = (int) (Math.random() * str.length);
                    //设置订单状态
                    order.setStatus(str[random_index]);
                    order.setCreateTime(currentTime);
                    //插入订单到延时队列中  注意并发场景下使用单例模式，避免重复创建对象；
                    OrderOverTimeClose.getInstance().orderPutQueue(order, currentTime, overTime);
                }
            };
            service.execute(run);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}



class OrderInfo implements Delayed, Serializable {

    private static final long serialVersionUID = 1L;
    private String orderNo;// 订单号
    private String status;// 订单状态
    private String expTime;// 订单过期时间
    private String createTime;//订单创建时间

    /**
     * 用于延时队列内部比较排序：当前订单的过期时间 与 队列中对象的过期时间 比较
     */
    @Override
    public int compareTo(Delayed o) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nowThreadtime = 0;
        long queueThreadtime = 0;
        try {
            nowThreadtime = formatter.parse(this.expTime).getTime();
            queueThreadtime = formatter.parse(((OrderInfo) o).expTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(nowThreadtime).compareTo(Long.valueOf(queueThreadtime));
    }

    /**
     * 时间单位：秒
     * 延迟关闭时间 = 过期时间 - 当前时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time = formatter.parse(this.expTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time - System.currentTimeMillis();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String overTime) {
        this.expTime = overTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}




/**
 * 使用延时队列DelayQueue实现订单超时关闭
 * 后台守护线程不断的执行检测工作
 *
 */

class OrderOverTimeClose {

    private volatile static OrderOverTimeClose oderOverTimeClose = null;

    private OrderOverTimeClose() {

    }

    /**
     * 单例模式，双检查锁模式，在并发环境下对象只被初始化一次
     */
    public static OrderOverTimeClose getInstance() {
        if (oderOverTimeClose == null) {
            synchronized (OrderOverTimeClose.class) {
                if (oderOverTimeClose == null) {
                    oderOverTimeClose = new OrderOverTimeClose();
                }
            }
        }
        return oderOverTimeClose;
    }

    /**
     * 守护线程
     */
    private Thread mainThread;

    /**
     * 启动方法
     */
    public void init() {
        mainThread = new Thread(() -> execute());
        mainThread.setDaemon(true);
        mainThread.setName("守护线程-->");
        mainThread.start();
    }

    /**
     * 创建空延时队列
     */
    private DelayQueue<OrderInfo> queue = new DelayQueue<OrderInfo>();

    /**
     * 读取延时队列，关闭超时订单
     */
    private void execute() {
        while (true) {
            try {
                if (queue.size() > 0) {
                    //从队列里获取超时的订单
                    OrderInfo orderInfo = queue.take();
                    // 检查订单状态，是否已经成功，成功则将订单从队列中删除。
                    if (Objects.equals(orderInfo.getStatus(), "成功")) {
                        //TODO 支付成功的订单处理逻辑
                        System.out.println("线程：" + Thread.currentThread().getName() + "，订单号："
                                + orderInfo.getOrderNo() + "，订单状态："
                                + orderInfo.getStatus() + "，订单创建时间："
                                + orderInfo.getCreateTime()
                                + "，订单超时时间：" + orderInfo.getExpTime() + "，当前时间："
                                + DelayQueueDemo.getTime(System.currentTimeMillis()));
                        Thread.sleep(2000);
                    } else {
                        // TODO 超时未支付订单处理逻辑
                        System.out.println("线程：" + Thread.currentThread().getName() + "，订单号："
                                + orderInfo.getOrderNo() + "，变更订单状态为：超时关闭"
                                + "，订单创建时间："
                                + orderInfo.getCreateTime()
                                + "，订单超时时间：" + orderInfo.getExpTime() + "，当前时间："
                                + DelayQueueDemo.getTime(System.currentTimeMillis()));
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入订单到延时队列中
     */
    public void orderPutQueue(OrderInfo orderInfo, String createTime,
                              String overTime) {
        System.out.println("订单号：" + orderInfo.getOrderNo()
                +"，订单状态："+ orderInfo.getStatus()
                +"，订单创建时间："+ createTime
                + "，订单过期时间：" + overTime);
        queue.add(orderInfo);
    }

}