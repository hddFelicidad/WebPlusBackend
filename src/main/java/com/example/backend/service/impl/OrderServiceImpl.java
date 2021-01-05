package com.example.backend.service.impl;

import com.example.backend.data.BomRepository;
import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.BomPo;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.OrderService;
import com.example.backend.service.ScheduleService;
import com.example.backend.util.CommonUtil;
import com.example.backend.util.OrderUtil;
import com.example.backend.vo.*;
import lombok.var;
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
    ScheduleInitServiceImpl scheduleInitService;
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
    @Autowired
    BomRepository bomRepository;


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
        List<ScheduleOutputDto.Order> orderScheduleList = scheduleService.tryGetScheduleOutput().getOrders();
        orderUtil.orderResort(orderScheduleList);
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

        List<ScheduleOutputDto.Order> orderScheduleList = scheduleService.tryGetScheduleOutput().getOrders();
        orderUtil.orderResort(orderScheduleList);
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

    @Override
    public ResponseVO getAllProduct() {
        var bomPoList = bomRepository.findAll();
        if(!bomPoList.isEmpty()){
            List<String> productList = new ArrayList<>();
            for(BomPo bomPo: bomPoList){
                String productId = bomPo.getBomId();
                if(productList.indexOf(productId) == -1){
                    productList.add(productId);
                }
            }
            return ResponseVO.buildSuccess(productList);
        }
        return ResponseVO.buildFailure();
    }

    @Override
    public ResponseVO insertUrgentOrder(UrgentOrderVo urgentOrder) {
        try{
            String itemId = urgentOrder.getProductId();
            int itemCount = Integer.parseInt(urgentOrder.getQuantity());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date deadline = simpleDateFormat.parse(urgentOrder.getDate());

            Random random = new Random();
            int orderId = random.nextInt(200000) + 800000;
            OrderPo order = new OrderPo(String.valueOf(orderId), itemId, itemCount, deadline);
            OrderPo newOrder = orderRepository.save(order);

            ScheduleInputDto scheduleInputDto = scheduleInitService.getScheduleInput();
            Date insertTime = new Date();
            List<ScheduleInputDto.Order> orderList = createUrgentOrder(newOrder);
            scheduleService.scheduleInsertUrgentOrder(scheduleInputDto, insertTime, orderList.get(0));

            return ResponseVO.buildSuccess(newOrder.getOrderId());
        }catch (ParseException e){
            e.printStackTrace();
        }

        return ResponseVO.buildFailure();
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
//        List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderDeliverByDate(scheduleService.tryGetScheduleOutput().getOrders(),
//                endDate);
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

        List<String> currentAllOrderIdList = new ArrayList<>();
        List<List<ScheduleOutputDto.Order>> currentAllOrderList = new ArrayList<>();

        for(ScheduleOutputDto.Order order: orderList){
            String orderId = order.getId().substring(0, order.getId().indexOf("-"));
            if(order.getRequiredOrderId() == null){
                currentAllOrderIdList.add(orderId);
                List<ScheduleOutputDto.Order> tmp = new ArrayList<>();
                tmp.add(order);
                currentAllOrderList.add(tmp);
            }else{
                int index = currentAllOrderIdList.indexOf(orderId);
                currentAllOrderList.get(index).add(order);
            }
        }

        for(int i = 0; i < currentAllOrderIdList.size(); i++){
            List<ScheduleOutputDto.Order> tmp = currentAllOrderList.get(i);
            String orderId = currentAllOrderIdList.get(i);
            OrderPo orderPo = orderRepository.findOrderPoByOrderId(orderId);
            String deadLine = simpleDateFormat.format(orderPo.getDeadLine());

            OrderOccupyVo orderOccupy = new OrderOccupyVo();
            orderOccupy.setId(id);
            orderOccupy.setNumber(orderId);
            //TODO
            orderOccupy.setText("");
            orderOccupy.setName("产品" + orderPo.getItemId());
            orderOccupy.setDeal_date(deadLine);
            //父任务数据项id
            int parentId = id;
            id++;
            //最后一个工艺的最后一个子订单的结束时间
            List<ScheduleOutputDto.SubOrder> tmpSubOrderList = tmp.get(tmp.size() - 1).getSubOrders();
            Date dealDate = tmpSubOrderList.get(tmpSubOrderList.size() - 1).getEndTime();
            orderOccupy.setExpc_date(simpleDateFormat.format(dealDate));

            //判断此订单准时交付
            if(!dealDate.after(orderPo.getDeadLine()))
                deliveryCount++;

            String allProcess = "";
            int allDoneTime = 0;
            int allTotalTime = 0;
            for(ScheduleOutputDto.Order order: tmp){
                String process = order.getId().substring(order.getId().indexOf("-") + 1);
                allProcess += process + ",";

                List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
                Date ddl = subOrderList.get(subOrderList.size() - 1).getEndTime();

                int doneTime = 0;
                int totalTime = 0;
                for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                    int durationHour = subOrder.getDurationTimeInHour();
                    Date startTime = subOrder.getStartTime();
                    Date endTime = subOrder.getEndTime();
                    totalTime += durationHour;
                    if(startTime.before(endDate)){
                        if(endTime.after(endDate)){
                            doneTime += commonUtil.getDistanceHour(startTime, commonUtil.addStartHour(startDate, 24));
                        }else{
                            doneTime += durationHour;
                        }
                    }
                }
                allDoneTime += doneTime;
                allTotalTime += totalTime;
                OrderOccupyVo subOrderOccupy = new OrderOccupyVo();
                subOrderOccupy.setId(id);
                subOrderOccupy.setNumber(orderId);
                subOrderOccupy.setText(process);
                subOrderOccupy.setName("产品" + orderPo.getItemId());
                subOrderOccupy.setDeal_date(deadLine);
                subOrderOccupy.setExpc_date(simpleDateFormat.format(ddl));
                subOrderOccupy.setParent(parentId);
                id++;
                subOrderOccupy.setProgress((float) doneTime / totalTime);
                orderOccupyVoList.add(subOrderOccupy);
            }

            orderOccupy.setText(allProcess.substring(0, allProcess.length()-1));
            orderOccupy.setProgress((float) allDoneTime/allTotalTime);
            orderOccupyVoList.add(orderOccupy);
        }

        tasks.put("data", orderOccupyVoList);
        tasks.put("links", links);
        content.put("tasks", tasks);
        content.put("delivery_rate", deliveryCount * 100 / currentAllOrderIdList.size());

        return ResponseVO.buildSuccess(content);
    }

    public List<ScheduleInputDto.Order> createUrgentOrder(OrderPo orderPo){
        List<ScheduleInputDto.Order> orderList = new ArrayList<>();
        String orderId = orderPo.getOrderId();
        String orderName = "订单" + orderId;
        Date ddl = orderPo.getDeadLine();

        String itemId = orderPo.getItemId();
        int itemCount = orderPo.getItemCount();

        var bomPoList = bomRepository.findBomPosByBomId(itemId);
        if(!bomPoList.isEmpty()){
            for(int i = 0; i < bomPoList.size(); i++){
                BomPo bomPo = bomPoList.get(i);
                String process = bomPo.getProcess();
                int standardOutput = Integer.parseInt(bomPo.getStandardOutput());
                int workCount = bomPo.getWorkerCount();
                List<String> groupResourceList = bomPo.getGroupResourceList();
                List<String> lineResourceList = bomPo.getMachineResourceList();

                int needHour = itemCount / standardOutput;

                List<String> availableGroupList = new ArrayList<>();
                for (String groupName : groupResourceList) {
                    GroupPo groupPo = groupRepository.findGroupPoByGroupName(groupName);
                    if (groupPo != null)
                        availableGroupList.add(groupPo.getGroupId());
                }
                List<String> availableMachineList = new ArrayList<>();
                for (String machineName : lineResourceList) {
                    List<MachinePo> machinePoList = machineRepository.findMachinePosByMachineName(machineName);
                    if (!machinePoList.isEmpty())
                        availableMachineList.add(machinePoList.get(0).getMachineId());
                }
                String requiredOrderId = null;
                if(i > 0){
                    requiredOrderId = orderId + "-" + bomPoList.get(i - 1).getProcess();
                }

                ScheduleInputDto.Order order = new ScheduleInputDto.Order(orderId + "-" + process, orderName + "-" + process, false, needHour,
                        workCount, new HashSet<>(availableGroupList), new HashSet<>(availableMachineList), ddl, requiredOrderId);
                orderList.add(order);
            }
        }

        return orderList;
    }
}
