package cn.lianrf.utils.db.sql.test.source;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Map;

/**
 * @version: v1.0
 * @date: 2019/10/14
 * @author: lianrf
 */
public class TestProvider {

    /**
     * 当参数为多个时，可以用map接收参数
     * @param map
     * @return
     */
    public String test1(Map map){

        return "SELECT * FROM file_record limit 10";
    }

    /**
     * 当参数为多个时，可以用Object与ProviderContext接收参数
     * ProviderContext为目标Mapper的描述
     * @param context
     * @return
     */
    public String test2(Object object,ProviderContext context){

        return "SELECT * FROM file_record limit 10";
    }
}
