package cn.lianrf.utils.db.sql.test;

import cn.lianrf.utils.db.sql.test.TestEntity;
import cn.lianrf.utils.db.sql.test.source.TestMapper;
import cn.lianrf.utils.db.sql.test.tk.TKTestMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.session.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * @version: v1.0
 * @date: 2019/10/14
 * @author: lianrf
 */
public class AppTest {
    public static void main(String[] args) {
        testTKMybatis();
    }



    /**
     * mybatis
     */
    public static void testSourceProvider() {
        SqlSession sqlSession = getSession();
        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        System.out.println(mapper.selectProvider1(123,"123"));
        System.out.println(mapper.selectProvider2(new TestEntity("hehh",18)));
    }

    /**
     * tk mybatis
     */
    public static void testTKMybatis() {
        Configuration configuration = new Configuration();

        DataSource dataSource = createDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        configuration.setEnvironment(environment);

        //这里可以参考上面的方式来配置 MapperHelper
        MapperHelper mapperHelper = new MapperHelper();
        configuration.setMapperHelper(mapperHelper);

        configuration.addMapper(TKTestMapper.class);

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session = factory.openSession();

        TKTestMapper mapper = session.getMapper(TKTestMapper.class);

        Example example = new Example(TestEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andCondition("name = ","fwe");
        criteria.andBetween("age",1,10);

        List<TestEntity> testEntities = mapper.selectByExample(example);
        System.out.println(testEntities);
    }

    private static SqlSession getSession() {
        DataSource dataSource = createDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(environment);
        configuration.addMapper(TestMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory.openSession();
    }

    private static DataSource createDataSource() {
        return new PooledDataSource("com.mysql.jdbc.Driver",
                    "jdbc:mysql://192.168.101.204:3306/fast_dfs?serverTimezone=UTC&useSSL=false",
                    "root",
                    "12345678");
    }
}
