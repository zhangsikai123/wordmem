package com.sky.wordmem.config;
import com.sky.wordmem.utils.ReaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.config
 */
@Configuration
@Slf4j
public class Database{

    @Value("${db.driverClassName:}")
    private String driverClass;
    @Value("${db.url:}")
    private String url;
    @Value("${db.username:}")
    private String user;
    @Value("${db.password:}")
    private String password;


    @Bean("sqlite")
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        init(dataSource);
        return dataSource;
    }

    private void init(DataSource dataSource){
        try {
            String initSql = ReaderUtil.readFile("sqlite-init.sql");
            Statement statement = dataSource.getConnection().createStatement();
            String[] sqls = initSql.split(";");
            for (String sql : sqls) {
                statement.addBatch(sql);
            }
            int[] results = statement.executeBatch();
        } catch (SQLException e) {
            log.error(e.toString());
        }
    }
}
