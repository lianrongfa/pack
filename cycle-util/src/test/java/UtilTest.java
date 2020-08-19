import com.sun.tools.attach.*;
import sun.misc.Unsafe;

import java.io.IOException;
import java.util.List;

/**
 * @version: v1.0
 * @date: 2020/5/12
 * @author: lianrf
 */
public class UtilTest {

    private String ss="1223fwe";

    public static void main(String[] args) throws NoSuchFieldException {
        Unsafe unsafe = Unsafe.getUnsafe();

        long offset = unsafe.objectFieldOffset(UtilTest.class.getDeclaredField("ss"));

        UtilTest test = new UtilTest();

        test.test(unsafe,offset);


    }

    public void test(Unsafe unsafe,long offset){
        System.out.println("offset:"+offset);
        System.out.println("op:"+unsafe.compareAndSwapObject(this,offset,"1223fwe","yeyeyey"));
        System.out.println("value:"+this.ss);
    }
}
