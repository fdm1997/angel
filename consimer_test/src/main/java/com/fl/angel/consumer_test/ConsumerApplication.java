package com.fl.angel.consumer_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author fdm
 * @Date 2020/11/9 11:43
 * @description：
 * @EnableDiscoveryClient :开启服务发现与注册功能
 */

@EnableDiscoveryClient  //开启服务注册与发现
@EnableFeignClients     //开启fegin注册功能
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }

    /**
     * 创建服务调用的对象
     * @return
     * @LoadBalanced 添加此注解后，RestTemplate就具有了ribben客户端负载均衡能力
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
