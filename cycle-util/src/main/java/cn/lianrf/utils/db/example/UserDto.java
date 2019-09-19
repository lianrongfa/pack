package cn.lianrf.utils.db.example;

import cn.lianrf.utils.db.annotation.Operator;
import lombok.Data;

import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/9/17
 * @author: lianrf
 */
@Data
public class UserDto {
    @Operator
    private String name;

    @Operator(Operator.IN)
    private List<String> ids;

    private String test;

}
