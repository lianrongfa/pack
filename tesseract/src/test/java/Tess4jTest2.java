import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;
import java.io.IOException;

/**
 * 图像识别
 * @version: v1.0
 * @date: 2019/12/9
 * @author: lianrf
 */
public class Tess4jTest2 {


    public static void main(String[] args) {

        // System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/win32-x86" : "lib/win32-x86-64");

        File imageFile = new File("eurotext.tif");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//        File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
//         instance.setDatapath(tessDataFolder.getPath());

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
