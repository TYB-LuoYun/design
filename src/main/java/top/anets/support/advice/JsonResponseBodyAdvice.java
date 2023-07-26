package top.anets.support.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.anets.annotation.Response;
import top.anets.utils.Result;

import java.lang.annotation.Annotation;

/**
 * @author admin
 */
@RestControllerAdvice(basePackages = "com.dicomclub.cdr")
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private static final String FEIGN_REQUEST_ID = "FEIGN_REQUEST_ID";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String path = serverHttpRequest.getURI().getPath();

        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        if(annotations!=null&&annotations.length>0){
            for (Annotation item: annotations ){
                if(item.annotationType().equals(Response.class)){
                    return o;
                }
            }
        }


        //Feign请求时通过拦截器设置请求头，如果是Feign请求则直接返回实体对象
        boolean isFeign = serverHttpRequest.getHeaders().containsKey(FEIGN_REQUEST_ID);
        if(isFeign){
            return o;
        }

        if (o instanceof Result) {
            return o;
        }

        if (o instanceof String || String.class == methodParameter.getMethod().getReturnType()) {
            //解决返回值为字符串时，不能正常包装
            return JSON.toJSONString(Result.success(o));
        }
        return Result.success(o);
    }
}
