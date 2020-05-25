package cn.lianrf.utils.date;


import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DateParse {

    private static DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void setOutFormatter(DateTimeFormatter outFormatter) {
        DateParse.outFormatter = outFormatter;
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
        regexs.put(Pattern.compile("^[1-2][\\d]{3}[01][0-9][0-3][0-9]$"),
                DateTimeFormatter.ofPattern("yyyyMMdd"));
        //eg:2019年09月27日2018年11月5日 2018年11月9日
        regexs.put(Pattern.compile("^[1-2][\\d]{3}年[01][0-9]月([0-3][0-9]|[1-9])日$"),
                DateTimeFormatter.ofPattern("yyyy年M月d日"));
        //eg:2019年9月27日 2019年5月13日
        regexs.put(Pattern.compile("^[1-2][\\d]{3}年[1-9]?[1-2]?月([0-3][0-9]|[1-9])日$"),
                DateTimeFormatter.ofPattern("yyyy年M月d日"));
        //eg:2019年9月7日
        regexs.put(Pattern.compile("^([12]){1}[\\d]{3}-(1){0,1}[0-9]{1}-([1-3]){0,1}[0-9]{1}$"),
                DateTimeFormatter.ofPattern("yyyy-M-d"));
        //eg:2019-09-27
        regexs.put(Pattern.compile("^([12]){1}[\\d]{3}-([01]){1}[0-9]{1}-[0-3]{1}[0-9]{1}$"),
                DateTimeFormatter.ofPattern("yyyy-M-d"));
        //eg:2019-9-27
        regexs.put(Pattern.compile("^([12]){1}[\\d]{3}-(1){0,1}[0-9]{1}-([1-3]){0,1}[0-9]{1}$"),
                DateTimeFormatter.ofPattern("yyyy-M-d"));
        //eg:2019/03/15
        regexs.put(Pattern.compile("^[1-2][\\d]{3}/[0|1][0-9]/([0-3][0-9]|[1-9])$"),
                DateTimeFormatter.ofPattern("yyyy/M/d"));
    }



    /**
     *
     *
     * @param dateStr 某种格式字符串日期
     * @return yyyyMMdd 默认
     */
    public static Integer parseInt(String dateStr){
        LocalDate localDate = doParse(dateStr);
        if(localDate==null){
            return null;
        }
        return Integer.parseInt(localDate.format(outFormatter));
    }

    /**
     * 字符串日期解析，返回日期格式 LocalDate
     * @param dateStr 某种格式字符串日期
     * @return LocalDate
     */
    private static LocalDate doParse(String dateStr) {
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
        boolean b=true;
        for (Map.Entry<Pattern, DateTimeFormatter> entry : regexs.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(dateStr);
            if (matcher.matches()) {
                b=false;
                DateTimeFormatter inFormatter = entry.getValue();
                try{
                    System.out.println(entry.getKey());
                    LocalDate parse = LocalDate.parse(dateStr, inFormatter);
                    return parse;
                } catch (Exception e){
                    log.error("DateParsec错误",e);
                }
            }
        }
        if(b){
            log.warn("DateParse警告,数据:{}未匹配到正则，请更新正则库",dateStr);
        }
        return null;
    }


    public static void main(String[] args){
        String s = "2015年4月2日";
        System.out.println(DateParse.parseInt(s));

    }

}
