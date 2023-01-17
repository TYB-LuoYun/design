package top.anets.utils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
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
     * 知道 目标str的前后，模糊匹配值
     */
    public static List<String> findStrByLikeMatch(String start ,String end ,String fullText){
        try {
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

    public static void main(String[] args){
        System.out.println(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP).toString());
    }


}
