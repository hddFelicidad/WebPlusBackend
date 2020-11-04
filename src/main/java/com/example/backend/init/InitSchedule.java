package com.example.backend.init;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.dto.TimeIntervalDto;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.LegacySystemService;
import com.example.backend.service.ScheduleService;
import com.example.backend.service.TimerService;
import com.example.backend.service.impl.controllerWS.erpService.BomEntity;
import com.example.backend.vo.TimerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component("initSchedule")
public class InitSchedule {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    TimerService timerService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    LegacySystemService legacySystemService;


    public void scheduleInit(){
        System.out.println("Start to init schedule ...");
//        ScheduleInputDto input = getScheduleInput();
        ScheduleInputDto input = getTmpSchedule();
        scheduleService.schedule(input);
        ScheduleOutputDto scheduleOutputDto = scheduleService.waitForScheduleOutput();
        System.out.println("End of init schedule.");
//        System.out.println(scheduleOutputDto.getOrders().size());
    }

    public ScheduleInputDto getTmpSchedule(){
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("5", "5组-童玲 (5)", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(new ScheduleInputDto.Group("9", "9组-张敏（5）", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(
                new ScheduleInputDto.Group("1", "1组-彭慧 (5)", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(
                new ScheduleInputDto.Group("12", "12组-姚兰（5）", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(
                new ScheduleInputDto.Group("15", "15组-李娟（5）", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(new ScheduleInputDto.Group("3", "3组-李翠 (4)", 4, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(new ScheduleInputDto.Group("14", "14组-周  清（4）", 4, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(
                new ScheduleInputDto.Group("16", "16组-朱美（4）", 4, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(new ScheduleInputDto.Group("40", "40组-高燕（5）", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("1", "line01", "1"));
        machines.add(new ScheduleInputDto.Machine("2", "line01", "1"));
        machines.add(new ScheduleInputDto.Machine("3", "line01", "1"));
        machines.add(new ScheduleInputDto.Machine("4", "line02", "2"));
        machines.add(new ScheduleInputDto.Machine("5", "line02", "2"));
        machines.add(new ScheduleInputDto.Machine("6", "line02", "2"));
        machines.add(new ScheduleInputDto.Machine("7", "line03", "3"));
        machines.add(new ScheduleInputDto.Machine("8", "line03", "3"));
        machines.add(new ScheduleInputDto.Machine("9", "line03", "3"));
        machines.add(new ScheduleInputDto.Machine("10", "line04", "4"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            input.setStartTime(dateFormat.parse("2020-11-3 07"));
            input.setGroups(groups);
            input.setMachines(machines);
            input.setOrders(orders);
            orders.add(new ScheduleInputDto.Order("413095", "订单413095", false, 8, 8, Arrays.asList("5", "9", "1", "12"), Arrays.asList("1", "2"),
                    dateFormat.parse("2020-11-4 10")));
            orders.add(new ScheduleInputDto.Order("414837", "订单414837", false, 8, 8, Arrays.asList("3", "14", "16", "40"), Arrays.asList("2", "3"),
                    dateFormat.parse("2020-11-4 12")));
            orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 8, 11, Arrays.asList("1", "15", "16", "40"), Arrays.asList("1", "3", "4"),
                    dateFormat.parse("2020-11-4 14")));
            return input;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ScheduleInputDto getScheduleInput() {
        TimerVo timerVo = timerService.getTimer();
        Date startTime = new Date();
        if(timerVo != null)
            startTime = timerService.getTimer().getInitTime();
        return new ScheduleInputDto(startTime,
                getGroupInput(), getMachineInput(), getOrderInput());
    }

    /**
     * 获取人力资源信息
     * @return
     */
    public List<ScheduleInputDto.Group> getGroupInput(){
        List<GroupPo> groupPoList = groupRepository.findAll();
        if(!groupPoList.isEmpty()){
            List<ScheduleInputDto.Group> groupList = new ArrayList<>();
            for(GroupPo eachGroup: groupPoList){
                ScheduleInputDto.Group tmpGroup = new ScheduleInputDto.Group();
                tmpGroup.setId(eachGroup.getGroupId());
                tmpGroup.setName(eachGroup.getGroupName());
                tmpGroup.setMemberCount(eachGroup.getMemberCount());
                List<TimeIntervalDto> workIntervals = new ArrayList<>();
                switch (eachGroup.getClassName()){
                    case "DAY":
                        workIntervals.add(new TimeIntervalDto(7, 19));
                        break;
                    case "NIGHT":
                        workIntervals.add(new TimeIntervalDto(19, 24));
                        workIntervals.add(new TimeIntervalDto(0, 7));
                        break;
                    case "ALL":
                        workIntervals.add(new TimeIntervalDto(0, 24));
                        break;
                    default:
                        break;
                }
                tmpGroup.setWorkIntervals(workIntervals);
                groupList.add(tmpGroup);
            }
            return groupList;
        }
        return null;
    }

    /**
     * 获取机器资源
     * @return
     */
    public List<ScheduleInputDto.Machine> getMachineInput(){
        List<MachinePo> machinePoList = machineRepository.findAll();
        if(!machinePoList.isEmpty()){
            List<ScheduleInputDto.Machine> machineList = new ArrayList<>();
            for(MachinePo eachMachine: machinePoList){
                ScheduleInputDto.Machine machine = new ScheduleInputDto.Machine(String.valueOf(eachMachine.getId()),
                        eachMachine.getMachineName(),
                        eachMachine.getMachineId());
                machineList.add(machine);
            }
            return machineList;
        }
        return null;
    }

    /**
     * 获取订单
     * @return
     */
    public List<ScheduleInputDto.Order> getOrderInput() {
        List<OrderPo> orderPoList = orderRepository.findAll();
        if(!orderPoList.isEmpty()){
            List<ScheduleInputDto.Order> orderList = new ArrayList<>();
            for(OrderPo eachOrder: orderPoList){
                String orderId = eachOrder.getOrderId();
                String orderName = "订单" + orderId;
                Date ddl = eachOrder.getDeadLine();

                String itemId = eachOrder.getItemId();
                int itemCount = eachOrder.getItemCount();

                BomEntity bomEntity = legacySystemService.getBOMById(itemId);
                int standardOutput = Integer.parseInt(bomEntity.getStandardOutput().substring(0, bomEntity.getStandardOutput().indexOf("个")));
                int workCount = bomEntity.getWorkerCount();
                List<String> groupResourceList = bomEntity.getMainResource();
                List<String> lineResourceList = bomEntity.getLineResource();

                int needHour = itemCount / standardOutput;

                List<String> availableGroupList = new ArrayList<>();
                for(String groupName: groupResourceList){
                    GroupPo groupPo = groupRepository.findGroupPoByGroupName(groupName);
                    if(groupPo != null)
                        availableGroupList.add(groupPo.getGroupId());
                }
                List<String> availableMachineList = new ArrayList<>();
                for(String machineName: lineResourceList){
                    List<MachinePo> machinePoList = machineRepository.findMachinePosByMachineName(machineName);
                    if(!machinePoList.isEmpty())
                        availableMachineList.add(machinePoList.get(0).getMachineId());
                }

                ScheduleInputDto.Order order = new ScheduleInputDto.Order(orderId, orderName, false, needHour, workCount,
                        availableGroupList, availableMachineList, ddl);
                orderList.add(order);
            }
            return orderList;
        }
        return null;
    }
}
