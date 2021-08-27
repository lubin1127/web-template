package module.scheduled.task;

import org.apache.commons.logging.Log;

/**
 * @author lubin
 * @date 2021/8/25
 */
public abstract class BaseTask {

    private static final String ERROR_FORMAT = "{task:%s}-{error:%s}-{message:%s}";

    public void exec(Log logger, Class<? extends BaseTask> clazz) {
        try {
            this.before();
            this.process();
            this.after();
        } catch (Throwable ex) {
            logger.error(String.format(ERROR_FORMAT, clazz.toString(), ex.getClass().toString(), ex.getMessage()));
        }
    }

    public abstract void before();

    public abstract void process();

    public abstract void after();

}
