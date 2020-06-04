package cn.lianrf.utils.bean;

import org.apache.commons.lang.RandomStringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 *
 * Bean与Bean的映射处理
 * @version: v1.0
 * @date: 2019/4/19
 * @author: lianrf
 */
public abstract class BeanMap {

    /**
     *
     * @param sourceBean 原Bean
     * @param targetBean 目标Bean
     * @param maps 映射 key:原Bean字段；value：目标Bean字段
     */
    public static void map(Object sourceBean, Object targetBean, Map<String,String> maps) throws IllegalAccessException {
        Class<?> sourceClass = sourceBean.getClass();
        Class<?> targetClass = targetBean.getClass();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Field sourceField = getFieldRecursion(sourceClass,key);
            if(sourceField==null){
                continue;
            }
            sourceField.setAccessible(true);

            Field targetField= getFieldRecursion(targetClass,value);
            if(targetField==null){
                continue;
            }
            targetField.setAccessible(true);

            Class<?> sourceFieldType = sourceField.getType();
            Class<?> targetFieldType = targetField.getType();

            if(sourceFieldType.equals(targetFieldType)){
                targetField.set(targetBean,sourceField.get(sourceBean));
            }
            if(sourceFieldType.isAssignableFrom(List.class)
                    &&targetFieldType.isAssignableFrom(String.class)){
                List list = (List) sourceField.get(sourceBean);
                if(list!=null&&list.size()>0){
                    targetField.set(targetBean,list.get(0));
                }
            }
        }
    }

    /**
     * 递归获得字段
     * @param clzz
     * @param filedName
     * @return
     */
    private static Field getFieldRecursion(Class clzz,String filedName){
        try {
            Field declaredField = clzz.getDeclaredField(filedName);
            return declaredField;
        } catch (NoSuchFieldException e) {
            Class superclass = clzz.getSuperclass();
            if(superclass!=null){
                return getFieldRecursion(superclass,filedName);
            }
        }
        return null;
    }

    /**
     * 给Bean字段填充值
     * @param source
     */
    public static void randomBuild(Object source) {
        String s = "中国纺织大跟我结婚我感觉我翻我牌假发票为佛教海明威明和破解后玩很久口味哦评价和贫困批发网";
        RandomStringUtils.randomAlphanumeric(50);
        Class<?> aClass = source.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (String.class.equals(type)) {
                try {
                    field.set(source, RandomStringUtils.random(50, s));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (Integer.class.equals(type)) {
                try {
                    field.set(source, new Random().nextInt(50));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
            if (type.isAssignableFrom(List.class)) {
                ParameterizedTypeImpl genericType = (ParameterizedTypeImpl) field.getGenericType();
                Type[] args = genericType.getActualTypeArguments();
                if (args != null) {
                    Class arg = (Class) args[0];
                    if (String.class.equals(arg)) {
                        try {
                            field.set(source, Arrays.asList(RandomStringUtils.random(50, s)));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Object o1 = arg.newInstance();
                            Object o2 = arg.newInstance();
                            randomBuild(o1);
                            randomBuild(o2);
                            field.set(source, Arrays.asList(o1, o2));
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    /**
     * Map转成实体对象
     *
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String filedTypeName = field.getType().getName();
                if (filedTypeName.equalsIgnoreCase("java.util.date")) {
                    String datetimestamp = String.valueOf(map.get(field.getName()));
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, new Date(Long.parseLong(datetimestamp)));
                    }
                } else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
