package com.ruoyi.order.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.order.domain.AYTime;
import com.ruoyi.order.domain.EnableTime;
import com.ruoyi.order.domain.OrderTime;
import com.ruoyi.order.service.IOrderService;
import com.ruoyi.order.service.ReserveService;
import com.ruoyi.system.domain.WorkTime;
import com.ruoyi.system.service.IWorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    private IWorkTimeService workTimeService;

    @Autowired
    private IOrderService orderService;

    private final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");

    /**
     * 预约时间表
     */
    private List<EnableTime> timeArray;

    /**
     * 初始化时间数组
     */
    @PostConstruct
    public void init(){
        timeArray = new ArrayList<>(34);
        timeArray.add(new EnableTime("7:00", true));
        timeArray.add(new EnableTime("7:30", true));
        timeArray.add(new EnableTime("8:00", true));
        timeArray.add(new EnableTime("8:30", true));
        timeArray.add(new EnableTime("9:00", true));
        timeArray.add(new EnableTime("9:30", true));
        timeArray.add(new EnableTime("10:00", true));
        timeArray.add(new EnableTime("10:30", true));
        timeArray.add(new EnableTime("11:00", true));
        timeArray.add(new EnableTime("11:30", true));
        timeArray.add(new EnableTime("12:00", true));
        timeArray.add(new EnableTime("12:30", true));
        timeArray.add(new EnableTime("13:00", true));
        timeArray.add(new EnableTime("13:30", true));
        timeArray.add(new EnableTime("14:00", true));
        timeArray.add(new EnableTime("14:30", true));
        timeArray.add(new EnableTime("15:00", true));
        timeArray.add(new EnableTime("15:30", true));
        timeArray.add(new EnableTime("16:00", true));
        timeArray.add(new EnableTime("16:30", true));
        timeArray.add(new EnableTime("17:00", true));
        timeArray.add(new EnableTime("17:30", true));
        timeArray.add(new EnableTime("18:00", true));
        timeArray.add(new EnableTime("18:30", true));
        timeArray.add(new EnableTime("19:00", true));
        timeArray.add(new EnableTime("19:30", true));
        timeArray.add(new EnableTime("20:00", true));
        timeArray.add(new EnableTime("20:30", true));
        timeArray.add(new EnableTime("21:00", true));
        timeArray.add(new EnableTime("21:30", true));
        timeArray.add(new EnableTime("22:00", true));
        timeArray.add(new EnableTime("22:30", true));
        timeArray.add(new EnableTime("23:00", true));
        timeArray.add(new EnableTime("23:30", true));
        timeArray.add(new EnableTime("24:00", true));
    }

    /**
     * 查找家政员可预约的时间数组
     * 核心方法 core
     */
    @Override
    public List<EnableTime> checkReserveTime(Date calDate, Long bId) {

        // 获取阿姨对应星期的工作时间
        List<AYTime> ayTime = getAYTime(calDate, bId);

        // 获取阿姨当天的所有已支付订单时间
        List<AYTime> orderTimes = orderService.getOrderTime1(calDate);

        // 1. 先把当前时间的之前的时间设为 false
        String hm = simpleDateFormat1.format(new Date());
        List<EnableTime> resTimeArray = timeArray.stream().peek(enableTime -> {
            if (checkBefore(enableTime.getTime(), hm)) {
                enableTime.setEnable(false);
            }
        }).collect(Collectors.toList());
        // 2. 从当前时间开始遍历数组
        for (EnableTime enableTime : resTimeArray){
            if (checkBefore(enableTime.getTime(), hm)){
                continue;
            }
            // 3. 不在工作时间内 或者 是否在已支付订单时间内
            if (!orderService.checkInside(enableTime.getTime(), ayTime) || orderService.checkInside(enableTime.getTime(), orderTimes)){
                enableTime.setEnable(false);
            }

        }

        return resTimeArray;
    }

    /**
     * 判断是否在当前时间之前
     * true -> 在当前时间之前
     * false -> 在当前时间之后
     */
    private boolean checkBefore(String time, String hm) {
        String[] split1 = time.split(":");
        String[] split2 = hm.split(":");
        return Integer.parseInt(split1[0] + split1[1]) < Integer.parseInt(split2[0] + split2[1]);
    }

    /**
     * 获取阿姨的工作时间
     * @return 阿姨的工作时间
     */
    private List<AYTime> getAYTime(Date date, Long bId){
        WorkTime workTime = workTimeService.selectWorkTimeByBId(bId);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        List<AYTime> ayTimeList = new ArrayList<>();
        switch (dayOfWeek) {
            case 0 :
                ayTimeList = JSONArray.parseArray(workTime.getSun(), AYTime.class);
                break;
            case 1 :
                ayTimeList = JSONArray.parseArray(workTime.getMon(), AYTime.class);
                break;
            case 2 :
                ayTimeList = JSONArray.parseArray(workTime.getTues(), AYTime.class);
                break;
            case 3 :
                ayTimeList = JSONArray.parseArray(workTime.getWed(), AYTime.class);
                break;
            case 4 :
                ayTimeList = JSONArray.parseArray(workTime.getThur(), AYTime.class);
                break;
            case 5 :
                ayTimeList = JSONArray.parseArray(workTime.getFri(), AYTime.class);
                break;
            case 6 :
                ayTimeList = JSONArray.parseArray(workTime.getSat(), AYTime.class);
                break;
        }
        return ayTimeList;
    }



}
