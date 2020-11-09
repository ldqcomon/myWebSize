package com.it.sf.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @Auther: ldq
 * @Date: 2020/9/29
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class FileUtils {
    private static String ip = "106.53.115.110";
    private static String port = "21";
    private static String user = "ftpuser";
    private static String pass = "itcast123";
    private static FTPClient ftpClient;

    public static String upload(String type, MultipartFile files) {
        String fileName = "";
        String fileUrl = "";
        try {
            //生成文件唯一名称
            String originalFilename = files.getOriginalFilename();
            String pictureType = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = System.currentTimeMillis() + pictureType;
            if ("1".equals(type)) {
                // /home/ftpuser/ 默认到此目录下
                fileUrl = "/pictures/";
            }
            else {
                fileUrl = "/video/";
            }
            if (connect()) {
                //切换工作目录
                boolean b = ftpClient.changeWorkingDirectory(fileUrl);
                ftpClient.setBufferSize(2048);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setConnectTimeout(1000);
                // 设置文件的类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //被动模式
               // ftpClient.setRemoteVerificationEnabled(false);
                ftpClient.enterLocalPassiveMode();
                ftpClient.storeFile(fileName, files.getInputStream());
                ftpClient.disconnect();
            };
        } catch (IOException e) {
            log.error("upload the file error:{}", e.getMessage());
        }
        // 返回文件路径,如:/video/qiurisiyu.mp3
        return fileUrl+fileName;
    }

    // 连接到Linux ftp服务器
    public static boolean connect() {
            boolean isSuccess = false;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ip, Integer.parseInt(port));
            isSuccess = ftpClient.login(user, pass);
        } catch (IOException e) {
            log.error("connect linux ftp error:{}",e.getMessage());
        }
        return isSuccess;
    }

}
