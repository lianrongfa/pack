package cn.lianrf;

import cn.lianrf.service.SchedualServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * feigin集成了ribbon，默认实现了负载均衡功能 ，feigin是个声明式伪http客户端
 *
 * feigin采用的是基于接口的注解
 * feigin整合了Hystrix拥有熔断功能(默认熔断功能没有开启，需要在配置文件中开启 feign.hystrix.enabled=true)
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class FeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class,args);
    }
    @Autowired
    private SchedualServiceHi serviceHi;

    @GetMapping("hi")
    public void hi(@RequestParam("name") String name){
        System.out.println(serviceHi.hi(name));
    }
}
