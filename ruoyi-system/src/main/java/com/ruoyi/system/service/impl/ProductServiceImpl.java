package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.enums.OrderStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.Order;
import com.ruoyi.system.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductMapper;
import com.ruoyi.system.domain.Product;
import com.ruoyi.system.service.IProductService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
@Service
public class ProductServiceImpl implements IProductService 
{
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Product selectProductById(Long id)
    {
        return productMapper.selectProductById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param product 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Product> selectProductList(Product product) {
        if (SecurityUtils.getUserId() != 1L) {
            product.setUserId(SecurityUtils.getUserId());
        }
        List<Product> productList = productMapper.selectProductList(product);
        productList = productList.stream().map(product1 -> {
            // 计算得分
            Order order = new Order();
            order.setbId(product1.getUserId());
            order.setStatus(OrderStatus.COMMIT.getCode());
            order.setpId(product1.getId());
            List<Order> orderList = orderMapper.selectOrderList(order);
            if (orderList.isEmpty()){
                product1.setScore(0L);
            } else {
                long tmp = 0L;
                for (Order order1 : orderList) {
                    tmp += order1.getScore();
                }
                product1.setScore(tmp / orderList.size());
            }
            return product1;
        }).collect(Collectors.toList());
        return productList;
    }

    @Override
    public List<Product> selectProductListForRe(Product product) {
        List<Product> productList = productMapper.selectProductList(product);
        productList = productList.stream().map(product1 -> {
            // 计算得分
            Order order = new Order();
            order.setbId(product1.getUserId());
            order.setStatus(OrderStatus.COMMIT.getCode());
            order.setpId(product1.getId());
            List<Order> orderList = orderMapper.selectOrderList(order);
            if (orderList.isEmpty()){
                product1.setScore(0L);
            } else {
                long tmp = 0L;
                for (Order order1 : orderList) {
                    tmp += order1.getScore();
                }
                product1.setScore(tmp / orderList.size());
            }
            return product1;
        }).collect(Collectors.toList());
        return productList;
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param product 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertProduct(Product product)
    {
        product.setCreateTime(DateUtils.getNowDate());
        return productMapper.insertProduct(product);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param product 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateProduct(Product product)
    {
        product.setUpdateTime(DateUtils.getNowDate());
        return productMapper.updateProduct(product);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProductByIds(Long[] ids)
    {
        return productMapper.deleteProductByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProductById(Long id)
    {
        return productMapper.deleteProductById(id);
    }
}
