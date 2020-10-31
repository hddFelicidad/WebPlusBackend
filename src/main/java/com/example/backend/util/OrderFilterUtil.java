package com.example.backend.util;

import com.example.backend.dto.ScheduleOutputDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderFilterUtil {

    /**
     * 根据开始时间和结束时间筛选排程订单
     * @param out
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ScheduleOutputDto.Order> getOrderByDate(ScheduleOutputDto out, Date startDate, Date endDate){
        List<ScheduleOutputDto.Order> originalOrderList = out.getOrders();
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
}
