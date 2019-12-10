package cn.lianrf.utils.db.sql.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 所有业务mapper继承此mapper
 * @version: v1.0
 * @date: 2019/10/14
 * @author: lianrf
 */
@RegisterMapper
public interface MyMapper<T> extends Mapper<T>,InsertMapperExtend<T>, SelectMapperExtend<T> {

}
