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
     * @param map 映射 key:原Bean字段；value：目标Bean字段
     */
    public static void map(Object sourceBean, Object targetBean, Map<String,String> map) throws NoSuchFieldException, IllegalAccessException {
        Class<?> sourceClass = sourceBean.getClass();
        Class<?> targetClass = targetBean.getClass();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Field sourceField = sourceClass.getDeclaredField(key);
            sourceField.setAccessible(true);
            Field targetField = targetClass.getDeclaredField(value);
            targetField.setAccessible(true);

            Class<?> sourceFieldType = sourceField.getType();
            Class<?> targetFieldType = targetField.getType();

            if(sourceFieldType.equals(targetFieldType)){
                targetField.set(targetBean,sourceField.get(sourceBean));
            }
        }
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
