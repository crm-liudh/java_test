
import Day1.SftpTask;
import Day1.SftpTime;
import Day1.SftpUpload;
import Day1.SftpUtil;
import ExecutorTest.ExecutorDemo;
import ExecutorTest.ThreadPoolWork;
import QueueTest.ArrayBlockingQueueTest;
import SchedualedThreadPoolExecutorTest.ScheduledThreadPoolExecutorTest;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author dinghuai.liu
 * @description CAS机制缺陷以及ABA问题解决
 */
public class Main {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static final String FILE_PATH = "C:/Users/dinghuai.liu/Desktop/test.txt";

    public static void main(String[] args) {
        System.out.println("111");
//        Main main = new Main();
//        //ABA问题
//        main.cas1();
        //解决方案
        //main.cas2();
//        ArrayBlockingQueueTest arrayBlockingQueueTest = new ArrayBlockingQueueTest();
//        arrayBlockingQueueTest.startRun();

//        ExecutorDemo executorDemo = new ExecutorDemo();
//        executorDemo.demo1();
        //executorDemo.demo2();

//        ThreadPoolWork threadPoolWork = new ThreadPoolWork(2,
//                5,
//                1,
//                TimeUnit.SECONDS,
//                new ArrayBlockingQueue<Runnable>(6));
//        threadPoolWork.demo();

//        ScheduledThreadPoolExecutorTest scheduledThreadPoolExecutorTest = new ScheduledThreadPoolExecutorTest();
//        scheduledThreadPoolExecutorTest.demo();

        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cas2() {
        AtomicBoolean result = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //小陈充值60第一次点击
            atomicStampedReference.compareAndSet(100, 40, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            //收到转账60
            atomicStampedReference.compareAndSet(40, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        }, "线程1").start();

        new Thread(() -> {
            try {
                //网络卡顿3秒
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //小陈充值60第二次点击
            atomicStampedReference.compareAndSet(100, 40, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        }, "线程2").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("修改成功否:" + result);
        System.out.println("当前实际最新值:" + atomicReference.get());
    }

    public void cas1() {
        AtomicBoolean result = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //小陈充值60第一次点击
            atomicReference.compareAndSet(100, 40);
            //收到转账60
            atomicReference.compareAndSet(40, 100);
        }, "线程1").start();

        new Thread(() -> {
            try {
                //网络卡顿3秒
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //小陈充值60第二次点击
            result.set(atomicReference.compareAndSet(100, 40));
        }, "线程2").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("修改成功否:" + result);
        System.out.println("当前实际最新值:" + atomicReference.get());
    }


    /**
     * 读取文件信息并打印至控制台
     */
    public static void readFile() throws IOException {
        BufferedReader in = null;
        try {
            File file = new File(FILE_PATH);
            //uploadFile(file);
            //打开输入流
            in = new BufferedReader(new FileReader(file));
            String s;
            //读字符串
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭缓冲读入流及文件读入流的连接
            in.close();
            try {
                SftpTime.whileFunction();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 上传文件至服务器
     *
     * @param file
     */
    public static void uploadFile(File file) {
        SftpUtil.uploadFile(file);
    }
}