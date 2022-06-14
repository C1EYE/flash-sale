package cn.wolfcode.feign;

import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.feign.fallback.ProductFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author c1eye
 * time 2022/6/14 20:51
 */

@FeignClient(name = "product-service",fallback = ProductFallback.class)
public interface ProductFeignApi {

    @RequestMapping("/product/findProductById")
    Result<Product> findProductById(@RequestParam Long productId);
}
