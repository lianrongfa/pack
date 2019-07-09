package cn.lianrf.utils.poi;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ColumnName {

    String value();

}
