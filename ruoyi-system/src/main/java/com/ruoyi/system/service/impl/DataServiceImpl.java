package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanCopyUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.AYInfoVo;
import com.ruoyi.system.domain.vo.HotVo;
import com.ruoyi.system.mapper.OrderMapper;
import com.ruoyi.system.mapper.ProductMapper;
import com.ruoyi.system.mapper.ProductTypeMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 所有订单的数据，返回每个服务的最高分的家政员
     * 首页展示
     */
    @Override
    public List<AYInfoVo> goldenService() {
        List<AYInfoVo> ayInfoVos = topGoldenService();
        List<AYInfoVo> ayInfoVos1 = new ArrayList<>();
        boolean flag1 = true;
        boolean flag2 = true;
        for (AYInfoVo ayInfoVo : ayInfoVos) {
            if (!flag1 && !flag2){
                break;
            }
            if (flag1 && ayInfoVo.getTypeName().equals("普通保洁")){
                ayInfoVos1.add(ayInfoVo);
                flag1 = false;
            } else if (flag2 && ayInfoVo.getTypeName().equals("擦玻璃")){
                ayInfoVos1.add(ayInfoVo);
                flag2 = false;
            }
        }
        return ayInfoVos1;
    }

    /**
     * 所有订单数据，返回每个服务得分前几（或者全部）的家政员
     *
     * 数据大盘展示
     */
    @Override
    public List<AYInfoVo> topGoldenService() {

        List<OrderTop> orderTopList = orderMapper.topGoldenService();

        List<AYInfoVo> ayInfoVoList = orderTopList.stream().map(orderTop -> {
            AYInfoVo ayInfoVo = new AYInfoVo();
            SysUser sysUser = userMapper.selectUserById(orderTop.getBId());
            Product product = productMapper.selectProductById(orderTop.getPId());
            ProductType productType = productTypeMapper.selectProductTypeById(product.getTypeId());

            ayInfoVo.setNickName(sysUser.getNickName());
            ayInfoVo.setTypeName(productType.getName());
            ayInfoVo.setScore(orderTop.getAvgScore());
            ayInfoVo.setImgUrl(product.getImg());
            ayInfoVo.setContent(product.getContent());
            return ayInfoVo;
        }).collect(Collectors.toList());

        return ayInfoVoList;
    }

    /**
     * 所有订单数据，返回当前平台每个月的不同服务需求的热度(数量)
     *
     * @param year 以哪一年为参数，
     * 返回一个数组，1 - 12月的服务需求热度
     * 数据大盘展示
     */
    @Override
    public List<HotVo> hotService(Date year) {
        year = new Date();
        List<OrderHot> orderHotList = orderMapper.hotService(year);

        List<HotVo> hotVoList = new ArrayList<>(12);
        // 求 1-12 个月的需求量
        int index = 0;
        for (int i = 1; i <= 12; i++){
            hotVoList.add(new HotVo());
            boolean flag1 = false;
            boolean flag2 = false;
            for(int j = index; j < orderHotList.size(); j++){
                if (orderHotList.get(j).getMonth() == i){
                    if (orderHotList.get(j).getProductId() == 1L){
                        hotVoList.get(i-1).setSum1(orderHotList.get(j).getSum());
                        flag1 = true;
                    } else {
                        hotVoList.get(i-1).setSum2(orderHotList.get(j).getSum());
                        flag2 = true;
                    }
                } else {
                    hotVoList.get(i-1).setSum1( flag1 ? hotVoList.get(i-1).getSum1() : 0L);
                    hotVoList.get(i-1).setSum2(flag2 ? hotVoList.get(i-1).getSum2() : 0L);
                    index = j;
                    break;
                }
            }
        }

        return hotVoList;
    }
}
