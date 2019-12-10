package cn.lianrf.utils.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dto查询操作注解 用于查询时拼接构建查询语句
 *
 * @version: v1.0
 * @date: 2019/9/17
 * @author: lianrf
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Operator {
    String EQUALS = "=";
    String NO_EQUALS = "!=";
    String LT = "<";
    String GT = ">";
    String LTE = "<=";
    String GTE = ">=";
    String IN = "IN";
    String NOT_IN = "NOT IN";

    /**
     * 操作符条件
     *
     * @return
     */
    String value() default EQUALS;

    /**
     * (注意：mysql中的null值为特殊类型，不能用与值比较,比如：= != ，需要用is null 、is not null)
     * 当值为空时，是否需要做操作
     * true：需要
     * false：不需要
     * {@link #value()}=={@link #EQUALS} 表示 is null
     * {@link #value()}=={@link #NO_EQUALS} 表示 is not null
     *
     * @return true|false
     */
    boolean nullValue() default false;


}
