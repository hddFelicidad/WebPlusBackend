package com.example.backend.service.impl;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.service.ResourceService;
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
    GroupRepository groupRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public ResponseVO getOccupy(String date) {
        List<ResourceOccupyVo> resourceOccupyVoList = new ArrayList<>();

        LegacySystemServiceImpl legacySystemService = new LegacySystemServiceImpl();

        List<ScheduleOutputData.Order> orderList = getOutputData().getOrders();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date += " 00:00:00";
        try{
            Date targetDate = simpleDateFormat.parse(date);

            for(ScheduleOutputData.Order order: orderList){
                //判断子订单是否为当天订单
                if(checkForDate(targetDate, order.getSubOrders().get(0).getStartTime())){
                    //子订单列表
                    List<ScheduleOutputData.SubOrder> subOrderList = order.getSubOrders();
                    //产品id
                    String itemId = orderRepository.findOrderPoByOrderId(order.getId()).getItemId();
                    //产品名称
                    String itemName = legacySystemService.getMaterialInfoById(itemId).getDescription();
                    //为该产品随机生成一个颜色（延期为红色）
                    String color = checkForDelay(order);

                    for(ScheduleOutputData.SubOrder subOrder: subOrderList){
                        if(checkForDate(targetDate, subOrder.getStartTime())){
                            //子订单开始时间
                            Date start_date = subOrder.getStartTime();
                            //子订单持续时间
                            int durationTime = subOrder.getDurationTimeInHour();
                            //子订单结束时间
                            Date end_date = addHour(start_date, durationTime);
                            //子订单占用人力资源Id
                            String groupId = subOrder.getGroupId();
                            //子订单占用机器资源Id
                            String machineId = subOrder.getMachineId();

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
            }

            for(int i = 0; i < groupIdList.size(); i++){
                String groupId = groupIdList.get(i);
                String groupName = groupRepository.findGroupPoByGroupId(Integer.parseInt(groupId)).getGroupName();
                int groupOccupyHour = groupOccupyHourList.get(i) > 24 ? 24 : groupOccupyHourList.get(i);
                String percent = groupOccupyHour / 24 * 100 + "%";
                String startDate = groupFirstOccupyTime.get(i);
                int duration = getDistanceHour(
                        simpleDateFormat.parse(groupFirstOccupyTime.get(i)),
                        simpleDateFormat.parse(groupLastOccupyTime.get(i)));
                duration = Math.min(duration, 24);
                ResourceOccupyVo groupOccupy = new ResourceOccupyVo();
                groupOccupy.setId(groupIdBegin + i);
                groupOccupy.setResource(groupName);
                groupOccupy.setPercent(percent);
                groupOccupy.setStart_date(startDate);
                groupOccupy.setDuration(String.valueOf(60 * duration));
                groupOccupy.setText("");
                groupOccupy.setColor("darkturquoise");
                groupOccupy.setProduct_id("");
                resourceOccupyVoList.add(groupOccupy);
            }

            for(int j = 0; j < machineIdList.size(); j++){
                String machineId = machineIdList.get(j);
                String machineName = machineRepository.findMachinePoByMachineId(Integer.parseInt(machineId)).getMachineName();
                int machineOccupyHour = machineOccupyHourList.get(j) > 24 ? 24 : machineOccupyHourList.get(j);
                String percent = machineOccupyHour / 24 * 100 + "%";
                String startDate = machineFirstOccupyTime.get(j);
                int duration = getDistanceHour(
                        simpleDateFormat.parse(machineFirstOccupyTime.get(j)),
                        simpleDateFormat.parse(machineLastOccupyTime.get(j)));
                duration = Math.min(duration, 24);
                ResourceOccupyVo machineOccupy = new ResourceOccupyVo();
                machineOccupy.setId(machineIdBegin + j);
                machineOccupy.setResource(machineName);
                machineOccupy.setPercent(percent);
                machineOccupy.setStart_date(startDate);
                machineOccupy.setDuration(String.valueOf(60 * duration));
                machineOccupy.setText("");
                machineOccupy.setColor("darkturquoise");
                machineOccupy.setProduct_id("");
                resourceOccupyVoList.add(machineOccupy);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseVO.buildSuccess(resourceOccupyVoList);
    }

    public String checkForDelay(ScheduleOutputData.Order order){
        String id = order.getId();
        ScheduleOutputData.SubOrder lastSubOrder = order.getSubOrders().get(order.getSubOrders().size() - 1);
        Date dueTime = orderRepository.findOrderPoByOrderId(id).getDeadLine();  //DDL
        Date endTime = addHour(lastSubOrder.getStartTime(), lastSubOrder.getDurationTimeInHour());
        if(endTime.after(dueTime)){
            return "red";
        }else{
            String randomColor = getColor();
            while (randomColor.equals("#FF0000") || randomColor.equals("#00CED1")){
                randomColor = getColor();
            }
            return randomColor;
        }
    }

    public boolean checkForDate(Date targetDate, Date startTime){
        int timeDiff = getDistanceHour(targetDate, startTime);
        return timeDiff >= 0 && timeDiff < 24;
    }

    public int getDistanceHour(Date startTime, Date endTime){
        long start = startTime.getTime();
        long end = endTime.getTime();
        long diff = end - start;
        return (int) (diff / (60 * 60 * 1000));
    }

    public Date addHour(Date date, int hour){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public String getColor(){
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length()==1 ? "0" + red : red ;
        //判断绿色代码的位数
        green = green.length()==1 ? "0" + green : green ;
        //判断蓝色代码的位数
        blue = blue.length()==1 ? "0" + blue : blue ;
        //生成十六进制颜色值
        return "#" + red + green + blue;
    }

    public ScheduleOutputData getOutputData(){
        ScheduleUtil util = new ScheduleUtil();
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("1", "组一", 5, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("2", "组二", 5, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("3", "组三", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("4", "组四", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("5", "组五", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("6", "组六", 6, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("7", "组七", 6, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("8", "组八", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("9", "组九", 5, Arrays.asList(new TimeInterval(7, 19))));
        List<Machine> machines = new ArrayList<>();
        machines.add(new Machine("1", "line1", "1"));
        machines.add(new Machine("2", "line1", "1"));
        machines.add(new Machine("3", "line1", "1"));
        machines.add(new Machine("4", "line2", "2"));
        machines.add(new Machine("5", "line2", "2"));
        machines.add(new Machine("6", "line3", "3"));
        machines.add(new Machine("7", "line3", "3"));
        machines.add(new Machine("8", "line3", "3"));
        machines.add(new Machine("9", "line3", "3"));
        machines.add(new Machine("10", "line4", "4"));
        List<ScheduleInputData.Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            ScheduleInputData input = new ScheduleInputData(dateFormat.parse("2020-10-26 09"), groups, machines,
                    orders);
            orders.add(input.new Order("1", "订单一", 40, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2"),
                    dateFormat.parse("2020-10-27 09")));
            orders.add(input.new Order("2", "订单二", 24, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3"),
                    dateFormat.parse("2020-10-27 12")));
            orders.add(input.new Order("3", "订单三", 36, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4"),
                    dateFormat.parse("2020-10-27 14")));
            return util.solve(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
