package test;

import java.io.Serializable;

/**
 * Created By maxiaoyao Date: 2017/9/26 Time: 下午1:56
 */
public class Result<T> implements Serializable {
    private T data;
    private static final long serialVersionUID = -7671033084414975063L;

    public Result(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
