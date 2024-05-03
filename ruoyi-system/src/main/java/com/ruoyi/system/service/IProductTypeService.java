package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ProductType;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
public interface IProductTypeService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ProductType selectProductTypeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param productType 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ProductType> selectProductTypeList(ProductType productType);

    /**
     * 新增【请填写功能名称】
     * 
     * @param productType 【请填写功能名称】
     * @return 结果
     */
    public int insertProductType(ProductType productType);

    /**
     * 修改【请填写功能名称】
     * 
     * @param productType 【请填写功能名称】
     * @return 结果
     */
    public int updateProductType(ProductType productType);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteProductTypeByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteProductTypeById(Long id);
}
