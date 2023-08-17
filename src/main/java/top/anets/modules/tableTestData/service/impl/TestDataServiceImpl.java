package top.anets.modules.tableTestData.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.transaction.annotation.Transactional;
import top.anets.base.WrapperQuery;
import top.anets.exception.ServiceException;
import top.anets.modules.tableTestData.entity.TestData;
import top.anets.modules.tableTestData.mapper.TestDataMapper;
import top.anets.modules.tableTestData.service.ITestDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2023-08-17
 */
@Service
public class TestDataServiceImpl extends ServiceImpl<TestDataMapper, TestData> implements ITestDataService {


    /**
     * 排他锁
     * 在某个事务中 加了for update 的 查询，代表给表加了个行锁 ，其他带有for update的查询会等待当前事务执行完才会继续往下走
     * 如果查询条件使用索引或主键，for update 是行级锁。
     * 如果查询条件没有使用索引或主键，for update 是表级锁
     * @param admin
     * @param amount
     */
    @Override
    @Transactional
    public void addBalance(String admin, BigDecimal amount) {
        TestData  byId = baseMapper.selectOne(Wrappers.<TestData>lambdaQuery().eq(TestData::getId, admin).last("FOR UPDATE"));
        byId.setBalance(byId.getBalance().add(amount));
        this.updateById(byId);
    }

    @Override
    @Transactional
    public void addBalanceSleep(String admin, BigDecimal amount) {

        TestData  byId = baseMapper.selectOne(Wrappers.<TestData>lambdaQuery().eq(TestData::getId, admin).last("FOR UPDATE"));
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(true){
            throw new ServiceException("SSSSSS");
        }
        byId.setBalance(byId.getBalance().add(amount));
        this.updateById(byId);
    }
}
