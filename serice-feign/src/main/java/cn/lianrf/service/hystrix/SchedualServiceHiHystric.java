package cn.lianrf.service.hystrix;

import cn.lianrf.service.SchedualServiceHi;
import org.springframework.stereotype.Component;

/**
 * @version: v1.0
 * @date: 2019/4/3
 * @author: lianrf
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String hi(String name) {
        return "hystric:"+name;
    }
}
