package cn.lianrf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
public class Client {
    public static void main(String[] args) {
        SpringApplication.run(Client.class,args);
    }

    @Value("${server.port}")
    private String port;

    @Value("${my-config}")
    private String myConfig;

    @GetMapping("/hi")
    public String hello(@RequestParam("name") String name){
        return "msg:"+name+"port is:"+port+"myConfig:"+myConfig;
    }
}
