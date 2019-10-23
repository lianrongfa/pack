package cn.lianrf.utils.db.sql.test;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于测试mapper 与工具类无关
 * @version: v1.0
 * @date: 2019/10/15
 * @author: lianrf
 */
@Data
@AllArgsConstructor
public class TestEntity {
    private String name;
    private Integer age;

}
