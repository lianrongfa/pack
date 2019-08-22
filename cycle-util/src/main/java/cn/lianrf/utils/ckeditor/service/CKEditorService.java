package cn.lianrf.utils.ckeditor.service;



import cn.lianrf.utils.ckeditor.util.FilePathUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *使用示例
 *
 *
 *    @ApiOperation("富文本图片上传")
 *     @PostMapping(value = "/upload")
 *     public Map ckeditorUpload(HttpServletRequest request) {
 *         String address=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
 *         Map resultMap = ckEditorService.ckeditorUpload(address,null,request);
 *         return resultMap;
 *     }
 *
 *    @ApiOperation("上传成功后查看图片")
 *     @GetMapping(value = "/ckeditor/view")
 *     public void ckeditorViewForEditor(@RequestParam(value = "imgName") String imgName ,
 *                                       HttpServletResponse response) {
 *         ckEditorService.ckeditorView(response, imgName);
 *     }
 *
 */
@Slf4j
@Data
public class CKEditorService {

    private String folder ="ckeditor";
    private String urlSuffix="/ckeditor/view?imgName=";
    private String dirPrefix = "/data/CKUtil/file/";
    private int hashNum = 100;
    /**
     * ckEditor图片上传
     * @param address  服务器访问地址 域名或者ip(包含端口)
     * @param token 用户token
     * @param request request
     * @return map里面内容为 uploaded：true url:访问路径
     */
    public Map ckeditorUpload(String address,String token, HttpServletRequest request) {
        MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = fileRequest.getFileMap();
        if (fileMap != null) {
            for (Map.Entry<String, MultipartFile> fileEntry : fileMap.entrySet()) {
                MultipartFile file = fileEntry.getValue();
                return ckeditorUpload(file,token,address);
            }
        }
        return errorMap();
    }

    /**
     * ckEditor图片上传
     * @param file  文件
     * @param token 用户token
     * @param address 服务器访问地址 域名或者ip(包含端口)
     * @return
     */
    public Map ckeditorUpload(MultipartFile file, String token, String address) {
        Map<String, Object> map= FilePathUtil.fileManage(file,dirPrefix, folder,hashNum);
        String sb = FilePathUtil.buildImgUrl(urlSuffix,String.valueOf(map.get("attachment")), token, address);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("uploaded",true);
        resultMap.put("url",sb);
        return resultMap;
    }


    /**
     * 图片查看
     * @param httpResponse
     * @param imgName
     */
    public void ckeditorView(HttpServletResponse httpResponse, String imgName) {
        String filePath = FilePathUtil.buildPath(this.dirPrefix,imgName, folder, hashNum)+imgName;
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            httpResponse.setContentType("image/gif");
            OutputStream out = httpResponse.getOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];

            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            log.error("图片获取错误！",e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static Map errorMap(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uploaded",false);
        return map;
    }
}
