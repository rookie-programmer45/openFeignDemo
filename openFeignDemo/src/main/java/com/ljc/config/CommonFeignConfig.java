package com.ljc.config;

import com.ljc.common.CommonResp;
import com.ljc.util.JsonUtil;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
@EnableFeignClients(basePackages = "com.ljc" ,defaultConfiguration = CommonFeignConfig.class)   // 设置EnableFeignClients的defaultConfiguration，将该配置类设置为Feign的默认配置类，这样FeignClient用的Decoder就是该配置类注入的Decoder
@Import(FeignClientsConfiguration.class)    // 把原先的配置类的配置内容引入到该配置类中
public class CommonFeignConfig {

    @Resource
    private Decoder originalDecoder;    // 获取原先的Decoder

    @Bean
    public RequestInterceptor commonRequestInterceptor() {
        return new MyHeaderInterceptor();
    }

    @Bean
    public Decoder customDecoder() {
        return new CommonDecoder(originalDecoder);
    }

    /**
     * 用于解析Feign接口返回的响应，把业务响应体从CommonResp中解构出来并返回
     */
    private static class CommonDecoder implements Decoder {

        private Decoder delegate;

        public CommonDecoder(Decoder delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            Object decode = delegate.decode(response, type);

            if (decode instanceof String) {
                CommonResp<?> resp = (CommonResp<?>) JsonUtil.toObject((String) decode, CommonResp.class);
                return resp == null ? decode : resp.getData();
            }
            return (decode instanceof CommonResp ? ((CommonResp<?>) decode).getData() : decode);
        }
    }
}
