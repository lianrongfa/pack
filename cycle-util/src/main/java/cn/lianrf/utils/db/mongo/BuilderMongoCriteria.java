package cn.lianrf.utils.db.mongo;


import cn.lianrf.utils.db.annotation.Operator;
import cn.lianrf.utils.db.base.BaseCriteria;
import cn.lianrf.utils.db.example.UserDto;
import cn.lianrf.utils.reflect.FieldUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
public class BuilderMongoCriteria extends BaseCriteria {

    /**
     * 缓存范围查找操作符
     */
    private static List<String> list = Arrays.asList(Operator.GT, Operator.GTE, Operator.LT, Operator.LTE);


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

    public Criteria build() {
        Criteria criteria = new Criteria();
        Class clazz = getClazz();
        List<Field> fields = getFields();
        //用来标记field是否需要处理，在此集合中的不需要再次处理了
        HashSet<Field> mark = new HashSet<>();
        for (Field field : fields) {
            //排查非映射字段
            if (isTransient(field)) {continue;}

            Operator operator = field.getDeclaredAnnotation(Operator.class);
            if (mark.contains(field)) {
                continue;
            }
            //不为空时走设定好的查询条件
            if (operator != null) {
                notNullOperator(criteria, clazz, mark, field, operator);
            } else {//为空时统一走 = 条件
                nullOperator(criteria, field);
            }

        }
        return criteria;
    }



    /**
     * 处理Operator为空时
     * @param criteria criteria
     * @param field 字段
     */
    private void nullOperator(Criteria criteria, Field field) {
        String fieldName = getFieldName(field);
        Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());
        criteria.and(fieldName).is(value);
    }



    /**
     * 处理Operator不为空时
     * @param criteria criteria
     * @param clazz class
     * @param mark 标示集合
     * @param field 字段
     * @param operator operator
     */
    private void notNullOperator(Criteria criteria, Class clazz, HashSet<Field> mark, Field field, Operator operator) {
        String operaName = operator.value();
        if ("".equals(operaName)) {
            return;
        }
        Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());
        if(nullValue(operator,value)){return;}
        //处理字段名
        String fieldName = null;
        Column column = field.getDeclaredAnnotation(Column.class);
        if (column != null) {
            fieldName = column.name();
            List<Field> list = FieldUtil.getFieldByColumn(column.name(), clazz, getFields());
            //size==2 说明查询为范围查询，因为mongo的范围查询需要单独处理
            if(list.size()==2){
                List<Field> tempList = list.stream().filter(item -> !item.equals(field)).collect(Collectors.toList());
                Field tempField = tempList.get(0);
                mark.add(tempField);
                Operator tempOperator = tempField.getDeclaredAnnotation(Operator.class);
                Objects.requireNonNull(tempOperator,"相同Column的Operator不能为空");
                rangeCriteria(criteria,getTarget(),fieldName,field,operator,tempField,tempOperator);
                return;
            }else if (list.size()>2){
                throw new RuntimeException("如有多个相同名称column，请用数组或集合");
            }
        } else {
            fieldName = fieldNaming.getFieldName(field.getName());
        }
        OperatorCriteria.get(operaName).operator(criteria,fieldName,value);
    }

    /**
     * 处理范围查询
     * @param criteria criteria
     * @param target 目标实例dto
     * @param fieldName 字段名(db中字段名)
     * @param field 类字段
     * @param operator 操作符
     * @param tempField 类字段
     * @param tempOperator 操作符
     */
    private void rangeCriteria(Criteria criteria, Object target, String fieldName,
                               Field field, Operator operator, Field tempField, Operator tempOperator) {
        String operator1 = operator.value();
        String operator2 = tempOperator.value();
        if (!list.contains(operator1) || !list.contains(operator2)) {
            throw new RuntimeException("不支持的操作符");
        }

        Object value1 = FieldUtil.getFieldValueByName(field.getName(), target);
        Object value2 = FieldUtil.getFieldValueByName(tempField.getName(), target);
        if (!Objects.isNull(value1) && !Objects.isNull(value2)) {
            Criteria tempCriteria = OperatorCriteria.get(operator1).operator(criteria, fieldName, value1);
            OperatorCriteria.get(operator2).neatOperator(tempCriteria, value2);
        } else if (!Objects.isNull(value1)) {
            OperatorCriteria.get(operator1).operator(criteria, fieldName, value1);
        } else if (!Objects.isNull(value2)) {
            OperatorCriteria.get(operator2).operator(criteria, fieldName, value2);
        }

    }


    public static void main(String[] args) throws IllegalAccessException {
        Criteria criteria = BuilderMongoCriteria.of(new UserDto()).build();

        Query query = new Query(criteria);

        Criteria and = criteria.and("");

        System.out.println(query);



    }


}
