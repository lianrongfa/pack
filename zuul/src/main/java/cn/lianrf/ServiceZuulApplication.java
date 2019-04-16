package cn.lianrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @version: v1.0
 * @date: 2019/4/15
 * @author: lianrf
 */

/**
 * zuul的主要功能是路由转发和过滤，路由是微服务的一部分功能。例如 /api/user转发到user服务  /api/order转发到order服务
 * 同时zuul集成了ribbon，拥有负载均衡功能
 *
 *
 *
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient
public class ServiceZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run( ServiceZuulApplication.class, args );
    }
}
