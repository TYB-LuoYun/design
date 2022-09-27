package top.anets.modules.bigDataInsert.service;

import top.anets.modules.bigDataInsert.entity.GeneralTable;

import java.util.List;

/**
 * @author ftm
 * @date 2022/9/19 0019 15:50
 */
public interface BatchOperateMysqlInf {
    boolean insert(List<GeneralTable> list);
}
