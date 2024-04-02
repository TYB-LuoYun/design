package top.anets.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {


    /**
     * 根据根路径和相对路径，拼接正确的URL,去掉重复的/情况
     * 比如http://127.0.0.1/  和 /get/file 拼接成 http://127.0.0.1//get/file
     * @param
     * @return
     */
    public static String joinUrl(String baseUrl, String relativeUrl){
        return (baseUrl+"/"+relativeUrl).replaceAll("((?<!:)/{2,})|(\\\\)", "/") ;
    }



    public static List<String>  fetchWord(String str){
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(str);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }

    /**
     * 从文本查找符合 正则 的字符串
     */
    public static List<String> fingStrFromText(String targetStrRegex,String fulltext) {
        List<String> result = new ArrayList<String>();

        Pattern pattern = Pattern.compile(targetStrRegex);

        Matcher matcher = pattern.matcher(fulltext);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 查找结尾的字符串
     * @param startRegex
     * @param fullText
     * @return
     */
    public static String  findStrByLikeMatchInEnd(String startRegex, String fullText) {
        List<String> match = findStrByLikeMatch(startRegex, null, fullText);
        if(match == null){
            return null;
        }
        return match.get(0);
    }




    /**
     * 知道 目标str的前后，模糊匹配值
     */
    public static List<String> findStrByLikeMatch(String start ,String end ,String fullText){
        try {
            if(start == null){
                start = "";
            }
            if(end == null){
                end="";
            }
            Pattern pattern = Pattern.compile("("+start +")(.*?)("+end +")");
            Matcher matcher = pattern.matcher(fullText);
            ArrayList<String> strings = new ArrayList<>();
            while (matcher.find()) {
                strings.add(matcher.group(2));
            }
            return strings;
        }catch (Exception e){
            return null;
        }
    }


    public static String findStrByLikeMatchOne(String start ,String end ,String fullText){
        try {
            if(start == null){
                start = "";
            }
            if(end == null){
                end="";
            }
            Pattern pattern = Pattern.compile("("+start +")(.*?)("+end +")");
            Matcher matcher = pattern.matcher(fullText);
            ArrayList<String> strings = new ArrayList<>();
            while (matcher.find()) {
                return matcher.group(2).trim();
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 获取根url:http://www.baidu.com/rre/www/www  => http://www.baidu.com
     */
    public static String getRootUrl(String fullUrl) {
        String s = fullUrl;
        Pattern p = Pattern.compile("([^:/])(/)");
        Matcher m = p.matcher(s);
        if (m.find()) {
            int start = m.start(2);
            return s.substring(0,start);
        }else{
            return null;
        }
    }



    /**
     * regexGroup(组1)(组2)
     * @param fullText
     * @param regexGroup
     * @return
     */
    public static List<String> groupMatch(String fullText, String regexGroup) {
        // 匹配数字和 URL 部分
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(fullText);

        List<String> list = new ArrayList<>();
        if (matcher.find()) {

            for(int i=1;i<=matcher.groupCount();i++){
                list.add(matcher.group(i));
            }
        }
        return list;
    }


    public static boolean validatePassword(String password) {
        // 密码长度不少于8位，必须包含大小写字母、数字和特殊字符
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    // 使用正则表达式匹配字符出现的次数
    public static int count(String str, String target) {
        // 构建正则表达式，匹配目标字符// 使用Pattern.quote()来转义特殊字符
        Pattern pattern = Pattern.compile(Pattern.quote(target));
        Matcher matcher = pattern.matcher(str);
        // 计数匹配到的次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    public static void main(String[] args) {
        // 测试密码
        String password1 = "Abcefg1$"; // 合格密码
        String password2 = "12345678";  // 不包含特殊字符
        String password3 = "Abcdefgh";  // 不包含数字和特殊字符
        String password4 = "abcdefghi"; // 不包含大写字母、数字和特殊字符

        System.out.println("Password 1: " + validatePassword(password1)); // true
        System.out.println("Password 2: " + validatePassword(password2)); // false
        System.out.println("Password 3: " + validatePassword(password3)); // false
        System.out.println("Password 4: " + count("weue.eee.jpg", ".")); // false
    }

}
