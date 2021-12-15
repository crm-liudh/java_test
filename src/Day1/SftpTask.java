package Day1;

import com.sun.javaws.Main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

/**
 * @author dinghuai.liu
 */
public class SftpTask extends TimerTask {
    static Integer i = 0;

    @Override
    public void run(){
        System.out.println(i+1);
        i++;
        try {
            //SftpUtil.readRemoteFile();
            if(i.equals(10)){
                SftpTime.stopTimer();
            }
            sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
