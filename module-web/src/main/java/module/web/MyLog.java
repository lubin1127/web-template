package module.web;

import org.apache.commons.logging.Log;

/**
 * @author lubin
 * @date 2021/8/7
 */
public class MyLog {

    private static final int index = 2;

    public static void log(Log logger) {
        logger.error(Thread.currentThread().getStackTrace()[index].toString());
    }

}
