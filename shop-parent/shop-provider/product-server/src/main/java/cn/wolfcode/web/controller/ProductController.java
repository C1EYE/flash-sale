package cn.wolfcode.web.controller;

import cn.wolfcode.common.exception.BusinessException;
import cn.wolfcode.common.web.CommonCodeMsg;
import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private IProductService productService;

    @RequestMapping("/findProductById")
    public Result<Product> findProductById(@RequestParam Long productId){
        if (StringUtils.isEmpty(productId)){
            throw new BusinessException(CommonCodeMsg.PARAM_ERROR);
        }
        return Result.success(productService.findProductById(productId));
    }
}
