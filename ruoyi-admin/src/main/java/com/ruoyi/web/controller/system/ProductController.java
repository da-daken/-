package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanCopyUtils;
import com.ruoyi.system.domain.vo.ProductSinVo;
import com.ruoyi.system.domain.vo.ProductVo;
import com.ruoyi.system.mapper.ProductTypeMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Product;
import com.ruoyi.system.service.IProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2024-03-17
 */
@RestController
@RequestMapping("/system/product")
public class ProductController extends BaseController
{
    @Autowired
    private IProductService productService;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Product product) {
        startPage();
        List<Product> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    @GetMapping("/listForRe")
    public TableDataInfo listForRe(Product product) {
        startPage();
        List<Product> productList = productService.selectProductList(product);
        List<ProductVo> collect = productList.stream().map(product1 -> {
            ProductVo productVo = BeanCopyUtils.copyBean(product1, ProductVo.class);
            productVo.setUsername(userMapper.selectUserById(product1.getUserId()).getNickName());
            productVo.setProductName(productTypeMapper.selectProductTypeById(product1.getTypeId()).getName());
            return productVo;
        }).collect(Collectors.toList());

        return getDataTable(collect, productList);
    }


    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productService.selectProductById(id));
    }

    @GetMapping("/forRe/{id}")
    public AjaxResult getInfoForRe(@PathVariable("id") Long id) {
        Product product = productService.selectProductById(id);
        ProductSinVo productSinVo = BeanCopyUtils.copyBean(product, ProductSinVo.class);
        return success(productSinVo);
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Product product)
    {
        return toAjax(productService.insertProduct(product));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Product product)
    {
        return toAjax(productService.updateProduct(product));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productService.deleteProductByIds(ids));
    }
}
