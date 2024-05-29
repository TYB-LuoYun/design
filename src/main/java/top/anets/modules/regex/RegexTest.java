package top.anets.modules.regex;

import org.springframework.boot.SpringApplication;
import top.anets.DesignApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

//   断言函数   (?<!A字符串)B字符串      匹配       B字符串的前面排除A字符串



//.   任意字符
//*   多个字符（也可以是0个）
//a*   多个a（也可以是0个a）
//+   多个数
//.*   多个任意字符（任意字符串）
//\\d   数字
//\\d+   多个任意数字
//\\D  非数字
//\\w  匹配任何字类字符（单词类字符），包括下划线。与"[A-Za-z0-9_]"等效
//(x|y)   或  （ 匹配 x 或 y）
//[^,。]    非逗号也非句号的字符
//*代表{0,}　+代表{1,}　?代表{0,1}
    public static void main(String[] args) {
//       perfectUrl();
    }


    /**
     * 去掉URL path中的//问题
     */
    public static void perfectUrl(){
        String url="http://127.0.0.1:88//sys/ssw//35555";
//        String pattern="(/+)";
        String pattern="(?<!:)/{2,}";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(url);
        while(matcher.find()) {
            System.out.println(matcher.group());
        }
        url = url.replaceAll(pattern, "/");
        System.out.println(url);

    }


    /**
     * 解析cookie
     */
    public static  void fun(){
        String s="QKYXWEBSessionID=N2Q0M2FlODUtMzVmNy00MzhkLWFkNDMtOTAxOWU3Y2YxY2Vl; Path=/; HttpOnly; SameSite=Lax";

        String strs ="";
        Pattern p = Pattern.compile("^QKYXWEBSessionID=\\w+");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs+=m.group();
        }
        strs =strs.replace("QKYXWEBSessionID=","" );
        System.out.println(strs);
    }

    /**
     * 44,555,555，333,woe,  中取单词数组
     */
    public static List<String>  fetchWord(Object str){
        if(str instanceof  String){
            List<String> strs = new ArrayList<String>();
            Pattern p = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+");
            Matcher m = p.matcher((String)str);
            while(m.find()) {
                strs.add(m.group());
            }
            return strs;
        }
        return (List<String>) str;
    }
}
