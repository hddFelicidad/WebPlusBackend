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
import com.example.backend.vo.*;
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
    OrderUtil orderUtil;
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

    @Override
    public ResponseVO getResourceOccupyInfo(String date) {
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        try{
            return getResourceOccupyInfo(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getResourceLoadByDay(String startDate, String endDate) {
        startDate += " 00:00:00";
        endDate += " 00:00:00";
        try{
            return getResourceLoad(startDate, endDate, "day");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getResourceLoadByMonth(String startDate, String endDate) {
        startDate += "-1 00:00:00";
        endDate += "-1 00:00:00";
        try{
            return getResourceLoad(startDate, endDate, "month");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseVO getResourceInfo() {
        List<ResourceGetVo> groupList = new ArrayList<>();
        List<ResourceGetVo> machineList = new ArrayList<>();
        List<GroupPo> groupPoList = groupRepository.findAll();
        List<MachinePo> machinePoList = machineRepository.findAll();
        List<String> machineIdList = new ArrayList<>();
        List<String> machineNameList = new ArrayList<>();
        List<String> machineDateList = new ArrayList<>();
        List<Integer> machineCountList = new ArrayList<>();

        for(GroupPo groupPo: groupPoList){
            int shift = -1;
            switch (groupPo.getClassName()){
                case "DAY":
                    shift = 2;
                    break;
                case "NIGHT":
                    shift = 3;
                    break;
                case "ALL":
                    shift = 1;
                    break;
                default:
                    break;
            }
            ResourceGetVo groupInfo = new ResourceGetVo("hr" + groupPo.getGroupId(),
                    groupPo.getAddDate(), groupPo.getGroupName(), groupPo.getMemberCount(), shift);
            groupList.add(groupInfo);
        }

        for(MachinePo machinePo: machinePoList){
            String machineId = machinePo.getMachineId();
            if(machineIdList.indexOf(machineId) == -1){
                machineIdList.add(machineId);
                machineNameList.add(machinePo.getMachineName());
                machineDateList.add(machinePo.getAddDate());
                machineCountList.add(1);
            }else{
                machineCountList.set(machineIdList.indexOf(machineId), machineCountList.get(machineIdList.indexOf(machineId)) + 1);
            }
        }
        for(int i = 0; i < machineIdList.size(); i++){
            ResourceGetVo machineInfo = new ResourceGetVo("ln" + machineIdList.get(i),
                    machineDateList.get(i), machineNameList.get(i), machineCountList.get(i), 1);
            machineList.add(machineInfo);
        }

        Map<String, List<ResourceGetVo>> content = new HashMap<>();
        content.put("human", groupList);
        content.put("device", machineList);

        return ResponseVO.buildSuccess(content);
    }

    @Override
    public ResponseVO updateResourceInfo(ResourceUpdateVo resourceInfo) {
        String resourceId = resourceInfo.getId();
        if(resourceId.startsWith("ln")){
            String machineId = resourceId.substring(2);
            List<MachinePo> machinePoList = machineRepository.findMachinePosByMachineId(machineId);
            if(machinePoList.size() > resourceInfo.getNumber()){
                for(int i = resourceInfo.getNumber(); i < machinePoList.size(); i++){
                    machineRepository.delete(machinePoList.get(i));
                }
            }else if(machinePoList.size() == resourceInfo.getNumber()){
                for(MachinePo machinePo: machinePoList){
                    machinePo.setMachineName(resourceInfo.getName());
                    machineRepository.save(machinePo);
                }
            }else{
                for(MachinePo machinePo: machinePoList){
                    machinePo.setMachineName(resourceInfo.getName());
                    machineRepository.save(machinePo);
                }
                for(int i = 0; i < resourceInfo.getNumber()-machinePoList.size(); i++){
                    MachinePo machinePo = new MachinePo(null, resourceInfo.getName(), machineId,
                            machinePoList.get(0).getAddDate());
                    machineRepository.save(machinePo);
                }
            }
            return ResponseVO.buildSuccess();
        }else if(resourceId.startsWith("hr")){
            String groupId = resourceId.substring(2);
            String className = "";
            switch (resourceInfo.getShift()){
                case 1:
                    className = "ALL";
                    break;
                case 2:
                    className = "DAY";
                    break;
                case 3:
                    className = "NIGHT";
                    break;
                default:
                    break;
            }

            GroupPo groupPo = groupRepository.findGroupPoByGroupId(groupId);
            groupPo.setGroupName(resourceInfo.getName());
            groupPo.setMemberCount(resourceInfo.getNumber());
            groupPo.setClassName(className);
            groupRepository.save(groupPo);
            return ResponseVO.buildSuccess();
        }else{
            return ResponseVO.buildFailure();
        }
    }

    @Override
    public ResponseVO addResource(ResourceAddVo resourceInfo) {
        int type = resourceInfo.getType();
        if(type == 1){
            String className = "";
            switch (resourceInfo.getShift()){
                case 1:
                    className = "ALL";
                    break;
                case 2:
                    className = "DAY";
                    break;
                case 3:
                    className = "NIGHT";
                    break;
                default:
                    break;
            }
            GroupPo groupPo = new GroupPo(null, resourceInfo.getName(), "",
                    resourceInfo.getNumber(), className, resourceInfo.getDate());
            GroupPo newGroup = groupRepository.save(groupPo);
            newGroup.setGroupId(String.valueOf(newGroup.getId()));
            groupRepository.save(newGroup);
            Map<String, String> content = new HashMap<>();
            content.put("id", "hr" + newGroup.getGroupId());
            return ResponseVO.buildSuccess(content);
        }else if(type == 2){
            int count = resourceInfo.getNumber();
            int machineId = -1;
            for(int i = 0; i< count; i++){
                MachinePo machinePo = new MachinePo(null, resourceInfo.getName(), "", resourceInfo.getDate());
                MachinePo newMachine = machineRepository.save(machinePo);
                if(machineId == -1)
                    machineId = newMachine.getId();
                newMachine.setMachineId(String.valueOf(machineId));
                machineRepository.save(newMachine);
            }
            Map<String, String> content = new HashMap<>();
            content.put("id", "ln" + machineId);
            return ResponseVO.buildSuccess(content);
        }else {
            return ResponseVO.buildFailure();
        }
    }

    @Override
    public ResponseVO deleteResource(String resourceId) {
        if(resourceId.startsWith("ln")){
            String machineId = resourceId.substring(2);
            List<MachinePo> machinePoList = machineRepository.findMachinePosByMachineId(machineId);
            for(MachinePo machinePo: machinePoList){
                machineRepository.delete(machinePo);
            }
            return ResponseVO.buildSuccess();
        }else if(resourceId.startsWith("hr")){
            String groupId = resourceId.substring(2);
            GroupPo groupPo = groupRepository.findGroupPoByGroupId(groupId);
            groupRepository.delete(groupPo);
            return ResponseVO.buildSuccess();
        }else{
            return ResponseVO.buildFailure();
        }
    }


    /**
     * 获取资源负载
     * @param s 开始时间，格式为2020-11-11 00：00：00
     * @param e 结束时间，格式同上
     * @param flag  按天/月显示的标志（"day"为按天，"month"为按月）
     * @return
     * @throws ParseException
     */
    public ResponseVO getResourceLoad(String s, String e, String flag) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);
        //起止时间内的天数/月份
        int timeDiff = 0;
        if(flag.equals("day")){
            timeDiff = commonUtil.getDistanceDay(startDate, endDate) + 1;
        }else if(flag.equals("month")){
            timeDiff = commonUtil.getDistanceMonth(startDate, endDate);
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        //一些初始化工作
        int machine_count = 0;
        List<GroupPo> groupPoList = groupRepository.findAll();          //所有的人力资源
        List<MachinePo> machinePoList = machineRepository.findAll();    //所有的机器资源
        List<Map<String, String>> resourceList = new ArrayList<>();     //所有的资源信息（机器在前，人力在后）
        List<String> resourceIdList = new ArrayList<>();                //所有的资源id（index与上面对应）
        List<Integer> machineCountList = new ArrayList<>();
        for(MachinePo machinePo: machinePoList){
            if(resourceIdList.indexOf("ln" + machinePo.getMachineId()) == -1){
                Map<String, String> machineInfo = new HashMap<>();
                machineInfo.put("id", "ln" + machinePo.getMachineId());
                machineInfo.put("name", machinePo.getMachineName());
                resourceList.add(machineInfo);
                resourceIdList.add("ln" + machinePo.getMachineId());
                machineCountList.add(1);
                machine_count++;
            }else{
                int index = resourceIdList.indexOf("ln" + machinePo.getMachineId());
                machineCountList.set(index, machineCountList.get(index) + 1);
            }
        }
        for(GroupPo groupPo: groupPoList){
            Map<String, String> groupInfo = new HashMap<>();
            groupInfo.put("id", "hr" + groupPo.getGroupId());
            groupInfo.put("name", groupPo.getGroupName());
            resourceList.add(groupInfo);
            resourceIdList.add("hr" + groupPo.getGroupId());
        }

        int groupLoad = 0;      //起止时间内，所有人力资源被占用时间的总和（单位：小时）
        int machineLoad = 0;    //起止时间内，所有机器资源被占用时间的总和（单位：小时）

        List<Map<String, Object>> tableData = new ArrayList<>();
        //按天/月统计资源负载
        for(int i = 0; i < timeDiff; i++){
            //起止时间
            //如果是按天显示，示例为：2020-11-11 00：00：00， 2020-11-11 23：59：59， 2020-11-11
            //如果是按月显示，示例为：2020-11-1 00：00：00， 2020-11-30 23：59：59， 2020-11
            Date currentStartTime;
            Date currentEndTime;
            String currentDate;
            if(flag.equals("day")){
                currentStartTime = commonUtil.addStartHour(startDate, 24 * i);
                currentEndTime = commonUtil.addEndHour(currentStartTime, 24);
                currentDate = dayFormat.format(currentEndTime);
            }else {
                currentStartTime = commonUtil.addStartMonth(startDate, i);
                currentEndTime = commonUtil.addEndMonth(startDate, i + 1);
                currentDate = monthFormat.format(currentEndTime);
            }

            Map<String, Object> resourceLoadInfo = new HashMap<>();
            resourceLoadInfo.put("date", currentDate);
            //获取当天/月的排程订单
            List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderByDate(scheduleService.tryGetScheduleOutput().getOrders(),
                    currentStartTime, currentEndTime);
            if(orderList.size() == 0){
                resourceLoadInfo.put("progress", new ArrayList<Integer>());
            }
            else{
                List<Integer> resourceWorkHourList = new ArrayList<>(); //记录每种资源在起止时间内被占用的时间（单位：小时），未被占用则为0
                for(int j = 0; j < resourceIdList.size(); j++){
                    resourceWorkHourList.add(0);
                }
                for(ScheduleOutputDto.Order order: orderList){
                    List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
                    for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                        //判断是否为当天的子订单
                        if(!(currentStartTime.after(subOrder.getStartTime()) || currentEndTime.before(subOrder.getStartTime()))){
                            int durationHour = subOrder.getDurationTimeInHour();
                            List<String> occupyGroupIdList = subOrder.getGroupIdList();     //子订单占用的人力资源
                            for(String occupyGroupId: occupyGroupIdList){
                                int groupIndex = resourceIdList.indexOf("hr" + occupyGroupId);  //获取对应下标
                                resourceWorkHourList.set(groupIndex, durationHour + resourceWorkHourList.get(groupIndex));
                            }
                            int occupyMachineId = Integer.parseInt(subOrder.getMachineId());               //子订单占用的机器资源
                            int machineIndex = resourceIdList.indexOf("ln" + machineRepository.findMachinePoById(occupyMachineId).getMachineId());  //获取对应下标
                            resourceWorkHourList.set(machineIndex, durationHour + resourceWorkHourList.get(machineIndex));
                        }
                    }
                }


                List<Integer> resourceLoadList = new ArrayList<>();
                for(int m = 0; m < machine_count; m++){
                    int progress = resourceWorkHourList.get(m) * 100 / 24 / machineCountList.get(m) /(commonUtil.getDistanceDay(currentStartTime, currentEndTime) + 1);
                    resourceLoadList.add(progress);
                    machineLoad += progress * machineCountList.get(m);

                }
                for(int n = machine_count; n < resourceIdList.size(); n++){
                    List<Integer> tmp = Arrays.asList(1, 2, 3, 4, 5);
                    int progress = 0;
                    if(flag.equals("day")){
                        progress = resourceWorkHourList.get(n) * 100 / 12;
                    }else{
                        progress = resourceWorkHourList.get(n) * 100 / getWorkHourByMonth(currentStartTime, tmp, 12);
                    }
                    resourceLoadList.add(progress);
                    groupLoad += progress;
                }
                resourceLoadInfo.put("progress", resourceLoadList);
            }
            tableData.add(resourceLoadInfo);
        }

        //计算人力资源和机器资源的总负载率
        machineLoad = machineLoad / timeDiff / machinePoList.size();
        groupLoad = groupLoad / timeDiff / groupPoList.size();

        Map<String, Object> content = new HashMap<>();
        content.put("human_percent", groupLoad);
        content.put("device_percent", machineLoad);
        content.put("resourceList", resourceList);
        content.put("tableData", tableData);

        return ResponseVO.buildSuccess(content);
    }

    /**
     * 获取资源甘特图
     * @param s 开始时间，格式为2020-11-11 00：00：00
     * @param e 结束时间，格式同上
     * @return
     * @throws ParseException
     */
    public ResponseVO getResourceOccupy(String s, String e) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);

        List<ResourceOccupyVo> resourceOccupyVoList = new ArrayList<>();
        //获取在起止时间内的排程订单
        List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderByDate(scheduleService.tryGetScheduleOutput().getOrders(),
                startDate, endDate);
        if(orderList.size() == 0){
            return ResponseVO.buildFailure("起止时间内无正在处理的订单");
        }
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
            groupIdList.add(group.getGroupId());
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
            String itemName = "产品" + orderRepository.findOrderPoByOrderId(order.getId()).getItemId();
            //为该产品随机生成一个颜色（延期为红色）
            String color = checkForDelay(order);

            for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                //判断子订单是否在起止时间内
                if(!(startDate.after(subOrder.getStartTime()) || endDate.before(subOrder.getStartTime()))){
                    //子订单开始时间
                    Date start_date = subOrder.getStartTime();
                    //子订单持续时间
                    int durationTime = subOrder.getDurationTimeInHour();
                    //子订单结束时间
                    Date end_date = subOrder.getEndTime();

                    //子订单占用人力资源id列表
                    List<String> occupyGroupIdList = subOrder.getGroupIdList();
                    for(String occupyGroupId: occupyGroupIdList){
                        //获取人力资源对应下标
                        int groupIndex = groupIdList.indexOf(occupyGroupId);
                        //更新对应人力资源占用时间
                        groupOccupyHourList.set(groupIndex, groupOccupyHourList.get(groupIndex) + durationTime);
                        //更新对应人力资源第一次被占用的时间
                        if(groupFirstOccupyTime.get(groupIndex).equals("")){
                            groupFirstOccupyTime.set(groupIndex, dateFormat.format(start_date));
                        }else{
                            if(start_date.before(dateFormat.parse(groupFirstOccupyTime.get(groupIndex)))){
                                groupFirstOccupyTime.set(groupIndex, dateFormat.format(start_date));
                            }
                        }
                        //更新对应人力资源最后一次被占用的时间
                        if(groupLastOccupyTime.get(groupIndex).equals("")){
                            groupLastOccupyTime.set(groupIndex, dateFormat.format(end_date));
                        }else{
                            if(end_date.after(dateFormat.parse(groupLastOccupyTime.get(groupIndex)))){
                                groupLastOccupyTime.set(groupIndex, dateFormat.format(end_date));
                            }
                        }
                        ResourceOccupyVo productGroupOccupy = new ResourceOccupyVo(productIdBegin, "", "",
                                dateFormat.format(start_date), durationTime * 60,
                                itemName, color, itemId, groupIndex + groupIdBegin);
                        productIdBegin++;
                        resourceOccupyVoList.add(productGroupOccupy);
                    }

                    //子订单占用机器资源Id
                    String occupyMachineId = subOrder.getMachineId();
                    //获取机器资源对应下标
                    int machineIndex = machineIdList.indexOf(occupyMachineId);
                    //更新对应机器资源占用时间
                    machineOccupyHourList.set(machineIndex, machineOccupyHourList.get(machineIndex) + durationTime);
                    //更新对应机器资源第一次被占用的时间
                    if(machineFirstOccupyTime.get(machineIndex).equals("")){
                        machineFirstOccupyTime.set(machineIndex, dateFormat.format(start_date));
                    }else{
                        if(start_date.before(dateFormat.parse(machineFirstOccupyTime.get(machineIndex)))){
                            machineFirstOccupyTime.set(machineIndex, dateFormat.format(start_date));
                        }
                    }
                    //更新对应机器资源最后一次被占用的时间
                    if(machineLastOccupyTime.get(machineIndex).equals("")){
                        machineLastOccupyTime.set(machineIndex, dateFormat.format(end_date));
                    }else{
                        if(end_date.after(dateFormat.parse(machineLastOccupyTime.get(machineIndex)))){
                            machineLastOccupyTime.set(machineIndex, dateFormat.format(end_date));
                        }
                    }
                    ResourceOccupyVo productMachineOccupy = new ResourceOccupyVo(productIdBegin, "", "",
                            dateFormat.format(start_date), durationTime * 60,
                            itemName, color, itemId, machineIndex + machineIdBegin);
                    productIdBegin++;
                    resourceOccupyVoList.add(productMachineOccupy);
                }
            }
        }

        for(int i = 0; i < groupIdList.size(); i++){
            String groupId = groupIdList.get(i);
            String groupName = groupRepository.findGroupPoByGroupId(groupId).getGroupName();
            String percent = Math.min(groupOccupyHourList.get(i), 12) * 100 / 12 + "%";
            String start = groupFirstOccupyTime.get(i);
            int duration = 0;
            if(groupOccupyHourList.get(i) > 0){
                duration = commonUtil.getDistanceHour(
                        dateFormat.parse(groupFirstOccupyTime.get(i)),
                        dateFormat.parse(groupLastOccupyTime.get(i)));
            }
            duration = Math.min(duration, 24);
            ResourceOccupyVo groupOccupy = new ResourceOccupyVo();
            groupOccupy.setId(groupIdBegin + i);
            groupOccupy.setResource(groupName);
            groupOccupy.setPercent(percent);
            groupOccupy.setStart_date(start);
            groupOccupy.setDuration(60 * duration);
            groupOccupy.setText("");
            groupOccupy.setColor("darkturquoise");
            groupOccupy.setProduct_id("");
            groupOccupy.setParent(0);
            resourceOccupyVoList.add(groupOccupy);
        }

        for(int j = 0; j < machineIdList.size(); j++){
            String machineId = machineIdList.get(j);
            String machineName = machineRepository.findMachinePoById(Integer.parseInt(machineId)).getMachineName();
            String percent = Math.min(machineOccupyHourList.get(j), 24) * 100 / 24 + "%";
            String start = machineFirstOccupyTime.get(j);
            int duration = 0;
            if(machineOccupyHourList.get(j) > 0){
                duration = commonUtil.getDistanceHour(
                        dateFormat.parse(machineFirstOccupyTime.get(j)),
                        dateFormat.parse(machineLastOccupyTime.get(j)));
            }
            duration = Math.min(duration, 24);
            ResourceOccupyVo machineOccupy = new ResourceOccupyVo();
            machineOccupy.setId(machineIdBegin + j);
            machineOccupy.setResource(machineName);
            machineOccupy.setPercent(percent);
            machineOccupy.setStart_date(start);
            machineOccupy.setDuration(60 * duration);
            machineOccupy.setText("");
            machineOccupy.setColor("darkturquoise");
            machineOccupy.setProduct_id("");
            machineOccupy.setParent(0);
            resourceOccupyVoList.add(machineOccupy);
        }

        Map<String, Object> content = new HashMap<>();
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("data", resourceOccupyVoList);
        tasks.put("links", new ArrayList<>());
        content.put("tasks", tasks);
        return ResponseVO.buildSuccess(content);
    }

    public ResponseVO getResourceOccupyInfo(String s, String e) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(s);
        Date endDate = simpleDateFormat.parse(e);

        //获取在起止时间内的排程订单
        List<ScheduleOutputDto.Order> orderList = orderUtil.getOrderByDate(scheduleService.tryGetScheduleOutput().getOrders(),
                startDate, endDate);
        if(orderList.size() == 0){
            return ResponseVO.buildFailure("起止时间内无正在处理的订单");
        }
        //一些初始化工作
        List<GroupPo> groupPoList = groupRepository.findAll();
        List<MachinePo> machinePoList = machineRepository.findAll();
        ArrayList<String> groupIdList = new ArrayList<>();              //所有人力资源Id
        ArrayList<ResourceOccupyInfoVo> groupOccupyInfoList = new ArrayList<>();
        ArrayList<String> machineIdList = new ArrayList<>();
        ArrayList<ResourceOccupyInfoVo> machineOccupyInfoList = new ArrayList<>();
        for(GroupPo group: groupPoList){
            groupIdList.add(group.getGroupId());
            ResourceOccupyInfoVo groupOccupyInfo = new ResourceOccupyInfoVo();
            List<ResourceOccupyInfoVo.OccupyInfo> occupyInfoList = new ArrayList<>();
            groupOccupyInfo.setResourceId("hr" + group.getGroupId());
            groupOccupyInfo.setResourceName(group.getGroupName());
            groupOccupyInfo.setOccupyInfoList(occupyInfoList);
            groupOccupyInfoList.add(groupOccupyInfo);
        }
        for(MachinePo machine: machinePoList){
            machineIdList.add(String.valueOf(machine.getId()));
            ResourceOccupyInfoVo machineOccupyInfo = new ResourceOccupyInfoVo();
            List<ResourceOccupyInfoVo.OccupyInfo> occupyInfoList = new ArrayList<>();
            machineOccupyInfo.setResourceId("hr" + machine.getId());
            machineOccupyInfo.setResourceName(machine.getMachineName());
            machineOccupyInfo.setOccupyInfoList(occupyInfoList);
            machineOccupyInfoList.add(machineOccupyInfo);
        }

        for(ScheduleOutputDto.Order order: orderList){
            String orderId = order.getId();
            List<ScheduleOutputDto.SubOrder> subOrderList = order.getSubOrders();
            for(ScheduleOutputDto.SubOrder subOrder: subOrderList){
                String subOrderId = subOrder.getId();
                Date startTime = subOrder.getStartTime();
                Date endTime = subOrder.getEndTime();
                if(!(startTime.before(startDate) || startTime.after(endDate))){
                    List<String> occupyGroupIdList = subOrder.getGroupIdList();
                    for(String occupyGroupId: occupyGroupIdList){
                        int occupyGroupIndex = groupIdList.indexOf(occupyGroupId);
                        List<ResourceOccupyInfoVo.OccupyInfo> occupyInfoList = groupOccupyInfoList.get(occupyGroupIndex).getOccupyInfoList();
                        ResourceOccupyInfoVo.OccupyInfo occupyInfo = new ResourceOccupyInfoVo.OccupyInfo(
                                simpleDateFormat.format(startTime), simpleDateFormat.format(endTime),
                                orderId, subOrderId);
                        occupyInfoList.add(occupyInfo);
                    }
                    String occupyMachineId = subOrder.getMachineId();
                    int occupyMachineIndex = machineIdList.indexOf(occupyMachineId);
                    List<ResourceOccupyInfoVo.OccupyInfo> occupyInfoList = machineOccupyInfoList.get(occupyMachineIndex).getOccupyInfoList();
                    ResourceOccupyInfoVo.OccupyInfo occupyInfo = new ResourceOccupyInfoVo.OccupyInfo(
                            simpleDateFormat.format(startTime), simpleDateFormat.format(endTime),
                            orderId, subOrderId);
                    occupyInfoList.add(occupyInfo);
                }
            }
        }

        Map<String, Object> content = new HashMap<>();
        content.put("human", groupOccupyInfoList);
        content.put("device", machineOccupyInfoList);
        return ResponseVO.buildSuccess(content);
    }

    public String checkForDelay(ScheduleOutputDto.Order order){
        String id = order.getId();
        //判断最后一个子订单是否超期
        ScheduleOutputDto.SubOrder lastSubOrder = order.getSubOrders().get(order.getSubOrders().size() - 1);
        Date dueTime = orderRepository.findOrderPoByOrderId(id).getDeadLine();  //DDL
        Date endTime = commonUtil.addEndHour(lastSubOrder.getStartTime(), lastSubOrder.getDurationTimeInHour());
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

    /**
     * 获取人力资源在某一个月内的可分配工作时间
     * @param date 日期
     * @param workdayList 工作日
     * @param workHour 工作时间
     * @return 当月可分配工作总时间
     */
    public int getWorkHourByMonth(Date date, List<Integer> workdayList, int workHour){
        List<String> dayOfWeekList = Arrays.asList("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
        SimpleDateFormat format = new SimpleDateFormat("E");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int count = 0;
        for(int i = 0; i < day; i++){
            Date current = commonUtil.addDay(date, i);
            String dayOfWeek = format.format(current);
            if(workdayList.indexOf(dayOfWeekList.indexOf(dayOfWeek)) != -1)
                count += workHour;
        }

        return count;
    }

}
