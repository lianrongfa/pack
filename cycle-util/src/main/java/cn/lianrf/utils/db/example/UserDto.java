package cn.lianrf.utils.db.example;

import cn.lianrf.utils.db.annotation.Operator;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/9/17
 * @author: lianrf
 */
@Data
public class UserDto {


    @Column(name = "edit_time")
    @Operator(Operator.LT)
    private LocalDate hehe1=LocalDate.now();

    @Column(name = "edit_time")
    @Operator(Operator.GTE)
    private LocalDate hehe2=LocalDate.now();


    private String name="123123";


}
