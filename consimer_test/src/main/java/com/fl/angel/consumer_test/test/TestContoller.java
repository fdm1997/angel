package com.fl.angel.consumer_test.test;

import com.fl.angel.consumer_test.test.Feginclient.ProviderTestClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author fdm
 * @Date 2020/11/9 12:00
 * @description：
 */

@RestController
@Slf4j
@RefreshScope
public class TestContoller {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${test}")
    private String test;



    @Autowired
    private ProviderTestClient providerTestClient;//注入接口客户端

    //fegin调用测试
    @GetMapping("/consumer2")
    public String consumer2() {
        //调用服务
        String result = providerTestClient.test("你好");
        System.out.println("结果："+result);

        return result;
    }

    //restTemplate调用测试
    @GetMapping("/consumer")
    public String consumer() {
        log.info("---------restTemplate消费者开始------------");
        System.out.println(test);
        //调用服务
        String result = restTemplate.getForObject("http://provider-test/provider", String.class);
        log.info("---------restTemplate消费者结束--------result{}----", result);
        return result;
    }




}
