package top.anets.modules.regex;

import org.springframework.boot.SpringApplication;
import top.anets.DesignApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {

       String s ="44,555,555，333,woe";
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
            System.out.println(m.group());
        }

        System.out.println(strs);




    }

    public void fun(){
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
}
