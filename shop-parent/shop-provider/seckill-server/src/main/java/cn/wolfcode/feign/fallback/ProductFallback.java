package cn.wolfcode.feign.fallback;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.feign.ProductFeignApi;
import org.springframework.stereotype.Component;

/**
 * @author c1eye
 * time 2022/6/14 21:00
 */
@Component
public class ProductFallback implements ProductFeignApi {

    @Override
    public Result<Product> findProductById(Long productId) {
        return null;
    }
}
