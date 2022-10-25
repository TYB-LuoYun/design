package top.anets.modules.Mongodb.example;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.anets.base.PageQuery;

import java.util.List;
import java.util.Map;

public interface ExamSearchListService {
    void save(ExamSearchList examSearchList);

    void removeById(Long id);

    void update(ExamSearchList examSearchList);

    ExamSearchList findById(String id);

    List<ExamSearchList> finds();

    IPage<ExamSearchList> page(Map<String, Object> params, PageQuery pageQuery);



}
