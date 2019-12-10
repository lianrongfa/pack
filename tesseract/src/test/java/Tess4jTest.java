import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

/**
 * 图像识别
 * @version: v1.0
 * @date: 2019/12/9
 * @author: lianrf
 */
public class Tess4jTest {


    public static void main(String[] args) {

        File file = new File("D:\\Java\\Tess4J\\1234.png");
        ITesseract instance = new Tesseract();

        /**
         *  获取项目根路径，例如： D:\IDEAWorkSpace\tess4J
         */
        File directory = new File("C:\\Program Files\\Tesseract-OCR");
        String courseFile = null;
        try {
            courseFile = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置训练库的位置
        instance.setDatapath(courseFile + "\\tessdata");

        //chi_sim ：简体中文， eng	根据需求选择语言库
        //instance.setLanguage("eng");

        String result = null;
        try {
            long startTime = System.currentTimeMillis();
            result =  instance.doOCR(file);
            long endTime = System.currentTimeMillis();
            System.out.println("Time is：" + (endTime - startTime) + " 毫秒");
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        System.out.println("result: ");
        System.out.println(result);
    }
}
