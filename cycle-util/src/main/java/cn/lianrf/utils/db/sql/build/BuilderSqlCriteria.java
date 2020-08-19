package cn.lianrf.utils.db.sql.build;

import cn.lianrf.utils.db.annotation.EntityClass;
import cn.lianrf.utils.db.annotation.Like;
import cn.lianrf.utils.db.annotation.Operator;
import cn.lianrf.utils.db.base.BaseCriteria;
import cn.lianrf.utils.db.exception.AllocationException;
import cn.lianrf.utils.reflect.FieldUtil;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 *
 * 本功能基于tk-mybatis
 *
 * @version: v1.0
 * @date: 2019/12/13
 * @author: lianrf
 */
public class BuilderSqlCriteria extends BaseCriteria {
    private <T> BuilderSqlCriteria(T t) {
        super(t);
    }

    public static <T> BuilderSqlCriteria of(T t) {
        return new BuilderSqlCriteria(t);
    }

    /**
     * 使用默认Example构建
     *
     * @return
     */
    public Example build() {
        Class clazz = getClazz();
        EntityClass entityClass = (EntityClass) clazz.getDeclaredAnnotation(EntityClass.class);
        if (entityClass == null) {
            throw new AllocationException("请指定实体类class");
        }
        Example example = new Example(entityClass.value());
        return doBuild(clazz, example);
    }

    /**
     * 使用自定义Example构建
     *
     * @return
     */
    public Example build(Example example) {
        return doBuild(getClazz(), example);
    }

    private Example doBuild(Class clazz, Example example) {
        List<Field> fields = getFields();
        Example.Criteria criteria = example.createCriteria();
        for (Field field : fields) {
            //排查非映射字段
            if (isTransient(field)) {
                continue;
            }
            if (doLike(criteria, field)) {
                continue;
            }
            Operator operator = field.getDeclaredAnnotation(Operator.class);
            //不为空时走设定好的查询条件
            if (operator != null) {
                notNullOperator(criteria, clazz, field, operator);
            } else {//为空时统一走 = 条件
                nullOperator(criteria, field);
            }
        }

        return example;
    }

    private boolean doLike(Example.Criteria criteria, Field field) {
        Like like = field.getDeclaredAnnotation(Like.class);
        if (like != null) {
            Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());
            if (Objects.isNull(value)) {
                return true;
            }
            String valueStr = value.toString();
            String fieldName = getFieldName(field);
            switch (like.value()) {
                case Like.LEFT:
                    valueStr = "%" + valueStr;
                    break;
                case Like.RIGHT:
                    valueStr = valueStr + "%";
                    break;
                case Like.BOTH:
                    valueStr = "%" + valueStr + "%";
                    break;
                default:
                    throw new AllocationException("Like.value()类型错误");
            }
            criteria.andLike(fieldName, valueStr);
            return true;
        }
        return false;
    }

    private void nullOperator(Example.Criteria criteria, Field field) {
        String fieldName = getFieldName(field);
        Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());
        criteria.andCondition(fieldName + " = ", value);
    }

    private void notNullOperator(Example.Criteria criteria, Class clazz, Field field, Operator operator) {
        String fieldName = getFieldName(field);

        Object value = FieldUtil.getFieldValueByName(field.getName(), getTarget());

        if (nullValue(operator, value)) {
            return;
        }

        String condition = fieldName + " " + operator.value() + " ";
        criteria.andCondition(condition, value);
    }


}

