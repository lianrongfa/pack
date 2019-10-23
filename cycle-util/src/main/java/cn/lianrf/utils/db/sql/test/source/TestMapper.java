package cn.lianrf.utils.db.sql.test.source;

import cn.lianrf.utils.db.sql.test.TestEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * 用于测试mapper 与工具类无关
 * @version: v1.0
 * @date: 2019/10/14
 * @author: lianrf
 */
public interface TestMapper {
    @Select("SELECT * FROM file_record WHERE id = #{id}")
    Map selectById(int id);

    @SelectProvider(type = TestProvider.class,method = "test1")
    List<Map> selectProvider1(int id,String name);

    @SelectProvider(type = TestProvider.class,method = "test2")
    List<Map> selectProvider2(TestEntity entity);
}
