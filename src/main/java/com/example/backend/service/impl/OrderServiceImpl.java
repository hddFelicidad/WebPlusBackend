package com.example.backend.service.impl;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.OrderService;
import com.example.backend.service.ScheduleService;
import com.example.backend.util.CommonUtil;
import com.example.backend.util.OrderUtil;
import com.example.backend.vo.OrderOccupyVo;
import com.example.backend.vo.OrderPlanVo;
import com.example.backend.vo.OrderProductionVo;
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
    OrderUtil orderUtil;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MachineRepository machineRepository;


    @Override
    public ResponseVO getOrderOccupy(String date) {
        String start = date + " 00:00:00";
        String end = date + " 23:59:59";
        try{
            return getOrderOccupy(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseVO.buildFailure();
    }

    @Override
    public ResponseVO getProductOccupyByHour(String productId, String date) {
        String start = date + " 00:00:00";
        String end = date + " 23:59:59";
        try{
            return getProductOccupy(start, end, productId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getProductOccupyByDay(String productId, Map<String, String> date) {
        String start = date.get("start_date") + " 00:00:00";
        String end = date.get("end_date") + " 23:59:59";
        try{
            return getProductOccupy(start, end, productId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getOrderPlan() {
        if(scheduleService.tryGetScheduleOutput() == null){
            return ResponseVO.buildFailure("排程暂未完成！");
        }

        List<ScheduleOutputDto.Order> orderScheduleList = orderUtil.orderResort(scheduleService.tryGetScheduleOutput().getOrders());
        List<OrderPlanVo> orderPlanVoList = new ArrayList<>();
        for(ScheduleOutputDto.Order eachOrder: orderScheduleList){
            List<ScheduleOutputDto.SubOrder> subOrderList = eachOrder.getSubOrders();
            List<OrderPlanVo.SubOrderPlanVo> subOrderPlanVoList = new ArrayList<>();
            for(ScheduleOutputDto.SubOrder eachSubOrder: subOrderList){
                OrderPlanVo.SubOrderPlanVo subOrderPlanVo = new OrderPlanVo.SubOrderPlanVo(eachSubOrder.getId(),
                        eachSubOrder.getStartTime(), eachSubOrder.getEndTime());
                subOrderPlanVoList.add(subOrderPlanVo);
            }
            OrderPlanVo orderPlanVo = new OrderPlanVo(eachOrder.getId(), subOrderPlanVoList);
            orderPlanVoList.add(orderPlanVo);
        }
        return ResponseVO.buildSuccess(orderPlanVoList);
    }

    @Override
    public ResponseVO getOrderPlanProduction() {
        if(scheduleService.tryGetScheduleOutput() == null){
            return ResponseVO.buildFailure("排程暂未完成！");
        }

        List<ScheduleOutputDto.Order> orderScheduleList = orderUtil.orderResort(scheduleService.tryGetScheduleOutput().getOrders());
        List<OrderProductionVo> orderProductionVoList = new ArrayList<>();
        for(ScheduleOutputDto.Order eachOrder: orderScheduleList) {
            List<ScheduleOutputDto.SubOrder> subOrderList = eachOrder.getSubOrders();
            List<OrderProductionVo.SubOrderProductionVo> subOrderProductionVoList = new ArrayList<>();
            for(ScheduleOutputDto.SubOrder eachSubOrder: subOrderList){
                List<OrderProductionVo.ProductionResourceVo> productionResourceVoList = new ArrayList<>();
                List<OrderProductionVo.ProductionTaskVo> productionTaskVoList = new ArrayList<>();
                List<String> groupIdList = eachSubOrder.getGroupIdList();
                String machineId = eachSubOrder.getMachineId();
                Date startTime = eachSubOrder.getStartTime();
                Date endTime = eachSubOrder.getEndTime();
                for(String groupId: groupIdList){
                    GroupPo groupPo = groupRepository.findGroupPoByGroupId(groupId);
                    OrderProductionVo.ProductionResourceVo groupResource = new
                            OrderProductionVo.ProductionResourceVo("hr" + groupId, groupPo.getGroupName());
                    OrderProductionVo.ProductionTaskVo groupTask = new
                            OrderProductionVo.ProductionTaskVo("hr" + groupId, groupPo.getGroupName(),
                            startTime, endTime);
                    productionResourceVoList.add(groupResource);
                    productionTaskVoList.add(groupTask);
                }
                MachinePo machinePo = machineRepository.findMachinePoById(Integer.parseInt(machineId));
                OrderProductionVo.ProductionResourceVo machineResource = new
                        OrderProductionVo.ProductionResourceVo("ln" + machineId, machinePo.getMachineName());
                OrderProductionVo.ProductionTaskVo machineTask = new
                        OrderProductionVo.ProductionTaskVo("ln" + machineId, machinePo.getMachineName(),
                        startTime, endTime);
                productionResourceVoList.add(machineResource);
                productionTaskVoList.add(machineTask);
                OrderProductionVo.SubOrderProductionVo subOrderProductionVo = new
                        OrderProductionVo.SubOrderProductionVo(eachSubOrder.getId(),
                        productionResourceVoList, productionTaskVoList);
                subOrderProductionVoList.add(subOrderProductionVo);
            }
            OrderProductionVo orderProductionVo = new OrderProductionVo(eachOrder.getId(),
                    subOrderProductionVoList);
            orderProductionVoList.add(orderProductionVo);
        }
        return ResponseVO.buildSuccess(orderProductionVoList);
    }

    /**
     * 获取产品甘特图
     * @param s 开始时间，格式为2020-11-5 00：00：00
     * @param e 结束时间，格式同上
     * @param productId 产品id
     * @return
     * @throws ParseException
     */
    public ResponseVO getProductOccupy(String s, String e, String productId) throws ParseException {
        if(scheduleService.tryGetScheduleOutput() == null){
            return ResponseVO.buildFailure("排程暂未完成！");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);
        List<ScheduleOutputDto.Order> orderListByProductId = orderUtil.getOrderByProductId(scheduleService.tryGetScheduleOutput().getOrders(), productId);
        if(orderListByProductId.size() == 0){
            return ResponseVO.buildFailure("没有该产品对应的订单");
        }
        List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderByDate(orderListByProductId, startDate, endDate);
        if(orderList.size() == 0){
            return ResponseVO.buildFailure("起止时间内无正在处理的订单");
        }

        Map<String, Object> content = new HashMap<>();
        //数据表里暂时没有产品名称
        content.put("product_name", "产品" + productId);
        Map<String, Object> tasks = new HashMap<>();
        content.put("tasks", tasks);

        ArrayList<Map<String, Integer>> linkList = new ArrayList<>();
        int linkId = 1;
        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        int dataId = 1;
        ArrayList<ArrayList<Integer>> groupLinkList = new ArrayList<>();
        ArrayList<Integer> machineLinkList = new ArrayList<>();

        for(ScheduleOutputDto.Order order: orderList){
            List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
            for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                int durationHour = subOrder.getDurationTimeInHour();
                Date startTime = subOrder.getStartTime();
                if(!(startDate.after(startTime) || endDate.before(startTime))){
                    Map<String, Object> machineData = new HashMap<>();
                    List<String> occupyGroupIdList = subOrder.getGroupIdList();
                    String occupyMachineId = subOrder.getMachineId();

                    ArrayList<Integer> groupTmp = new ArrayList<>();
                    for(String occupyGroupId: occupyGroupIdList){
                        Map<String, Object> groupData = new HashMap<>();
                        String groupName = groupRepository.findGroupPoByGroupId(occupyGroupId).getGroupName();
                        groupData.put("id", dataId);
                        groupData.put("resource", groupName);
                        groupData.put("start_date", simpleDateFormat.format(startTime));
                        groupData.put("duration", durationHour * 60);
                        groupTmp.add(dataId);
                        dataId++;
                        dataList.add(groupData);
                    }
                    groupLinkList.add(groupTmp);

                    String machineName = machineRepository.findMachinePoById(Integer.parseInt(occupyMachineId)).getMachineName();
                    machineData.put("id", dataId);
                    machineData.put("resource", machineName);
                    machineData.put("start_date", simpleDateFormat.format(startTime));
                    machineData.put("duration", durationHour * 60);
                    machineLinkList.add(dataId);
                    dataId++;

                    dataList.add(machineData);
                }

            }
            for(int i = 0; i < groupLinkList.size() - 1; i++){
                ArrayList<Integer> groupLink = groupLinkList.get(i);
                for(Integer current: groupLink){
                    for(Integer target: groupLinkList.get(i + 1)){
                        Map<String, Integer> link = new HashMap<>();
                        link.put("id", linkId);
                        link.put("source", current);
                        link.put("target", target);
                        linkId++;
                        linkList.add(link);
                    }
                }
            }
            for(int j = 0; j< machineLinkList.size() - 1; j++){
                Map<String, Integer> link = new HashMap<>();
                link.put("id", linkId);
                link.put("source", machineLinkList.get(j));
                link.put("target", machineLinkList.get(j + 1));
                linkId++;
                linkList.add(link);
            }
            groupLinkList.clear();
            machineLinkList.clear();
        }

        tasks.put("data", dataList);
        tasks.put("links", linkList);
        return ResponseVO.buildSuccess(content);
    }

    /**
     * 获取订单甘特图
     * @param s 开始时间，格式为2020-11-5 00：00：00
     * @param e 结束时间，格式同上
     * @return
     * @throws ParseException
     */
    public ResponseVO getOrderOccupy(String s, String e) throws ParseException {
        if(scheduleService.tryGetScheduleOutput() == null){
            return ResponseVO.buildFailure("排程暂未完成！");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);
        //获取在起止时间内的排程订单
        List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderByDate(scheduleService.tryGetScheduleOutput().getOrders(),
                startDate, endDate);
        if(orderList.size() == 0){
            return ResponseVO.buildFailure("起止时间内无正在处理的订单");
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
            orderOccupy.setText("装配");
            //产品名称（数据表里暂时没有这一项）
            orderOccupy.setName("产品" + orderPo.getItemId());
            orderOccupy.setDeal_date(deadLine);
            //父任务数据项id
            int parentId = id;
            id++;
            List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
            //预计交期（最后一个子订单的结束时间）
            Date dealDate = subOrderList.get(subOrderList.size() - 1).getEndTime();
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
                Date endTime = subOrder.getEndTime();
                totalTime += durationHour;
                if(startTime.before(now)){
                    if(now.before(endTime)){
                        doneTime += commonUtil.getDistanceHour(startTime, now);
                    }else{
                        doneTime += durationHour;
                    }
                }
            }
            OrderOccupyVo subOrderOccupy = new OrderOccupyVo();
            subOrderOccupy.setId(id);
            subOrderOccupy.setNumber(orderId);
            //暂时没有获取工艺的方法
            subOrderOccupy.setText("装配");
            //产品名称（数据表里暂时没有这一项）
            subOrderOccupy.setName("产品" + orderPo.getItemId());
            subOrderOccupy.setDeal_date(deadLine);
            subOrderOccupy.setExpc_date(format.format(dealDate));
            subOrderOccupy.setParent(parentId);
            id++;
            subOrderOccupy.setProgress((float) doneTime / totalTime);
            orderOccupyVoList.add(subOrderOccupy);


            orderOccupy.setProgress((float) doneTime / totalTime);
            orderOccupyVoList.add(orderOccupy);
        }

        tasks.put("data", orderOccupyVoList);
        tasks.put("links", links);
        content.put("tasks", tasks);
        content.put("delivery_rate", deliveryCount * 100 / orderList.size());

        return ResponseVO.buildSuccess(content);
    }

}
