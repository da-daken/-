package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WorkTime;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-02
 */
public interface WorkTimeMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param bId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public WorkTime selectWorkTimeById(Long bId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param workTime 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<WorkTime> selectWorkTimeList(WorkTime workTime);

    /**
     * 新增【请填写功能名称】
     * 
     * @param workTime 【请填写功能名称】
     * @return 结果
     */
    public int insertWorkTime(WorkTime workTime);

    /**
     * 修改【请填写功能名称】
     * 
     * @param workTime 【请填写功能名称】
     * @return 结果
     */
    public int updateWorkTime(WorkTime workTime);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteWorkTimeById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkTimeByIds(Long[] ids);
}
