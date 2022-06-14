package cn.wolfcode.job;

import cn.wolfcode.common.exception.BusinessException;
import cn.wolfcode.common.web.CommonCodeMsg;
import cn.wolfcode.common.web.Result;
import cn.wolfcode.domain.SeckillProductVo;
import cn.wolfcode.feign.SeckillProductFeignApi;
import cn.wolfcode.redis.SeckillRedisKey;
import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 用于定时扫描数据库将秒杀商品上架
 */
@Component
@Setter@Getter
@RefreshScope
@Slf4j
public class SeckillSyncJob implements SimpleJob {
    @Value("${jobCron.initSeckillProduct}")
    private String cron;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SeckillProductFeignApi seckillProductFeignApi;
    @Override
    public void execute(ShardingContext shardingContext) {
        doWork();
    }
    private void doWork() {
        System.out.println("商品上架");
        // 需要zookeeper
        // 1. 删除redis中秒杀商品的所有信息：商品信息和库存信息
        int[] times = {10, 12, 14};
        // 秒杀商品信息 bigkey 前缀 + time smallkey 秒杀id
        for (int time : times) {
            String bigSeckillProductKey = SeckillRedisKey.SECKILL_PRODUCT_HASH.getRealKey(String.valueOf(time));
            String bigSeckillProductStoreKey = SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.getRealKey(String.valueOf(time));
            redisTemplate.delete(bigSeckillProductKey);
            redisTemplate.delete(bigSeckillProductStoreKey);

        }
        // 2. 读取秒杀服务中的所有秒杀商品信息 rpc
        Result<List<SeckillProductVo>> result = seckillProductFeignApi.selectSeckillProduct();
        if(result == null || result.hasError()){
            throw new BusinessException(CommonCodeMsg.RESULT_ERROR);
        }
        // 3. 把读取到的秒杀商品信息放入redis
        for (SeckillProductVo seckillProductVo : result.getData()) {
            String bigSeckillProductKey = SeckillRedisKey.SECKILL_ORDER_HASH.getRealKey(String.valueOf(seckillProductVo.getTime()));
            String smallSeckillProductKey = SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.getRealKey(String.valueOf(seckillProductVo.getTime()));
            // 秒杀商品信息
            redisTemplate.opsForHash().put(bigSeckillProductKey,String.valueOf(seckillProductVo.getId()), JSON.toJSONString(seckillProductVo));
            // 秒杀商品库存信息
            redisTemplate.opsForHash().put(bigSeckillProductKey,String.valueOf(seckillProductVo.getId()),String.valueOf(seckillProductVo.getStockCount()));


        }
    }


}
