package cn.wolfcode.feign.fallback;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.feign.SeckillProductFeignApi;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author c1eye
 * time 2022/6/13 20:50
 */

@Component
public class SeckillProductFeignFallback implements SeckillProductFeignApi {
    // 远程方法不可用时的本地方法
    @Override
    public Result<List<SeckillProductVo>> selectSeckillProduct() {
        return null;
    }
}
