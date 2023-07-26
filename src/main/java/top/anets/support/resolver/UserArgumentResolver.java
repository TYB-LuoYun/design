//package top.anets.support.resolver;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
///**
// * @author ftm
// * @date 2023/3/8 0008 14:26
// */
//@Component
//public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//    @Override
//    public boolean supportsParameter(MethodParameter methodParameter) {
//        return methodParameter.getParameterType().equals(SysUser.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
//        try {
//            return SecurityUtils.getUser();
//        }catch (Exception e){
//            return null;
//        }
//    }
//}
