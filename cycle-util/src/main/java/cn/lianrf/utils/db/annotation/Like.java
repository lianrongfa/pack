package cn.lianrf.utils.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 用户模糊查询
 * @version: v1.0
 * @date: 2020/6/3
 * @author: lianrf
 */
@Documented
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Like {

    /**
     * 左边模糊
     * sql eg：xxx like '%天气'
     * mongo eg: .*天气
     */
    String LEFT="LEFT";
    /**
     * 右边模糊
     * sql eg：xxx like '天气%'
     * mongo eg: 天气.*
     */
    String RIGHT="RIGHT";
    /**
     * 同时模糊
     * sql eg：xxx like '%天气%'
     * mongo eg: .*天气.*
     */
    String BOTH="BOTH";

    String value() default BOTH;
}