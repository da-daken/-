package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductTypeMapper;
import com.ruoyi.system.domain.ProductType;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
@Service
public class ProductTypeServiceImpl implements IProductTypeService
{
    @Autowired
    private ProductTypeMapper productTypeMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ProductType selectProductTypeById(Long id)
    {
        return productTypeMapper.selectProductTypeById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param productType 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ProductType> selectProductTypeList(ProductType productType)
    {
        return productTypeMapper.selectProductTypeList(productType);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param productType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertProductType(ProductType productType)
    {
        productType.setCreateTime(DateUtils.getNowDate());
        return productTypeMapper.insertProductType(productType);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param productType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateProductType(ProductType productType)
    {
        return productTypeMapper.updateProductType(productType);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeByIds(Long[] ids)
    {
        return productTypeMapper.deleteProductTypeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeById(Long id)
    {
        return productTypeMapper.deleteProductTypeById(id);
    }
}
