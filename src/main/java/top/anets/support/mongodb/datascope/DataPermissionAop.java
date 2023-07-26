//package top.anets.support.mongodb.datascope;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.bson.Document;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
//import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author ftm
// * @date 2023/3/10 0010 11:01
// */
//@Component
//@Aspect
//@Slf4j
//public class DataPermissionAop  {
//
//    private ThreadLocal<Boolean> executed = new ThreadLocal<>();
//    private final static String Executed = "Executed";
//
//    @Pointcut("execution(* org.springframework.data.mongodb.core.MongoTemplate.*(..)) && (args(org.springframework.data.mongodb.core.query.Query,.. )" +
//            "|| args(org.springframework.data.mongodb.core.aggregation.Aggregation,.. ))")
//    public void pointCut() {}
//
//
//
//    @Around("pointCut()")
//    public Object pemissionData(ProceedingJoinPoint joinPoint) throws Throwable {
////        if (!Boolean.TRUE.equals(executed.get())) {
////            long start = System.currentTimeMillis();
////            try {
////                executed.set(Boolean.TRUE);
////                handle(joinPoint);
////            } finally {
////                executed.remove();
////            }
////        }else{
////            //          防止方法内调用方法
////        }
//        return handle(joinPoint);
//    }
//
//    private Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object[] args = joinPoint.getArgs();
//        Class<?> entityClass = null;
//        Query query = null;
//        Aggregation aggregation =null;
//        // 判断实体类是否包含orgcode字段
//        Field orgcodeField = null;
//        // 获取实体类和查询条件
//        for (Object arg : args) {
//            if (arg instanceof Class && orgcodeField == null) {
//                entityClass = (Class<?>) arg;
//                orgcodeField = ReflectionUtils.findField(entityClass, "ExamOrganCode");
//            } else if (arg instanceof Query) {
//                query = (Query) arg;
//                boolean isExcuted = judgeExcuted(query);
//                if(isExcuted){
//                    return joinPoint.proceed();
//                }else{
//                    setExecuted(query);
//                }
//            }else if(arg instanceof Aggregation){
//                aggregation =(Aggregation) arg;
//            }
//
//
//        }
//        List<String> orgcodes = null;
//        // 获取当前用户的orgcode列表
//        SysUser user = SecurityUtils.getUser();
//        if(user !=null){
//            orgcodes = user.getAuthorityOrgCodes();
//        }
//        String header = ServletUtil.getRequest().getHeader("organization");
//        if(StringUtils.isNotBlank(header)){
//            orgcodes.clear();
//            orgcodes.add(header);
//        }
//
//        if (orgcodeField != null ) {
//            if(orgcodes == null || orgcodes.isEmpty()){
//                throw new ServiceException("您未被赋予相关组织的数据权限!无权查看!");
//            }
//            if(aggregation!=null){
//                return aggregationHandle(aggregation,orgcodes, joinPoint);
//            }
////          查询是否有默认的in
//            List $in = getOriginCondition$in(query,"ExamOrganCode");
//            Criteria criteria = null;
//            if($in!=null&&!$in.isEmpty()){
//                if(!orgcodes.containsAll($in)){
//                    throw new ServiceException("你在查看非权限范围内的数据,已拦截!");
//                }else{
////                    包含就放行
//                }
//            }else{
//                criteria = Criteria.where("ExamOrganCode").in(orgcodes);
//                // 如果query对象为空，则创建一个新的query对象
//            }
//            if (query == null) {
//                query = new Query();
//            }
//            if(criteria!=null){
//                query.addCriteria(criteria);
//            }
//        }
//        return joinPoint.proceed();
//    }
//
//    private Object aggregationHandle(Aggregation aggregation, List<String> orgCodes, ProceedingJoinPoint joinPoint) throws Throwable {
//        AggregationPipeline pipeline = aggregation.getPipeline();
//        List<AggregationOperation> operations = pipeline.getOperations();
//        List<AggregationOperation> list = new ArrayList<>();
//        list.add(Aggregation.match(Criteria.where("ExamOrganCode").in(orgCodes)));
//        for (AggregationOperation each: operations ) {
//            list.add(each);
//        }
////      反射设置某个对象的值
//        ReflectUtil.setFieldValue(pipeline,"pipeline",list);
//       return joinPoint.proceed();
//    }
//
//    private List getOriginCondition$in(Query query, String field) {
//        List $in = null;
//        BasicDBList list = null;
//        Document queryObject = null;
//        Document document3 = null;
//        try {
//            queryObject = query.getQueryObject();
//            list =(BasicDBList) queryObject.get("$and");
//            if(list!=null){
//                for (int i=0;i<list.size();i++) {
//                    Document document = (Document) list.get(i);
//                    Document document2 = (Document) document.get(field);
//                    if(document2!=null){
//                        $in = (List) document2.get("$in");
//                        break;
//                    }
//                }
//                if($in == null){
////                      说明and操作符没有
//                    document3 = (Document) queryObject.get("ExamOrganCode");
//                    if(document3!=null){
//                        $in = (List) document3.get("$in");
//                    }
//                }
//            }
//        }catch (Exception e){
//        }
//        return $in;
//
//    }
//
//    /**
//     * 判断是否执行过
//     * @param query
//     */
//    private boolean judgeExcuted(Query query) {
//        if(query.getMeta() == null){
//            return false;
//        }
//        if(Executed.equals(query.getMeta().getComment())){
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 设置执行过的信息
//     */
//    private void setExecuted(Query query){
//        if(query.getMeta() != null&& StringUtils.isBlank(query.getMeta().getComment())){
//            query.getMeta().setComment(Executed);
//        }
//    }
//
//
//
//
//}
