package cn.lianrf.utils.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dto查询操作注解 用于查询时拼接构建查询语句
 * @version: v1.0
 * @date: 2019/9/17
 * @author: lianrf
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Operator {
    String EQUALS="=";
    String NO_EQUALS ="!=";
    String LT="<";
    String GT=">";
    String LTE="<=";
    String GTE=">=";
    String IN="IN";
    String NOT_IN="NOT IN";

    String value() default EQUALS;
}
