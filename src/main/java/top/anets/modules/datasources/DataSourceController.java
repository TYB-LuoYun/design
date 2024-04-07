package top.anets.modules.datasources;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ftm
 * @date 2024-04-03 13:24
 */
@RestController
@RequestMapping("mysql")
public class DataSourceController {
    @RequestMapping("query")
    public void query() throws SQLException {
        DataSource dataSource = DataSourceUtils.createDataSource(null, null, null);
        ResultSet resultSet = DataSourceUtils.excuteQuery(dataSource,"select * from user");
    }

}
