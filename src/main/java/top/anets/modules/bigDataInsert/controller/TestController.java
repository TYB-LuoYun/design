package top.anets.modules.bigDataInsert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.bigDataInsert.entity.GeneralTable;
import top.anets.modules.bigDataInsert.service.BatchOperateMysqlInf;
import top.anets.modules.bigDataInsert.utils.BathUtil;
import top.anets.thread.ThreadPoolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ftm
 * @date 2022/9/19 0019 15:48
 */
@RestController
public class TestController {
    @Autowired
    private BatchOperateMysqlInf batchOperateMysqlInf;
    @RequestMapping("/insert")
    public Boolean insert() {
        GeneralTable gt = new GeneralTable();
        Random rand = new Random();
        List<GeneralTable> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            gt.setColAttr("列属性" + rand.nextInt(9) * 1000);
            gt.setColFrom("表属性" + rand.nextInt(9) * 1000);
            gt.setColValue("列值" + rand.nextInt(9) * 1000);
            gt.setColType("列类型" + rand.nextInt(9) * 1000);
            gt.setRowKey((long) rand.nextInt(1000));
            list.add(gt);
        }

        List<List<GeneralTable>> lists = BathUtil.pagingList(list, 1000);
        lists.forEach(item->{
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    boolean a = batchOperateMysqlInf.insert(list);
                }
            });
        });

        return true;
    }
}
