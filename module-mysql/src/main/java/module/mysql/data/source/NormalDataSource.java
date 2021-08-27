package module.mysql.data.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author lubin
 * @date 2021/6/6
 */
public class NormalDataSource {

    private final DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public NormalDataSource() {
        // FISHadmin@2021
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://192.168.32.129:12336/manage?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        config.setUsername("root");
        config.setPassword("MElb@2020");
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(100);
        config.setAutoCommit(true);
        config.setIdleTimeout(500000L);
        config.setMaxLifetime(540000L);
        config.setConnectionTimeout(60000);
        config.setPoolName("DatebookHikariCP");
        config.setConnectionTestQuery("SELECT 1");
        this.dataSource = new HikariDataSource(config);
    }

}
