package cn.lianrf.utils.reflect;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
public class FieldUtil {
    /**
     * 获取子类和所有超类的属性<br/>
     * 过滤掉子类和父类同名的属性，以子类为主
     *
     * @param clazz  目标class
     * @param fields new ArrayList 即可
     * @return list
     */
    public static List<Field> getDeclaredField(Class clazz, List<Field> fields) {
        List<Field> clazzFields = Lists.newArrayList(Arrays.copyOf(clazz.getDeclaredFields(), clazz.getDeclaredFields().length));
        //过滤掉子类和父类同名的属性，以子类为主
        for (Iterator<Field> it = clazzFields.iterator(); it.hasNext(); ) {
            Field clazzField = it.next();
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getName().equals(clazzField.getName())) {
                    it.remove();
                }
            }
        }
        fields.addAll(clazzFields);
        if (clazz.getSuperclass() != null) {
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

    /**
     * 获得所有相同column的field
     *
     * @param columnName {@link Column#name()}
     * @param clazz 目标对象class
     * @param fields 目标对象的field(应包括父类)，可以为空，该字段用于开发人员使用，响应速度，开发人员可自行增加缓存
     * @return 返回list 不是null
     */
    public static List<Field> getFieldByColumn(String columnName, Class clazz, @Nullable List<Field> fields) {
        if (columnName == null) {
            throw new RuntimeException("column不能为空");
        }

        if (fields == null || fields.isEmpty()) {
            fields = new ArrayList<Field>();
            getDeclaredField(clazz, fields);
        }
        List<Field> result = new ArrayList<>();

        for (Field field : fields) {
            Column temp = field.getDeclaredAnnotation(Column.class);
            if (temp == null) {
                continue;
            }
            if (columnName.equals(temp.name())) {
                result.add(field);
            }
        }
        return result;
    }
}
