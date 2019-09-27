package cn.lianrf.utils.db.base;

import cn.lianrf.utils.db.common.FieldNaming;
import cn.lianrf.utils.db.common.SnakeFieldNaming;
import cn.lianrf.utils.reflect.FieldUtil;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

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
    private Object target;
    private Class clazz;
    private List<Field> fields;
    //字段命名规则转化器
    protected FieldNaming fieldNaming;

    public <T> BaseCriteria(T t) {
        if(t==null){
            throw new RuntimeException("构造参数不能为空");
        }
        Class<?> aClass = t.getClass();
        this.target=t;
        this.clazz=aClass;
        List<Field> fields = fieldCache.get(aClass);
        if(fields==null){
            fields = new ArrayList<>();
            FieldUtil.getDeclaredField(aClass, fields);
            fieldCache.putIfAbsent(aClass,fields);
        }
        this.fields=fields;
        this.fieldNaming=new SnakeFieldNaming();
    }
}
