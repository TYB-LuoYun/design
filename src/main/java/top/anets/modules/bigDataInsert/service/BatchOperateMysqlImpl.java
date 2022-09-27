package top.anets.modules.bigDataInsert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.anets.modules.bigDataInsert.entity.GeneralTable;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author ftm
 * @date 2022/9/19 0019 15:51
 */
@Service
public class BatchOperateMysqlImpl implements BatchOperateMysqlInf {

    @Override
    public boolean insert(List<GeneralTable> list) {
//         调用mabatis的批量保存方法，注意配置中url最好加上&rewriteBatchedStatements=true
        return true;
    }

}