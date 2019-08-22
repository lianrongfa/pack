package cn.lianrf.utils.ckeditor.util;

import com.google.common.base.Splitter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ml on 2018/11/21.
 */
public class FileUtil {
    private FileUtil() {

    }
    //文件上传工具类服务方法
    public static boolean uploadFile(String filePath, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取指定url中的某个参数
     * @param url
     * @param name
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(name);
    }

    /**
     *远程文件下载
     * @param remoteFilePath 远程文件地址
     * @return 本地生成文件名
     */
    public static String downloadFile(String remoteFilePath) {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String localFile = null;
        try {
            urlfile = new URL(remoteFilePath);
            String file = urlfile.getFile();

            localFile = UUID.randomUUID().toString().replace("-", "") +
                    file.substring(file.lastIndexOf("."));

            String localFilePath = FilePathUtil.buildPath("",localFile, "",100) + localFile;

            File f = new File(localFilePath);

            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());

            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            System.out.println("上传成功");
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localFile;
    }
}
