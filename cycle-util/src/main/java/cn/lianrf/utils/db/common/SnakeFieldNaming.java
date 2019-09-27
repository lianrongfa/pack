package cn.lianrf.utils.db.common;


/**
 * @version: v1.0
 * @date: 2019/9/24
 * @author: lianrf
 */
public class SnakeFieldNaming implements FieldNaming{
    private final static String UNDERLINE = "_";
    /**
     * 将nameTest 转换为name_test
     * @param fieldName 目标字段名称 nameTest
     * @return name_test
     */
    @Override
    public String getFieldName(String fieldName) {
        StringBuilder sb = new StringBuilder(fieldName);
        int temp = 0;//定位
        if (!fieldName.contains(UNDERLINE)) {
            for (int i = 0; i < fieldName.length(); i++) {
                if (Character.isUpperCase(fieldName.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }
}
