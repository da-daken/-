package com.ruoyi.web.controller.order;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 所有订单的数据，返回每个服务的最高分的家政员
     *
     */
    @GetMapping("/goldenService")
    public AjaxResult goldenService() {
        return AjaxResult.success(dataService.goldenService());
    }

    @PostMapping("/hotService")
    public AjaxResult hotService(Date year) {
        return AjaxResult.success(dataService.hotService(year));
    }
}
