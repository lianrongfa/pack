package cn.lianrf.utils.db.example;

import cn.lianrf.utils.db.annotation.EntityClass;
import cn.lianrf.utils.db.annotation.Operator;
import cn.lianrf.utils.db.sql.test.TestEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/9/17
 * @author: lianrf
 */
@Data
@EntityClass(TestEntity.class)
public class UserDto{

    /**
     * 查询edit_time<LocalDate.now()
     */
    @Column(name = "edit_time")
    @Operator(Operator.LT)
    private LocalDate hehe1=LocalDate.now();

    /**
     * 查询edit_time>=LocalDate.now()
     */
    @Column(name = "edit_time")
    @Operator(Operator.GTE)
    private LocalDate hehe2=LocalDate.now();


    @Operator(Operator.IN)
    private List<String> name;

}
