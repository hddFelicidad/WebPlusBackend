package com.example.backend.service.impl;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.service.LegacySystemService;
import com.example.backend.service.ResourceService;
import com.example.backend.service.ScheduleService;
import com.example.backend.util.*;
import com.example.backend.vo.ResourceOccupyVo;
import com.example.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    LegacySystemService legacySystemService;
    @Autowired
    OrderFilterUtil orderFilterUtil;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public ResponseVO getResourceOccupyByHour(String date) {
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        try{
            return getResourceOccupy(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getResourceOccupyByDay(Map<String, String> date) {
        String startDate = date.get("start_date") + " 00:00:00";
        String endDate = date.get("end_date") + " 23:59:59";
        try{
            return getResourceOccupy(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ResponseVO getResourceOccupy(String s, String e) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);

        List<ResourceOccupyVo> resourceOccupyVoList = new ArrayList<>();
        //获取在起止时间内的排程订单
        List<ScheduleOutputDto.Order> orderList = orderFilterUtil.getOrderByDate(scheduleService.tryGetScheduleOutput(),
                startDate, endDate);
        //一些初始化工作
        List<GroupPo> groupPoList = groupRepository.findAll();
        List<MachinePo> machinePoList = machineRepository.findAll();
        ArrayList<String> groupIdList = new ArrayList<>();              //所有人力资源Id
        ArrayList<Integer> groupOccupyHourList = new ArrayList<>();     //人力资源当天被占用的时间
        ArrayList<String> groupFirstOccupyTime = new ArrayList<>();     //人力资源当天第一次被占用的时间
        ArrayList<String> groupLastOccupyTime = new ArrayList<>();      //人力资源当天最后一次被占用的时间
        ArrayList<String> machineIdList = new ArrayList<>();
        ArrayList<Integer> machineOccupyHourList = new ArrayList<>();
        ArrayList<String> machineFirstOccupyTime = new ArrayList<>();
        ArrayList<String> machineLastOccupyTime = new ArrayList<>();
        for(GroupPo group: groupPoList){
            groupIdList.add(String.valueOf(group.getGroupId()));
            groupOccupyHourList.add(0);
            groupFirstOccupyTime.add("");
            groupLastOccupyTime.add("");
        }
        for(MachinePo machine: machinePoList){
            machineIdList.add(String.valueOf(machine.getId()));
            machineOccupyHourList.add(0);
            machineFirstOccupyTime.add("");
            machineLastOccupyTime.add("");
        }
        //人力资源对象开始id
        int groupIdBegin = 1;
        //机器资源对象开始id
        int machineIdBegin = groupIdList.size() + 1;
        //产品对象开始id
        int productIdBegin = groupIdList.size() + machineIdList.size() + 1;

        for(ScheduleOutputDto.Order order: orderList){
            //子订单列表
            List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
            //产品id
            String itemId = orderRepository.findOrderPoByOrderId(order.getId()).getItemId();
            //产品名称（数据表里暂时没有这一项）
            String itemName = orderRepository.findOrderPoByOrderId(order.getId()).getItemId();
            //为该产品随机生成一个颜色（延期为红色）
            String color = checkForDelay(order);

            for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                //判断子订单是否在起止时间内
                if(startDate.before(subOrder.getStartTime()) && endDate.after(subOrder.getStartTime())){
                    //子订单开始时间
                    Date start_date = subOrder.getStartTime();
                    //子订单持续时间
                    int durationTime = subOrder.getDurationTimeInHour();
                    //子订单结束时间
                    Date end_date = commonUtil.addHour(start_date, durationTime);
                    //子订单占用人力资源Id
                    String groupId = subOrder.getGroupId();
                    //子订单占用机器资源Id
                    String machineId = subOrder.getMachineId();
                    //获取人力资源、机器资源对应下标
                    int groupIndex = groupIdList.indexOf(groupId);
                    int machineIndex = machineIdList.indexOf(machineId);
                    //更新对应人力资源占用时间
                    groupOccupyHourList.set(groupIndex, groupOccupyHourList.get(groupIndex) + durationTime);
                    //更新对应人力资源第一次被占用的时间
                    if(groupFirstOccupyTime.get(groupIndex).equals("")){
                        groupFirstOccupyTime.set(groupIndex, simpleDateFormat.format(start_date));
                    }else{
                        if(start_date.before(simpleDateFormat.parse(groupFirstOccupyTime.get(groupIndex)))){
                            groupFirstOccupyTime.set(groupIndex, simpleDateFormat.format(start_date));
                        }
                    }
                    //更新对应人力资源最后一次被占用的时间
                    if(groupLastOccupyTime.get(groupIndex).equals("")){
                        groupLastOccupyTime.set(groupIndex, simpleDateFormat.format(end_date));
                    }else{
                        if(end_date.after(simpleDateFormat.parse(groupLastOccupyTime.get(groupIndex)))){
                            groupLastOccupyTime.set(groupIndex, simpleDateFormat.format(end_date));
                        }
                    }
                    //更新对应机器资源占用时间
                    machineOccupyHourList.set(machineIndex, machineOccupyHourList.get(machineIndex) + durationTime);
                    //更新对应机器资源第一次被占用的时间
                    if(machineFirstOccupyTime.get(machineIndex).equals("")){
                        machineFirstOccupyTime.set(machineIndex, simpleDateFormat.format(start_date));
                    }else{
                        if(start_date.before(simpleDateFormat.parse(machineFirstOccupyTime.get(machineIndex)))){
                            machineFirstOccupyTime.set(machineIndex, simpleDateFormat.format(start_date));
                        }
                    }
                    //更新对应机器资源最后一次被占用的时间
                    if(machineLastOccupyTime.get(machineIndex).equals("")){
                        machineLastOccupyTime.set(machineIndex, simpleDateFormat.format(end_date));
                    }else{
                        if(end_date.after(simpleDateFormat.parse(groupLastOccupyTime.get(groupIndex)))){
                            machineLastOccupyTime.set(machineIndex, simpleDateFormat.format(end_date));
                        }
                    }

                    ResourceOccupyVo productGroupOccupy = new ResourceOccupyVo(productIdBegin, "", "",
                            simpleDateFormat.format(start_date), String.valueOf(durationTime * 60),
                            itemName, color, itemId, groupIndex + groupIdBegin);
                    ResourceOccupyVo productMachineOccupy = new ResourceOccupyVo(productIdBegin + 1, "", "",
                            simpleDateFormat.format(start_date), String.valueOf(durationTime * 60),
                            itemName, color, itemId, machineIndex + machineIdBegin);
                    productIdBegin += 2;
                    resourceOccupyVoList.add(productGroupOccupy);
                    resourceOccupyVoList.add(productMachineOccupy);
                }
            }
        }

        for(int i = 0; i < groupIdList.size(); i++){
            String groupId = groupIdList.get(i);
            String groupName = groupRepository.findGroupPoByGroupId(Integer.parseInt(groupId)).getGroupName();
            String percent = Math.min(groupOccupyHourList.get(i), 24) / 24 * 100 + "%";
            String start = groupFirstOccupyTime.get(i);
            int duration = commonUtil.getDistanceHour(
                    simpleDateFormat.parse(groupFirstOccupyTime.get(i)),
                    simpleDateFormat.parse(groupLastOccupyTime.get(i)));
            duration = Math.min(duration, 24);
            ResourceOccupyVo groupOccupy = new ResourceOccupyVo();
            groupOccupy.setId(groupIdBegin + i);
            groupOccupy.setResource(groupName);
            groupOccupy.setPercent(percent);
            groupOccupy.setStart_date(start);
            groupOccupy.setDuration(String.valueOf(60 * duration));
            groupOccupy.setText("");
            groupOccupy.setColor("darkturquoise");
            groupOccupy.setProduct_id("");
            resourceOccupyVoList.add(groupOccupy);
        }

        for(int j = 0; j < machineIdList.size(); j++){
            String machineId = machineIdList.get(j);
            String machineName = machineRepository.findMachinePoByMachineId(Integer.parseInt(machineId)).getMachineName();
            String percent = Math.min(machineOccupyHourList.get(j), 24) / 24 * 100 + "%";
            String start = machineFirstOccupyTime.get(j);
            int duration = commonUtil.getDistanceHour(
                    simpleDateFormat.parse(machineFirstOccupyTime.get(j)),
                    simpleDateFormat.parse(machineLastOccupyTime.get(j)));
            duration = Math.min(duration, 24);
            ResourceOccupyVo machineOccupy = new ResourceOccupyVo();
            machineOccupy.setId(machineIdBegin + j);
            machineOccupy.setResource(machineName);
            machineOccupy.setPercent(percent);
            machineOccupy.setStart_date(start);
            machineOccupy.setDuration(String.valueOf(60 * duration));
            machineOccupy.setText("");
            machineOccupy.setColor("darkturquoise");
            machineOccupy.setProduct_id("");
            resourceOccupyVoList.add(machineOccupy);
        }
        return ResponseVO.buildSuccess(resourceOccupyVoList);
    }

    public String checkForDelay(ScheduleOutputDto.Order order){
        String id = order.getId();
        //判断最后一个子订单是否超期
        ScheduleOutputDto.SubOrder lastSubOrder = order.getSubOrders().get(order.getSubOrders().size() - 1);
        Date dueTime = orderRepository.findOrderPoByOrderId(id).getDeadLine();  //DDL
        Date endTime = commonUtil.addHour(lastSubOrder.getStartTime(), lastSubOrder.getDurationTimeInHour());
        if(endTime.after(dueTime)){
            return "red";
        }else{
            String randomColor = commonUtil.getColor();
            while (randomColor.equals("#FF0000") || randomColor.equals("#00CED1")){
                randomColor = commonUtil.getColor();
            }
            return randomColor;
        }
    }

}
