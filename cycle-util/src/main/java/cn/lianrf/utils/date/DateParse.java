package cn.lianrf.utils.date;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期类识别 不包括小时
 * @version: v1.0
 * @date: 2019/4/10
 * @author: lianrf
 */

public class DateParse {

    private static DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void setOutFormatter(DateTimeFormatter outFormatter) {
        this.outFormatter = outFormatter;
    }

    public DateParse() {
    }

    public DateParse(DateTimeFormatter outFormatter) {
        this.outFormatter = outFormatter;
    }

    private static Map<Pattern, DateTimeFormatter> regexs = new HashMap<>();

    static {
        //eg:October 25, 2011
        regexs.put(Pattern.compile("^([a-z|A-Z]{2,})(\\s{1})([0-9]{1,2},)(\\s{1})([1-2][\\d]{3})$"),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH));
        //eg:Monday, December 10, 2012
        regexs.put(Pattern.compile("^([a-z|A-Z]{2,},\\s)([a-z|A-Z]{2,})(\\s{1})([0-9]{1,2},)(\\s{1})([1-2][\\d]{3})$"),
                DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH));
        //eg:Monday December 10, 2012
        regexs.put(Pattern.compile("^([a-z|A-Z]{2,}\\s)([a-z|A-Z]{2,})(\\s{1})([0-9]{1,2},)(\\s{1})([1-2][\\d]{3})$"),
                DateTimeFormatter.ofPattern("EEEE MMMM dd, yyyy", Locale.ENGLISH));
        //eg:20180608
        regexs.put(Pattern.compile("^[1-2][\\d]{3}[0|1][0-9][0-3][0-9]$"),
                DateTimeFormatter.ofPattern("yyyyMMdd"));
        //eg:2019年09月27日
        regexs.put(Pattern.compile("^[1-2][\\d]{3}年[0|1][0-9]月[0-3][0-9]日$"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        //eg:2019年9月27日
        regexs.put(Pattern.compile("^[1-2][\\d]{3}年[1-9]?[1-2]?月[0-3][0-9]日$"),
                DateTimeFormatter.ofPattern("yyyy年M月dd日"));
    }



    /**
     * 字符串日期解析，返回日期格式
     *
     * @param dateStr 某种格式字符串日期
     * @return yyyy-MM-dd 日期
     */
    public static Integer parse(String dateStr){
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        dateStr = dateStr.trim().replaceAll("\\s{2,}", " ");
        if (dateStr.contains("on")) {
            String[] dateStrArray = dateStr.split("on ");
            if(dateStrArray.length==2){
                dateStr = dateStrArray[1];
            }
        }

        for (Map.Entry<Pattern, DateTimeFormatter> entry : regexs.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(dateStr);
            if (matcher.matches()) {
                DateTimeFormatter inFormatter = entry.getValue();
                LocalDate localDate = LocalDate.parse(dateStr, inFormatter);
                return Integer.parseInt(localDate.format(outFormatter));
            }
        }
        return null;
    }


    public static void main(String[] args){
        String s = "Monday December 10, 2012";
        System.out.println(DateParse.parse(s));

    }

}
