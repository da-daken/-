package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WorkTimeMapper;
import com.ruoyi.system.domain.WorkTime;
import com.ruoyi.system.service.IWorkTimeService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-02
 */
@Service
public class WorkTimeServiceImpl implements IWorkTimeService 
{
    @Autowired
    private WorkTimeMapper workTimeMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param bId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public WorkTime selectWorkTimeByBId(Long bId)
    {
        return workTimeMapper.selectWorkTimeById(bId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param workTime 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<WorkTime> selectWorkTimeList(WorkTime workTime)
    {
        return workTimeMapper.selectWorkTimeList(workTime);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param workTime 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertWorkTime(WorkTime workTime)
    {
        return workTimeMapper.insertWorkTime(workTime);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param workTime 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateWorkTime(WorkTime workTime)
    {
        return workTimeMapper.updateWorkTime(workTime);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteWorkTimeByIds(Long[] ids)
    {
        return workTimeMapper.deleteWorkTimeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteWorkTimeById(Long id)
    {
        return workTimeMapper.deleteWorkTimeById(id);
    }
}
