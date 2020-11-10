package com.fl.angel.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * @Author fdm
 * @Date 2020/11/10 12:16
 * @description：
 */
@RefreshScope
@RestController
@Slf4j
public class TestContoller {
    @Value("${test}")
    private String test;

    /**
     * @return
     */
    @RequestMapping("/provider")
    public String provider(@RequestBody String prarm) {
        log.info("服务提供者正常服务"+test);
        System.out.println("------------------"+prarm);
        return "服务提供者正常服务,请求参数："+prarm;
    }
}
