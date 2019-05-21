package cn.lianrf.utils.bean;

import org.apache.commons.lang.RandomStringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        String s = "odssOaeidlcoeodmnpariauefeedr kds eoesrls zr srr,r cno  echfnfp2eos sorpoOr if saoineisni e Erioh is";
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
}
