package cn.lianrf.utils.db.sql.mapper;

import cn.lianrf.utils.db.sql.test.tk.ExpandSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/11/5
 * @author: lianrf
 */
@RegisterMapper
public interface SelectMapperExtend<T> {
    @SelectProvider(type = ExpandSelectProvider.class,method = "dynamicSQL")
    List selectByExampleExpand();
}
