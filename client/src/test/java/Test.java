import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @version: v1.0
 * @date: 2019/4/10
 * @author: lianrf
 */
public class Test {

    public Test(String s) {

    }

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT20"));
        System.out.println(format.format(date));

        format.setTimeZone(TimeZone.getTimeZone("Yemen"));
        System.out.println(format.format(date));

        format.setTimeZone(TimeZone.getTimeZone("UTC−10:00"));
        System.out.println(format.format(date));

        format.setTimeZone(TimeZone.getDefault());
        System.out.println(format.format(date));

    }

    private String s;



    public String mm(String s){
        return null;
    }

    private static void test2(){

    }
}
