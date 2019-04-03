package cn.lianrf;

import cn.lianrf.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * ribbon是一个负载均衡客户端
 */

/**
 * 通过@EnableDiscoveryClient向服务中心注册
 *
 * 在ribbon中需要使用熔断功能时，需要引入
    <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
   详细使用参考 ${@link HelloService#hello(String)}
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RestController
public class ServiceRibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class,args);
    }

    /**
     *通过@LoadBalanced表名resetTemplate开启了负债均衡功能
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private HelloService service;

    @GetMapping("/hi")
    public void hi(String name){
        System.out.println(service.hello(name));
    }
}
