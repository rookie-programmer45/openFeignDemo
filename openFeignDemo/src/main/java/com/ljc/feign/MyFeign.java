package com.ljc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MyFeign")
public interface MyFeign {

    @PostMapping("/dynamic")
    public String dynamic();
}
