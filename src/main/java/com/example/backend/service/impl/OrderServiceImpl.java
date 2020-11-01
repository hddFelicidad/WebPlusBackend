package com.example.backend.service.impl;

import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.OrderPo;
import com.example.backend.service.OrderService;
import com.example.backend.service.ScheduleService;
import com.example.backend.util.CommonUtil;
import com.example.backend.util.OrderFilterUtil;
import com.example.backend.vo.OrderOccupyVo;
import com.example.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    OrderFilterUtil orderFilterUtil;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    OrderRepository orderRepository;


    @Override
    public ResponseVO getOrderOccupy(String date) {
        String start = date + " 00:00:00";
        String end = date + " 23:59:59";
        try{
            return getOccupy(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseVO.buildFailure();
    }

    public ResponseVO getOccupy(String s, String e) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);
        //获取在起止时间内的排程订单
        List<ScheduleOutputDto.Order> orderList = orderFilterUtil.getOrderByDate(scheduleService.tryGetScheduleOutput(),
                startDate, endDate);
        if(orderList == null){
            return ResponseVO.buildFailure();
        }
        //一些初始化
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> tasks = new HashMap<>();
        List<OrderOccupyVo> orderOccupyVoList = new ArrayList<>();
        List<String> links = new ArrayList<>();
        //数据项id，不可重复
        int id = 1;
        //当天能准时交付的订单
        int deliveryCount = 0;

        for(ScheduleOutputDto.Order order: orderList){
            OrderOccupyVo orderOccupy = new OrderOccupyVo();
            String orderId = order.getId();
            OrderPo orderPo = orderRepository.findOrderPoByOrderId(orderId);
            String deadLine = format.format(orderPo.getDeadLine());

            orderOccupy.setId(id);
            orderOccupy.setNumber(orderId);
            //暂时没有获取工艺的方法
            orderOccupy.setText("");
            //产品名称（数据表里暂时没有这一项）
            orderOccupy.setName(orderPo.getItemId());
            orderOccupy.setDeal_date(deadLine);
            //父任务数据项id
            int parentId = id;
            id++;
            List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
            Date dealDate = commonUtil.addHour(subOrderList.get(subOrderList.size() - 1).getStartTime(),
                    subOrderList.get(subOrderList.size() - 1).getDurationTimeInHour());
            orderOccupy.setExpc_date(format.format(dealDate));

            //判断此订单当天是否能准时交付
            if(dealDate.before(endDate) && dealDate.before(orderPo.getDeadLine()))
                deliveryCount++;

            int doneTime = 0;
            int totalTime = 0;

            for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                Date now = new Date();
                int durationHour = subOrder.getDurationTimeInHour();
                Date startTime = subOrder.getStartTime();
                Date endTime = commonUtil.addHour(startTime, durationHour);
                totalTime += durationHour;
                if(startTime.before(now)){
                    if(now.before(endTime)){
                        doneTime += commonUtil.getDistanceHour(startTime, now);
                    }else{
                        doneTime += durationHour;
                    }
                }

                if(startDate.before(startTime) && endDate.after(startTime)){
                    OrderOccupyVo subOrderOccupy = new OrderOccupyVo();
                    subOrderOccupy.setId(id);
                    subOrderOccupy.setNumber(orderId);
                    //暂时没有获取工艺的方法
                    subOrderOccupy.setText("");
                    //产品名称（数据表里暂时没有这一项）
                    subOrderOccupy.setName(orderPo.getItemId());
                    subOrderOccupy.setDeal_date(format.format(startTime));
                    subOrderOccupy.setExpc_date(format.format(endTime));
                    subOrderOccupy.setParent(parentId);
                    id++;

                    float progress = 0;
                    if(startTime.before(now)){
                        if(now.before(endTime)){
                            progress = (float) commonUtil.getDistanceHour(startTime, now) / durationHour;
                        }else{
                            progress = 1;
                        }
                    }
                    subOrderOccupy.setProgress(progress);
                    orderOccupyVoList.add(subOrderOccupy);
                }
            }

            orderOccupy.setProgress( (float) doneTime / totalTime);
            orderOccupyVoList.add(orderOccupy);
        }

        tasks.put("data", orderOccupyVoList);
        tasks.put("links", links);
        content.put("tasks", tasks);
        content.put("delivery_rate", deliveryCount / orderList.size() + "%");

        return ResponseVO.buildSuccess(content);
    }

}
