package cn.lianrf.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用熔断使用@HystrixCommand(fallbackMethod = "fallback")
     * fallbackMethod代表默认返回方法
     * @param msg
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback")
    public String hello(String msg){
        return restTemplate.getForObject("http://CLIENT/hi?name="+msg,String.class);
    }

    public String fallback(String msg){
        return "fallback:"+msg;
    }
}
