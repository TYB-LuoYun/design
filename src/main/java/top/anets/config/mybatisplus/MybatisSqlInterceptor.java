package top.anets.config.mybatisplus;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.regex.Pattern;

/**
 * 动态修改 mybatis 的 SQL
 *
 * @author eddie
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class MybatisSqlInterceptor implements Interceptor {

    private static final Pattern TENANT_PATTERN = Pattern.compile("@tenant@");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("进入到SQL拦截器=====================================");
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        System.out.println("sql语句:");
        System.out.println(sql);
//        sql = TENANT_PATTERN.matcher(sql).replaceAll("'" + TenantContext.get() + "'");
        Field sqlField = boundSql.getClass().getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql, sql);
        System.out.println("=====================================");
        return invocation.proceed();
    }
}