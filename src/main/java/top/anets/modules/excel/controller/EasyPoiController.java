package top.anets.modules.excel.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.excel.entity.BalanceVo;
import top.anets.modules.excel.entity.Order;
import top.anets.modules.excel.entity.RateVo;
import top.anets.modules.excel.entity.UnitDepositBalance;
import top.anets.modules.excel.utils.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:32
 */
@Controller
@Api(tags = "EasyPoiController", description = "EasyPoi导入导出测试")
@RequestMapping("/easyPoi")
public class EasyPoiController {

    @GetMapping("/export")
    public void export(HttpServletResponse response){
        // 导出数据集合
        List<UnitDepositBalance> unitDepositBalances = new ArrayList<>();

        UnitDepositBalance unitDepositBalance = new UnitDepositBalance();
        unitDepositBalance.setFields("客户证件类型（3）");
        unitDepositBalance.setName("单位");

        List<BalanceVo> balanceVos = new ArrayList<>();

        BalanceVo balanceVo = new BalanceVo();
        balanceVo.setBalanceProp(new BigDecimal(100));
        balanceVo.setCurrentPeriod(new BigDecimal(127488.21));
        balanceVo.setLastPeriod(new BigDecimal(120720.62));
        balanceVo.setMomZJ(BigDecimal.ONE);
        balanceVos.add(balanceVo);

        BalanceVo balanceVo2 = new BalanceVo();
        balanceVo2.setBalanceProp(new BigDecimal(100));
        balanceVo2.setCurrentPeriod(new BigDecimal(127488.21));
        balanceVo2.setLastPeriod(new BigDecimal(120720.62));
        balanceVo2.setMomZJ(BigDecimal.ONE);
        balanceVos.add(balanceVo2);

        unitDepositBalance.setBalanceLists(balanceVos);

        List<RateVo> rateVos = new ArrayList<>();

        RateVo rateVo = new RateVo();
        rateVo.setCurrentRate(new BigDecimal(1.88));
        rateVo.setLastRate(new BigDecimal(1.98));
        rateVos.add(rateVo);
        RateVo rateVo2 = new RateVo();
        rateVo2.setCurrentRate(new BigDecimal(1.88));
        rateVo2.setLastRate(new BigDecimal(1.98));
        rateVos.add(rateVo2);

        unitDepositBalance.setRateVoList(rateVos);

        unitDepositBalances.add(unitDepositBalance);

        // 导出
        ExcelUtil.exportExcel(unitDepositBalances,"单位存款余额","单位存款余额", UnitDepositBalance.class,"导出文件测试.xls",response);
    }


    /**
     * 导入
     */
    @PostMapping("/import")
    public void imports(MultipartFile file){
        List<UnitDepositBalance> unitDepositBalances = ExcelUtil.importExcel(file, 1, 2, UnitDepositBalance.class);
    }

}