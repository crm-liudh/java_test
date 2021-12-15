package Day1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


import com.jcraft.jsch.*;


/**
 * 文件上传工具类
 * @author dinghuai.liu
 */
public class SftpUtil {

    public static final String SFTP_IP_HOST = "172.17.10.248";
    public static final Integer SFTP_IP_PORT = 22;
    public static final String SFTP_IP_USER = "root";
    public static final String SFTP_IP_PASSWORD = "123456";
    public static final String SFTP_DIRECTORY_PATH = "/usr/local";

    /**
     * spring文件上传方法
     */
    public static String uploadFile(File file) {
        Session session = null;
        ChannelSftp sftp = null;
        try {
            InputStream inputStream = new FileInputStream(file);

            //生成文件名
            Date uploadTime = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = df.format(uploadTime) + "_" + new Random().nextInt(1000) + ".txt";
            fileName = file.getName();

            SftpUpload mySftp = new SftpUpload();
            sftp = mySftp.connect(SFTP_IP_HOST, SFTP_IP_PORT, SFTP_IP_USER, SFTP_IP_PASSWORD);
            session = sftp.getSession();
            SftpUtil.createDir(SFTP_DIRECTORY_PATH, sftp);
            sftp.cd(SFTP_DIRECTORY_PATH);
            sftp.put(inputStream, fileName);
            sftp.disconnect();
            sftp.getSession().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sftp != null) {
                    sftp.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (session != null) {
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            String reName = SFTP_DIRECTORY_PATH;
            return reName;
        }
    }

    /**
     * 创建目录
     *
     * @throws Exception
     */
    public static void createDir(String createpath, ChannelSftp sftp) throws Exception {
        try {
            if (isDirExist(sftp, createpath)) {
                sftp.cd(createpath);
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(sftp, filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 判断目录是否存在
     */
    public static boolean isDirExist(ChannelSftp sftp, String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 读取远程服务器文件
     */
    public static void readRemoteFile() {
        Session session = null;
        ChannelSftp sftp = null;
        SftpUpload mySftp = new SftpUpload();
        try {
            sftp = mySftp.connect(SFTP_IP_HOST, SFTP_IP_PORT, SFTP_IP_USER, SFTP_IP_PASSWORD);
            session = sftp.getSession();

            sftp.disconnect();
            sftp.getSession().disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
