package top.anets.modules.threads.queues;

/**
 * @author ftm
 * @date 2022/9/23 0023 10:50
 *
 * LinkedBlockingQueue是一个基于链表实现的阻塞队列，默认情况下，该阻塞队列的大小为
 * Integer.MAX_VALUE，由于这个数值特别大，所以 LinkedBlockingQueue 也被称作无界队列，
 * 代表它几乎没有界限，队列可以随着元素的添加而动态增长，但是如果没有剩余内存，则队列
 * 将抛出OOM错误。所以为了避免队列过大造成机器负载或者内存爆满的情况出现，我们在使用的
 * 时候建议手动传一个队列的大小。
 * LinkedBlockingQueue内部由单链表实现，只能从head取元素，从tail添加元素。
 * LinkedBlockingQueue采用两把锁的锁分离技术实现入队出队互不阻塞，添加元素和获取元素都
 * 有独立的锁，也就是说LinkedBlockingQueue是读写分离的，读写操作可以并行执行。
 */
public class LinkedBlockingQueueDemo {

}
