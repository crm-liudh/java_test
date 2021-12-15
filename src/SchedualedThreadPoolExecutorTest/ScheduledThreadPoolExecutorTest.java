package SchedualedThreadPoolExecutorTest;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {

    public void demo() {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟执行");
            }
        }, 1, TimeUnit.SECONDS);

        /**
         * 这个执行周期是固定，不管任务执行多长时间，还是每过3秒中就会产生一个新的任务
         */
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //这个业务逻辑需要很长的时间，定时任务去统计一张数据上亿的表，财务财务信息，需要30min
                System.out.println("重复执行1");
            }
        }, 1, 3, TimeUnit.SECONDS);

        /**
         * 半小时执行一次
         */
        pool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //30min
                try {
                    Thread.sleep(60000 * 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("" + new Date() + "重复执行2");
            }
        }, 1, 3, TimeUnit.SECONDS);
    }
}
