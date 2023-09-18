package com.ljc.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义feign的RequestInterceptor来传请求头信息
 */
public class MyHeaderInterceptor implements RequestInterceptor {
    private Map<String, String> headers = new HashMap<>();

    public MyHeaderInterceptor() {
        addHeader("myHeader", "myHeaderValue");
    }

    @Override
    public void apply(RequestTemplate template) {
        headers.forEach((k, v) -> {
            template.header(k, v);
        });
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
