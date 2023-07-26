package top.anets.modules.gc;

import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.utils.Result;

/**
 * @author ftm
 * @date 2023/2/14 0014 10:53
 */
public class gc {
    public static void main(String[] args){
        Result obj1 = new Result();
        Result obj2 =obj1;
        obj1 = null;
        System.gc();
        System.out.println(obj1);
        System.out.println(obj2);

    }
}
