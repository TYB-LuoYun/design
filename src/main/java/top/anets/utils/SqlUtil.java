package top.anets.utils;

import cn.hutool.core.convert.Convert;

/**
 * @author ftm
 * @date 2024-04-29 13:15
 */
public class SqlUtil {
    // 将MySQL的LIMIT转换为Oracle语法
    public static String mysqlToOracle(String mysqlQuery) {
        // 将MySQL的LIMIT转换为Oracle的ROWNUM
        // MySQL中的LIMIT 3,8，对应Oracle的ROWNUM >= 4 AND ROWNUM <= 11
        // 计算Oracle中的起始行和结束行

        if(mysqlQuery.contains("limit")){
            int offset = 0;
            int limit = 1;
            String limitStr = mysqlQuery.substring(mysqlQuery.indexOf("limit"));
            String[] split = limitStr.split(",");
            if(split.length>=2){
                offset = RegexUtil.parseNumber(split[0]);
                limit =RegexUtil.parseNumber(split[1]);
            }else{
                offset = 0;
                limit = RegexUtil.parseNumber(split[0]);
            }
            mysqlQuery= mysqlQuery.substring(0,mysqlQuery.indexOf("limit"));

            int startRow = offset + 1;
            int endRow = offset + limit;
            // 构建Oracle语句
            String oracleQuery = "SELECT * FROM (SELECT TMP.*, ROWNUM RN FROM (" + mysqlQuery + ") TMP WHERE ROWNUM <= " + endRow + ") WHERE RN >= " + startRow;
            return oracleQuery;
        }
        return mysqlQuery;
    }


    public static String handleSql(String sql){
        return  mysqlToOracle("select * from user order by time desc limit  1000");
    }


    public static void main(String[] args){
        String s = "2";
        s=handleSql(s);
        System.out.println(s);
    }



}
