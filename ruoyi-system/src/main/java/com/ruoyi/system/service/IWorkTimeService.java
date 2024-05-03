package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WorkTime;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2024-04-02
 */
public interface IWorkTimeService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param bId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public WorkTime selectWorkTimeByBId(Long bId);

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
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteWorkTimeByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteWorkTimeById(Long id);
}
