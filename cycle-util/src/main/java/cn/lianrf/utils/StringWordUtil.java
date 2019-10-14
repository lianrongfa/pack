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

    /**
     * 中文数字转化阿拉伯数字
     * @param chineseNumber 二十一
     * @return 21
     */
    public static int chineseNumber2Int(String chineseNumber){
        int result = 0;
        int temp = 1;
        //存放一个单位的数字如：十万
        int count = 0;
        //判断是否有chArr
        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;
            //判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {
                //非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){
                        //添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if(b){
                //单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {
                //遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }
}
