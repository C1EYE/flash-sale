package cn.wolfcode.service.impl;

import cn.wolfcode.common.exception.BusinessException;
import cn.wolfcode.common.web.CommonCodeMsg;
import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.Product;
import cn.wolfcode.domain.SeckillProduct;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.feign.ProductFeignApi;
import cn.wolfcode.mapper.SeckillProductMapper;
import cn.wolfcode.service.ISeckillProductService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class SeckillProductServiceImpl implements ISeckillProductService {
    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ProductFeignApi productFeignApi;
    /**
     * 查询秒杀商品信息
     *
     * @return
     */
    @Override
    public List<SeckillProductVo> selectSeckillProduct() {
        // 1. 读取当天秒杀商品信息
        List<SeckillProduct> seckillProducts = seckillProductMapper.queryAllSeckillProduct();
        // 2. 依据商品的id远程调用产品服务
        ArrayList<SeckillProductVo> ret = new ArrayList<>();
        for (SeckillProduct seckillProduct : seckillProducts) {
            Result<Product> result = productFeignApi.findProductById(seckillProduct.getProductId());
            if (StringUtils.isEmpty(result) || result.hasError()){
                throw new BusinessException(CommonCodeMsg.RESULT_ERROR);
            }
            SeckillProductVo vo = new SeckillProductVo();
            Product product = result.getData();
            // 3. 合并两个表的信息返回结果
            BeanUtils.copyProperties(product, vo);
            BeanUtils.copyProperties(seckillProduct, vo);
            ret.add(vo);
        }
        return ret;
    }
}
