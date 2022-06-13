package cn.wolfcode.feign;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.feign.fallback.SeckillProductFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author c1eye
 * time 2022/6/13 20:48
 */
@FeignClient(name = "seckill-service",fallback = SeckillProductFeignFallback.class)
public interface SeckillProductFeignApi {

    @RequestMapping("/seckillProduct/selectSeckillProduct")
    Result<List<SeckillProductVo>> selectSeckillProduct();
}
