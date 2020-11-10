package com.fl.angel.consumer_test.test.Feginclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @Author fdm
 * @Date 2020/11/10 9:39
 * @description：
 */
// name：调⽤的服务名称，和服务提供者yml⽂件中spring.application.name保持⼀致，可以使用url形式，则为直接调取，不走注册中心
@FeignClient(name = "provider-test")
public interface ProviderTestClient {

     @GetMapping(value = "/provider")
     String test(String prarm); //value必须指定，否则会报错
}
