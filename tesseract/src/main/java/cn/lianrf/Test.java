package cn.lianrf;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * @version: v1.0
 * @date: 2019/12/10
 * @author: lianrf
 */
public class Test {
    private static final String DIR="tessdata";


    public static void main(String[] args) {
        String tessdata = null;
        try {
            tessdata = Paths.get(Test.class.getClassLoader().getResource(DIR).toURI()).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdata);
        tesseract.setLanguage("chi_sim+eng");
        try {
            String s = tesseract.doOCR(new File("C:\\Users\\86180\\Desktop\\123.png"));
            System.out.println(s);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
