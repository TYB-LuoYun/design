package top.anets.exceptions;

/**
 * @author ftm
 * @date 2022/12/6 0006 16:17
 */

import com.baomidou.mybatisplus.annotation.TableField;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.anets.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理器
 *
 * @author Yuzhe Ma
 * @date 2018/11/12
 */
@ControllerAdvice
public class SpecialGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MysqlDataTruncation.class)
    public Result exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        e.printStackTrace();
        String msg = null;
       try {
           msg = e.getCause().getMessage();
       }catch (Exception e1){
       }

        try {
            String column = findStrByLikeMatch("Data truncation: Data too long for column '", "' at", e.getCause().getMessage()).get(0);
            String filedName=  null;

//           尝试读取中文解释
            List<String> strByLikeMatch = findStrByLikeMatch("The error may involve ", "Mapper", e.getMessage());
            Class<?> clazz = getClazz(strByLikeMatch.get(0) + "Mapper");
            Class<?> classT = getInterActualType(clazz);
            Field[] fields = classT.getDeclaredFields();
            for(Field fie : fields){
                if(!fie.isAccessible()){
                    fie.setAccessible(true);
                }
                TableField annotation = fie.getAnnotation(TableField.class);
                if(annotation!=null&&annotation.value().equals(column)){
//                   获取中文解释
                    ApiModelProperty annotation1 = fie.getAnnotation(ApiModelProperty.class);
                    if(annotation1!=null){
                        filedName = annotation1.value();
                    }
                }
            }
            if(StringUtils.isNotBlank(filedName)){
                msg = filedName+"字段("+column+")输入过长";
//                msg = filedName+"字段输入过长";
            }

        }catch (Exception e1){
        }

       if(msg!=null){
           return Result.error(msg);
       }else{
           return Result.error(e.getMessage());
       }
    }





    /**
     * 知道 目标str的前后，模糊匹配值
     */
    public static List<String> findStrByLikeMatch(String start , String end , String fullText){
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
     * 获取接口父类的泛型对象
     * @Description: interTypeTest
     */
    public static Class getInterActualType(Class objClass)
    {
        // 获取实例对象父接口
        Type[] interTypes = objClass.getGenericInterfaces();
        // 转化抽象父类为参数类,因为这里我们只有一个父接口,所以下表0位我们需要的
        ParameterizedType pType=(ParameterizedType) interTypes[0];
        // 获取父接口的参数类型数组
        Type[] types = pType.getActualTypeArguments();
        // 因为我们父接口中泛型参数只有一个,所以泛型类型数组第一个就是我们的泛型类型,Class是Type的子类
        Class clazz=(Class) types[0];
        return clazz;
    }

    public static Class<?> getClazz(String clazzName){
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
