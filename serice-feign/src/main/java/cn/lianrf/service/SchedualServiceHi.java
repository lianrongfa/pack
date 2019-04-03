package cn.lianrf.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("client")
public interface SchedualServiceHi {
    @GetMapping("/hi")
    String hi(@RequestParam("name") String name);
}
