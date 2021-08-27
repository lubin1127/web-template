package module.web.controller;

/**
 * @author lubin
 * @date 2021/8/6
 */
public enum FailedResultEnum {

    CHECK_PARAMS(1100),
    DEFAULT_EXCEPTION(1101)
    ;

    private final Integer result;

    FailedResultEnum(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }
}
