package cn.lianrf.utils.reflect;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
public class FieldUtil {
    /**
     * 获取子类和所有超类的属性<br/>
     * 过滤掉子类和父类同名的属性，以子类为主
     * @param clazz
     * @param fields
     * @return
     */
    public static List<Field> getDeclaredField(Class clazz, List<Field> fields){
        List<Field> clazzFields = Lists.newArrayList(Arrays.copyOf(clazz.getDeclaredFields(), clazz.getDeclaredFields().length));
        //过滤掉子类和父类同名的属性，以子类为主
        for (Iterator<Field> it = clazzFields.iterator(); it.hasNext();) {
            Field clazzField = it.next();
            for(int i=0; i<fields.size(); i++) {
                if (fields.get(i).getName().equals(clazzField.getName())){
                    it.remove();
                }
            }
        }
        fields.addAll(clazzFields);
        if(clazz.getSuperclass() != null){
            getDeclaredField(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    /***
     * 根据属性获取对象属性值，通过get方法
     * @param fieldName
     * @param target
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object target) {
        try {
            String e = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + e + fieldName.substring(1);
            Method method = target.getClass().getMethod(getter);
            return method.invoke(target);
        } catch (Exception var6) {
            return null;
        }
    }
}
