package module.mysql.generator;

/**
 * snowflake id生成器
 *
 * @author lubin
 * @date 2018/05/08
 */
public class SnowflakeGenerator {

    /**
     * 开始事件戳(2018-06-01)
     */
    private final static long START_STAMP = 1527782400000L;
    /**
     * 首位0，2-42位为时间戳
     * 接下来为数据中心（5位,暂时没有数据中心,暂作为业务模块划分），机器码（5位），序列（12位）
     */
    private final static long DATA_CENTER_BIT = 5;
    private final static long MACHINE_BIT = 5;
    private final static long SEQUENCE_BIT = 12;

    /**
     * 时间戳，数据中心，机器码应该左移的位数
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = MACHINE_LEFT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    /**
     * 数据中心，机器码，序列最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MIN_DATA_CENTER_NUM = 0L;
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MIN_MACHINE_NUM = 0L;
    private final static long MAX_SEQUENCE_NUM = -1L ^ (-1L << SEQUENCE_BIT);

    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastStamp = -1L;

    public SnowflakeGenerator(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < MIN_DATA_CENTER_NUM) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < MIN_MACHINE_NUM) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currStamp == lastStamp) {
            //时间相同自增
            sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //时间不同重置为0
            sequence = 0L;
        }
        lastStamp = currStamp;
        return (currStamp - START_STAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATA_CENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    public long getTimeMinId(long time) {
        return (time - START_STAMP) << TIMESTAMP_LEFT;
    }

    public long getIdTime(long id) {
        return (id >> TIMESTAMP_LEFT) + START_STAMP;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill < lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

}
