package cn.lianrf.utils.db.mongo;


import cn.lianrf.utils.db.annotation.Operator;
import cn.lianrf.utils.db.base.BaseCriteria;
import cn.lianrf.utils.db.example.UserDto;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: v1.0
 * @date: 2019/9/18
 * @author: lianrf
 */
public class BuilderMongoCriteria extends BaseCriteria {

    private static Map<String,Object> operatorCriteria=new HashMap<String, Object>();

    static {
        operatorCriteria.put(Operator.EQUALS,"");
        operatorCriteria.put(Operator.IN,null);
        operatorCriteria.put(Operator.GT,null);
        operatorCriteria.put(Operator.GTE,null);
        operatorCriteria.put(Operator.LT,null);
        operatorCriteria.put(Operator.LTE,null);
        operatorCriteria.put(Operator.No_EQUALS,null);
        operatorCriteria.put(Operator.NOT_IN,null);
    }

    public <T> BuilderMongoCriteria(T t) {
        super(t);
    }

    public static <T> BuilderMongoCriteria of(T t){
        return new BuilderMongoCriteria(t);
    }

    public Criteria createCriteria(){
        Criteria criteria = new Criteria();
        Class clazz = getClazz();
        List<Field> fields = getFields();
        for (Field field : fields) {
            Operator operator = field.getDeclaredAnnotation(Operator.class);
            if(operator!=null){
                String value = operator.value();

                if (value==null||"".equals(value)) continue;
                //处理字段名
                String fieldName=null;
                Column column = field.getDeclaredAnnotation(Column.class);
                if(column!=null) fieldName=column.name();


            }

        }
        return criteria;
    }




    public static void main(String[] args) {
        BuilderMongoCriteria.of(new UserDto()).createCriteria();
    }
}
