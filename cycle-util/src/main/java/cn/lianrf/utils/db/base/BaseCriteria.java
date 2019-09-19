package cn.lianrf.utils.db.base;

import cn.lianrf.utils.reflect.FieldUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
@Data
public abstract class BaseCriteria {
    private static final Map<Class, List<Field>> fieldCache=new ConcurrentHashMap<>(32);
    private Class clazz;
    private List<Field> fields;

    public <T> BaseCriteria(T t) {
        if(t==null){
            throw new RuntimeException("构造参数不能为空");
        }
        Class<?> aClass = t.getClass();
        this.clazz=aClass;
        List<Field> fields = fieldCache.get(aClass);
        if(fields==null){
            fields = new ArrayList<>();
            FieldUtil.getDeclaredField(aClass, fields);
            fieldCache.putIfAbsent(aClass,fields);
        }
        this.fields=fields;
    }
}
