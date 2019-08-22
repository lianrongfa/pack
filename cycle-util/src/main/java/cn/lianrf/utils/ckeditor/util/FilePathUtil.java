package cn.lianrf.utils.ckeditor.util;

import com.drcnet.response.exception.InvalidException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FilePathUtil {

    /**
     * 文件路径分配
     * @param file   文件
     * @param dirPrefix 文件夹绝对路径前缀
     * @param folder 存储的文件夹
     * @param hashNum 文件夹个数
     * @return 返回值map attachment：加上时间戳的文件名  attachmentName:原文件名
     */
    public static Map<String, Object> fileManage(MultipartFile file, String dirPrefix, String folder, int hashNum) {
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName.contains(File.separator)) {
                originalFileName = originalFileName.substring(originalFileName.lastIndexOf(File.separator) + 1, originalFileName.length());
            }
            String fileName = System.currentTimeMillis() + originalFileName;
            String filePath = buildPath(dirPrefix,fileName, folder,hashNum);
            File newFile = new File(filePath.toString());
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            filePath = filePath + fileName;
            if (!FileUtil.uploadFile(filePath, file)) {
                throw new RuntimeException("上传文件失败");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("attachment", fileName);
            map.put("attachmentName", originalFileName);
            return map;
        }
        return null;
    }



    /**
     * 文件夹绝对路基构建
     * @param dirPrefix 文件夹绝度路径
     * @param fileName 文件名
     * @param folder   存储的文件夹
     * @param hashNum 文件夹个数
     * @return
     */
    public static String buildPath(String dirPrefix,String fileName, String folder,int hashNum) {
        if (StringUtils.isEmpty(fileName)) {
            throw new InvalidException("文件名不能为空!");
        }
        long bucket = fileName.hashCode() % hashNum;
        StringBuilder filePath = new StringBuilder();
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            filePath.append("C:\\soft")
                    .append(File.separator).append(folder)
                    .append(File.separator).append(bucket).append(File.separator);

        } else {
            filePath.append(dirPrefix).append(folder)
                    .append(File.separator).append(bucket).append(File.separator);
        }

        File file = new File(filePath.toString());
        if(!file.exists()){
            file.mkdirs();
        }
        return filePath.toString();
    }

    /**
     * 构建图片访问路径
     * @param urlSuffix url访问前缀
     * @param imgName 图片名
     * @param token
     * @param address 访问地址
     * @return
     */
    public static String buildImgUrl(String urlSuffix,String imgName,String token,String address){
        StringBuilder sb;
        if(address.contains("http")){
            sb = new StringBuilder();
        }else{
            sb=new StringBuilder("http://");
        }
        sb.append(address);
        sb.append(urlSuffix);
        sb.append(imgName);
        sb.append("&token="+token);
        return sb.toString();
    }

    public static boolean isImage(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(inputStream);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static void createPath(String ... paths){
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
        }
    }

    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

}
