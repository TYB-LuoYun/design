package top.anets.utils;

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
}
