package cn.lianrf.utils.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于sql，指明dto映射的为哪个实体类class
 * @version: v1.0
 * @date: 2019/12/31
 * @author: lianrf
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityClass {
    Class value();
}
