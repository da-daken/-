package com.ruoyi.web.controller.order;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.EnableTime;
import com.ruoyi.system.domain.request.EnableTimeRequest;
import com.ruoyi.system.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预约Controller
 *
 * @author daken
 */
@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    /**
     * 获取可用的预约时间数组
     * @param enableTimeRequest
     * @return
     */
    @PostMapping
    public AjaxResult getEnableTime(@RequestBody EnableTimeRequest enableTimeRequest){
        List<EnableTime> enableTimes = reserveService.checkReserveTime(enableTimeRequest.getCalDate(), enableTimeRequest.getbId());
        return AjaxResult.success(enableTimes);
    }
}
