package top.anets.modules.datasources;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ftm
 * @date 2024-04-03 11:30
 */
public class DataSourceUtils {
    enum Type{
        MYSQL,
        ORACLE,
        SQLSERVER
    }
    /**
     * 创建数据源连接池
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static DataSource createDataSource(DataSourceUtils.Type type,String url, String username, String password) {
        if(Type.ORACLE == type){
            DataSource oracleDataSource = null;
            try {
                 oracleDataSource = createOracleDataSource(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            return oracleDataSource;
        }else if(Type.SQLSERVER == type){
            return createSqlServerDataSource(url, username, password);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }


    private  static DataSource createOracleDataSource(String url, String username, String password) throws SQLException {
        OracleDataSource dataSource  = new OracleDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static DataSource createSqlServerDataSource(String url, String username, String password) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }


    /**
     * 有了JdbcTemplate就可以不用关心连接的获取和释放了，也不用操心结果的封装
     * @param dataSource
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }


    @Deprecated
    public static ResultSet excuteQuery(DataSource dataSource,String query)  {
        try (Connection connection =dataSource.getConnection();
             Statement statement =connection.createStatement()
        ){
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
