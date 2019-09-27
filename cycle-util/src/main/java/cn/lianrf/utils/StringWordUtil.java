package cn.lianrf.utils;

/**
 * @version: v1.0
 * @date: 2019/6/12
 * @author: lianrf
 */
public class StringWordUtil {

    /**
     * 每个单词首字母大写
     * @param str 字符串 test name
     * @return 首字母大写 Test Name
     */
    public static String toUpperFirstCode(String str) {
        String[] strs = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String strTmp : strs) {
            char[] ch = strTmp.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            String strT = new String(ch);
            sb.append(strT).append(" ");
        }
        return sb.toString().trim();
    }
}
