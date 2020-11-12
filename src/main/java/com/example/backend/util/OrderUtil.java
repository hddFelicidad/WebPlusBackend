package com.example.backend.util;

import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderUtil {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CommonUtil commonUtil;

    /**
     * 根据开始时间和结束时间筛选排程订单
     * @param originalOrderList 原始排程数据
     * @param startDate 开始时间按
     * @param endDate 结束时间
     * @return
     */
    public List<ScheduleOutputDto.Order> getOrderByDate(List<ScheduleOutputDto.Order> originalOrderList, Date startDate, Date endDate){
        if(originalOrderList.size() > 0){
            List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
//            List<ScheduleOutputDto.Order> tmpOrderList = orderRemake(originalOrderList);
            orderResort(originalOrderList);
            for(ScheduleOutputDto.Order order: originalOrderList){
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

    public List<ScheduleOutputDto.Order> getOrderDeliverByDate(List<ScheduleOutputDto.Order> originalOrderList, Date endDate){
        if(originalOrderList.size() > 0){
            List<ScheduleOutputDto.Order> targetOrderList = new ArrayList<>();
            List<ScheduleOutputDto.Order> tmpOrderList = orderRemake(originalOrderList);
            for(ScheduleOutputDto.Order order: tmpOrderList){
                String orderId = order.getId();
                OrderPo orderPo = orderRepository.findOrderPoByOrderId(orderId);
                if(!orderPo.getDeadLine().after(endDate))
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
        orderResort(originalOrderList);
        for(ScheduleOutputDto.Order originalOrder: originalOrderList){
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
                    ScheduleOutputDto.SubOrder newSubOrder = new ScheduleOutputDto.SubOrder(originalOrder.getId() + "_" + subOrderId,
                            current.getStartTime(), current.getDurationTimeInHour(), current.getGroupIdList(), current.getMachineId());
                    targetSubOrderList.add(newSubOrder);
                }
                subOrderId++;
            }
            ScheduleOutputDto.Order newOrder = new ScheduleOutputDto.Order(originalOrder.getId(), targetSubOrderList);
            targetOrderList.add(newOrder);
        }
        return targetOrderList;
    }

    /**
     * 对子订单按开始时间进行进行排序
     * @param originalOrderList
     * @return
     */
    public void orderResort(List<ScheduleOutputDto.Order> originalOrderList){
        for(ScheduleOutputDto.Order originalOrder: originalOrderList){
            List<ScheduleOutputDto.SubOrder> originalSubOrderList = originalOrder.getSubOrders();
            originalSubOrderList.sort(Comparator.comparing(ScheduleOutputDto.SubOrder::getStartTime));

        }
    }
}
