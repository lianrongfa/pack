package cn.lianrf.utils.db.sql.mapper;

import cn.lianrf.utils.db.sql.test.tk.ExactInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @version: v1.0
 * @date: 2019/11/5
 * @author: lianrf
 */
@RegisterMapper
public interface InsertMapperExtend<T> {
    @InsertProvider(type = ExactInsertProvider.class,method ="dynamicSQL" )
    void insertExact();
}
