package cn.lianrf.utils.File;

import com.sun.istack.internal.Nullable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @version: v1.0
 * @date: 2019/6/10
 * @author: lianrf
 */
public class FileDowload {

    /**
     * springboot文件下载
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static ResponseEntity<InputStreamResource> dowloadTemplate(String fileName,@Nullable String fileDir) throws IOException {
        ClassPathResource file = new ClassPathResource("template/"+fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
