package com.alibaba.json.bvt.bug;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Bug_for_issue_490 extends TestCase {

    private final OkHttpClient client = new OkHttpClient();

    public void test_for_issue() throws Exception {
        Request request = new Request.Builder().url("https://api.github.com/gists/c2a7c39532239ff261be").build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        
        Gist gist = JSON.parseObject(response.body().string(), Gist.class);
        response.body().close();

        for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().content);
        }
    }

    public static class Gist {
        public Map<String, GistFile> files;
    }

    public static class GistFile {
        public String content;
    }

}
