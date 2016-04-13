package data.taobao;

import java.util.List;

public class Response<T> {
    public String api;
    public String v;
    public List<String> ret;
    
    public T data;
}
