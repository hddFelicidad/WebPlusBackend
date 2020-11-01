package com.example.backend.util;

import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderUtil {
    @Autowired
    OrderRepository orderRepository;

    /**
     * 根据开始时间和结束时间筛选排程订单
     * @param originalOrderList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ScheduleOutputDto.Order> getOrderByDate(List<ScheduleOutputDto.Order> originalOrderList, Date startDate, Date endDate){
        if(originalOrderList.size() > 0){
            List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
            for(ScheduleOutputDto.Order order: originalOrderList){
                List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
                //订单在当前日期是否有子订单在进行
                boolean within = false;
                for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                    Date startTime = subOrder.getStartTime();
                    if(startTime.after(startDate) && startTime.before(endDate)){
                        within = true;
                        break;
                    }
                }
                if(within)
                    targetOrderList.add(order);
            }
            return targetOrderList;
        }
        return null;
    }

    public List<ScheduleOutputDto.Order> getOrderByProductId(List<ScheduleOutputDto.Order> originalOrderList, String productId){
        if(originalOrderList.size() > 0){
            List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
            for(ScheduleOutputDto.Order order: originalOrderList){
                String orderId = order.getId();
                String itemId = orderRepository.findOrderPoByOrderId(orderId).getItemId();
                if(itemId.equals(productId))
                    targetOrderList.add(order);
            }
            return targetOrderList;
        }
        return null;
    }

    /**
     * TODO
     * @param originalOrderList
     * @return
     */
    public List<ScheduleOutputDto.Order> orderRemake(List<ScheduleOutputDto.Order> originalOrderList){
        List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
        for(ScheduleOutputDto.Order originalOrder: originalOrderList){
            List<ScheduleOutputDto.SubOrder> originalSubOrderList = originalOrder.getSubOrders();
            List<ScheduleOutputDto.SubOrder> targetSubOrderList = new ArrayList<>();

        }
        return targetOrderList;
    }
}
