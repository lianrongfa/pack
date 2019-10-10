package cn.lianrf.utils.db.mongo;

import cn.lianrf.utils.db.annotation.Operator;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: v1.0
 * @date: 2019/10/8
 * @author: lianrf
 */
public enum OperatorCriteria {
    /**
     * 等于 默认方法
     * 见 {@link OperatorCriteria#operator(Criteria, String, Object)}
     */
    EQUALS() {

    },
    /**
     * 不等于
     */
    NO_EQUALS() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            return criteria.and(fieldName).ne(value);
        }
    },
    /**
     * 小于
     */
    LT() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            return criteria.and(fieldName).lt(value);
        }
    },
    /**
     * 大于
     */
    GT() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            return criteria.and(fieldName).gt(value);
        }
    },
    /**
     * 小于等于
     */
    LTE() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            return criteria.and(fieldName).lte(value);
        }
    },
    /**
     * 大于等于
     */
    GTE() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            return criteria.and(fieldName).gte(value);
        }
    },
    /**
     * in
     */
    IN() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            if(value instanceof Collection){
                return criteria.and(fieldName).in((Collection) value);
            }
            return criteria.and(fieldName).in(value);
        }
    },
    /**
     * not in
     */
    NOT_IN() {
        @Override
        public Criteria operator(Criteria criteria, String fieldName, Object value) {
            if(value instanceof Collection){
                return criteria.and(fieldName).not().in((Collection) value);
            }
            return criteria.and(fieldName).not().in(value);
        }
    };
    private static Map<String, OperatorCriteria> map = new HashMap<String, OperatorCriteria>(10);

    static {
        Class<Operator> operatorClass = Operator.class;
        Field[] declaredFields = operatorClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String key = field.getName();
            OperatorCriteria value = valueOf(key);
            map.put(key, value);
        }
    }

    /**
     * 默认方法
     *
     * @param criteria  criteria 不能为空
     * @param fieldName 字段名称
     * @param value     值
     * @return criteria
     */
    public Criteria operator(Criteria criteria, String fieldName, Object value) {
        return criteria.and(fieldName).is(value);
    }
}
