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
    @Autowired
    CommonUtil commonUtil;

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
            List<ScheduleOutputDto.Order> tmpOrderList = orderRemake(originalOrderList);
            for(ScheduleOutputDto.Order order: tmpOrderList){
                List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
                //订单在当前日期是否有子订单在进行
                boolean within = false;
                for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                    Date startTime = subOrder.getStartTime();
                    if(!(startTime.before(startDate) || startTime.after(endDate))){
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
     * 将跨天子订单拆开
     * @param originalOrderList
     * @return
     */
    public List<ScheduleOutputDto.Order> orderRemake(List<ScheduleOutputDto.Order> originalOrderList){
        List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
        List<ScheduleOutputDto.Order> resortOrderList = orderResort(originalOrderList);
        for(ScheduleOutputDto.Order originalOrder: resortOrderList){
            int subOrderId = 1;
            List<ScheduleOutputDto.SubOrder> originalSubOrderList = originalOrder.getSubOrders();
            List<ScheduleOutputDto.SubOrder> targetSubOrderList = new ArrayList<>();
            for (ScheduleOutputDto.SubOrder current : originalSubOrderList) {
                if (!commonUtil.isDifferentDay(current.getStartTime(), current.getEndTime())) {
                    Date splitDate = commonUtil.getStartOfDay(current.getEndTime());
                    ScheduleOutputDto.SubOrder firstNewSubOrder = new ScheduleOutputDto.SubOrder(originalOrder.getId() + "_" + subOrderId,
                            current.getStartTime(), commonUtil.getDistanceHour(current.getStartTime(), splitDate),
                            current.getGroupIdList(), current.getMachineId());
                    subOrderId++;
                    ScheduleOutputDto.SubOrder secondNewSubOrder = new ScheduleOutputDto.SubOrder(originalOrder.getId() + "_" + subOrderId,
                            splitDate, commonUtil.getDistanceHour(splitDate, current.getEndTime()),
                            current.getGroupIdList(), current.getMachineId());
                    targetSubOrderList.add(firstNewSubOrder);
                    targetSubOrderList.add(secondNewSubOrder);
                }
                else{
                    current.setId(originalOrder.getId() + "_" + subOrderId);
                    targetSubOrderList.add(current);
                }
                subOrderId++;
            }
            ScheduleOutputDto.Order newOrder = new ScheduleOutputDto.Order(originalOrder.getId(), targetSubOrderList);
            targetOrderList.add(newOrder);
        }
        return targetOrderList;
    }

    /**
     * 对子订单进行排序
     * @param originalOrderList
     * @return
     */
    public List<ScheduleOutputDto.Order> orderResort(List<ScheduleOutputDto.Order> originalOrderList){
        List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
        for(ScheduleOutputDto.Order originalOrder: originalOrderList){
            List<ScheduleOutputDto.SubOrder> originalSubOrderList = originalOrder.getSubOrders();
            List<ScheduleOutputDto.SubOrder> targetSubOrderList = new ArrayList<>();
            for(int i = 0; i < originalSubOrderList.size(); i++){
                targetSubOrderList.add(new ScheduleOutputDto.SubOrder());
            }
            for(ScheduleOutputDto.SubOrder originalSubOrder: originalSubOrderList){
                String id = originalSubOrder.getId();
                int subOrderId = Integer.parseInt(id.substring(id.indexOf("_") + 1));
                targetSubOrderList.set(subOrderId - 1, originalSubOrder);
            }
            ScheduleOutputDto.Order targetOrder = new
                    ScheduleOutputDto.Order(originalOrder.getId(), targetSubOrderList);
            targetOrderList.add(targetOrder);
        }
        return targetOrderList;
    }
}
