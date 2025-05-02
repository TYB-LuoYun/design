package top.anets.support.springThreadpool;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ftm
 * @date 2024-03-07 16:40
 */
@Slf4j
public class GlobalUncaughtExceptionHandler  implements Thread.UncaughtExceptionHandler {

    private static final GlobalUncaughtExceptionHandler instance = new GlobalUncaughtExceptionHandler();

    private GlobalUncaughtExceptionHandler() {
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("线程池-线程异常 {} ", t.getName(), e);
    }

    public static GlobalUncaughtExceptionHandler getInstance() {
        return instance;
    }

}
