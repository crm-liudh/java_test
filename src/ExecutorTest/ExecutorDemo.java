package ExecutorTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorDemo {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static volatile AtomicInteger atomicInteger = new AtomicInteger(0);

    public void demo1(){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    atomicInteger.incrementAndGet();
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(atomicInteger);
    }

    public void demo2(){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    atomicInteger.incrementAndGet();
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(atomicInteger);
    }
}
