package cn.wolfcode.service;


import cn.wolfcode.domain.SeckillProductVo;

import java.util.List;

public interface ISeckillProductService {

    List<SeckillProductVo> selectSeckillProduct();

    List<SeckillProductVo> queryByTime(Integer time);

    SeckillProductVo find(Integer time, Long seckillId);
}
