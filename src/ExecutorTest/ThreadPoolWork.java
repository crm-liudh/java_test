package ExecutorTest;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolWork extends ThreadPoolExecutor {

    public ThreadPoolWork(int corePoolSize, int maximumPoolSize,
                          long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    //任务个数
    private final AtomicInteger numTasks = new AtomicInteger();
    //所有线程总执行时间
    private final AtomicLong totalTimes = new AtomicLong();
    private final AtomicLong totalTimes1 = new AtomicLong();
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    public void demo() {
        ThreadPoolExecutor pool = new ThreadPoolWork(
                2,
                4,
                3,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(6));
        int taskMaxNumber = 10;
        for(int i=0; i<taskMaxNumber; i++){
            Runnable task = new Runnable(){
                //@Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"任务正在执行");
                    try {
                        int rand = getRandom(1000,2000);
                        Thread.sleep(rand);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            pool.execute(task);
        }
        pool.shutdown();
    }
    //取某个范围的任意数
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    //在执行给定线程中的给定 Runnable 之前调用的方法
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println(Thread.currentThread().getName()+"任务执行之前..");
        startTime.set(System.currentTimeMillis());
    }

    //基于完成执行给定 Runnable 所调用的方法
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        long endTime = System.currentTimeMillis();
        long tasktime = endTime - startTime.get();
        numTasks.incrementAndGet();
        System.out.println(Thread.currentThread().getName()+"任务执行之后需要的时长："+tasktime);
        totalTimes.addAndGet(tasktime);
    }

    //当 Executor 已经终止时调用的方法
    @Override
    protected void terminated() {
        super.terminated();
        System.out.println("全部完成.......");
        System.out.println("共有几个任务执行："+numTasks);
        System.out.println("任务共消耗的时间："+totalTimes.get());
    }
}