package module.web.controller;

/**
 * @author lubin
 * @date 2021/8/6
 */
public class FailedResult implements Result {

    private Integer result;

    private String msg;

    private FailedResult(Integer result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public static FailedResult valueOf(Integer result, String msg) {
        return new FailedResult(result, msg);
    }

    public static FailedResult checkParamsOf(String msg) {
        return new FailedResult(FailedResultEnum.CHECK_PARAMS.getResult(), msg);
    }

    public static FailedResult defaultException(String msg) {
        return new FailedResult(FailedResultEnum.DEFAULT_EXCEPTION.getResult(), msg);
    }


    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
