package cn.lianrf.utils.db.mongo;


import cn.lianrf.utils.db.annotation.Operator;
import cn.lianrf.utils.db.base.BaseCriteria;
import cn.lianrf.utils.db.example.UserDto;
import cn.lianrf.utils.reflect.FieldUtil;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
public class BuilderMongoCriteria extends BaseCriteria {
    /**
     * @param t
     * @param <T>
     */
    private <T> BuilderMongoCriteria(T t) {
        super(t);
    }

    /**
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> BuilderMongoCriteria of(T t) {
        return new BuilderMongoCriteria(t);
    }

    public Criteria createCriteria() {
        Criteria criteria = new Criteria();
        Class clazz = getClazz();
        List<Field> fields = getFields();
        //用来标记field
        HashSet<Field> mark = new HashSet<>();
        for (Field field : fields) {
            Operator operator = field.getDeclaredAnnotation(Operator.class);
            if(mark.contains(field)){continue;}
            if (operator != null) {
                String operaName = operator.value();
                if ("".equals(operaName)) {continue;}
                //处理字段名
                String fieldName = null;
                Column column = field.getDeclaredAnnotation(Column.class);
                if (column != null) {
                    fieldName = column.name();
                    List<Field> list = FieldUtil.getFieldByColumn(column.name(), clazz, getFields());
                    //size==2 说明查询为范围查询，因为mongo的范围查询需要单独处理
                    if(list.size()==2){
                        List<Field> tempList = list.stream().filter(item -> !item.equals(field)).collect(Collectors.toList());
                        Field temp = tempList.get(0);
                        mark.add(temp);
                        Operator tempOperator = temp.getDeclaredAnnotation(Operator.class);
                        Objects.requireNonNull(tempOperator,"相同Column的Operator不能为空");

                    }else if (list.size()>2){
                        throw new RuntimeException("如有多个相同名称column，请用数组或集合");
                    }
                } else {
                    fieldName = fieldNaming.getFieldName(field.getName());
                }
                Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());
            }

        }
        return criteria;
    }


    public static void main(String[] args) throws IllegalAccessException {
        BuilderMongoCriteria.of(new UserDto()).createCriteria();

        UserDto userDto = new UserDto();
        Object testName = FieldUtil.getFieldValueByName("testName", userDto);
        System.out.println(testName);


    }


}
