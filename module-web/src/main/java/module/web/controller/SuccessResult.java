package module.web.controller;

/**
 * @author lubin
 * @date 2021/8/6
 */
public class SuccessResult implements Result {

    private static final Integer DEFAULT_RESULT = 200;

    private Integer result;

    private Object data;

    public SuccessResult() {
        this.result = DEFAULT_RESULT;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
