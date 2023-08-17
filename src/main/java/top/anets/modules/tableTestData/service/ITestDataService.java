package top.anets.modules.tableTestData.service;

import top.anets.modules.tableTestData.entity.TestData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2023-08-17
 */
public interface ITestDataService extends IService<TestData> {


    void addBalance(String admin, BigDecimal amount);
    void addBalanceSleep(String admin, BigDecimal amount);
}
