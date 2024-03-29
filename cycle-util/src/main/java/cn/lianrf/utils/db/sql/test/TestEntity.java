package cn.lianrf.utils.db.sql.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * 用于测试mapper 与工具类无关
 * @version: v1.0
 * @date: 2019/10/15
 * @author: lianrf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
    private String name;
    private Integer age;

    private String title;
}
