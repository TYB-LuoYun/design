package top.anets.modules.log.requestDbLog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.anets.base.PageQuery;
import top.anets.base.QueryMap;
import top.anets.base.WrapperQueryForMongo;
import top.anets.modules.Mongodb.MongoDBUtil;
import top.anets.modules.log.appender.Loginfo;
import top.anets.modules.log.requestDbLog.vo.LogSearchsVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.support.mongodb.danamicdatasource.MongoDB;
import top.anets.support.mongodb.danamicdatasource.MongoDataSource;
import top.anets.utils.DateUtils;

import javax.validation.Valid;

/**
 * @author ftm
 * @date 2024-04-18 15:46
 */
@RestController
@RequestMapping("logback")
public class LogbackController {

    @MongoDataSource(MongoDB.LOGDB)
    @RequestMapping("page")
//    @FetchParam
    public IPage pages(@Valid LogSearchsVo params, PageQuery pageQuery ) {
        QueryMap from = QueryMap.from(params, Loginfo.class);
        from.gte(Loginfo::getTime, DateUtils.parseDateSmart(params.getTimeBegin() ));
        from.lt(Loginfo::getTime,DateUtils.parseDateSmart(params.getTimeEnd()) );
        from.desc(Loginfo::getTime);
        IPage<Loginfo> page = MongoDBUtil.page(pageQuery.Page(), WrapperQueryForMongo.query(from), Loginfo.class);
        return page;
    }

}
