package next.config;

import core.annotation.Bean;
import core.annotation.ComponentScan;
import core.annotation.Configuration;
import core.jdbc.MyJdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"next", "core"})
public class MyConfiguration {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/my-jwp-basic;AUTO_SERVER=TRUE";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        return dataSource;
    }

    @Bean
    public MyJdbcTemplate myJdbcTemplate(DataSource dataSource) {
        return new MyJdbcTemplate(dataSource);
    }
}
