package top.anets.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ftm
 * @date 2022/11/3 0003 18:19
 */
public class StrUtil {
    public static List<String> fetchWord(Object str) {
        if (str instanceof String) {
            List<String> strs = new ArrayList<String>();
            Pattern p = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+");
            Matcher m = p.matcher((String) str);
            while (m.find()) {
                strs.add(m.group());
            }
            return strs;
        }
        return (List<String>) str;
    }

    public static String joinStr(String joinFlag,String... str){
        if(str == null||str.length==0){
            return "";
        }
        List<String> strings = Arrays.asList(str);
        StringBuffer stringBuffer = new StringBuffer();
        strings.forEach(item->{
            if(item!=null&&item!=""){
                stringBuffer.append(item+joinFlag);
            }
        });
        System.out.println(stringBuffer.toString());
       return stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
    }

    public static String joinStr(String joinFlag,List<String> strings){

        StringBuffer stringBuffer = new StringBuffer();
        strings.forEach(item->{
            if(item!=null&&item!=""){
                stringBuffer.append(item+joinFlag);
            }
        });
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
    }


    public static void main(String[] args){
        String s = joinStr("-", "1",null, "3");
        System.out.println(s);
    }
}
