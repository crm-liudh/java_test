package Day1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * @author dinghuai.liu
 */
public class SftpTime {

    private static Timer timer = new Timer(true);
    public static boolean timeFlag = true;
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 定时器
     */
    public static void timeListener() {
        SftpTask sftpTask = new SftpTask();
        try {
            timer.schedule(sftpTask, SDF.parse("2021-12-14 11:47:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void stopTimer() {
        timer.cancel();
        timeFlag = false;
        System.out.println("TIMER STOPED");
    }

    public static void whileFunction(){
        while (timeFlag) {
            timeListener();
        }
    }
}
