package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Product;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public interface IProductService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Product selectProductById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param product 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增【请填写功能名称】
     * 
     * @param product 【请填写功能名称】
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改【请填写功能名称】
     * 
     * @param product 【请填写功能名称】
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteProductByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteProductById(Long id);

}
