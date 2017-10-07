package pattern;

import java.io.Serializable;

/**
 * Created By maxiaoyao Date: 2017/10/3 Time: 下午6:24
 */
public class Result<T> implements Serializable{

    private static final long serialVersionUID = -7671033084414975063L;

    private T data;

    public Result(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
