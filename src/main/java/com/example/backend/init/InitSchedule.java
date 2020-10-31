package com.example.backend.init;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.TimeIntervalDto;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.LegacySystemService;
import com.example.backend.service.ScheduleService;
import com.example.backend.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        ScheduleInputDto input = getScheduleInput();
        scheduleService.schedule(input);
        System.out.println("End of init schedule.");
    }

    public ScheduleInputDto getScheduleInput(){
        Date startTime = timerService.getTimer().getInitTime();
        ScheduleInputDto input = new ScheduleInputDto(startTime,
                getGroupInput(), getMachineInput(), getOrderInput());
        return null;
    }

    /**
     * 获取人力资源信息
     * @return
     */
    public List<ScheduleInputDto.Group> getGroupInput(){
        List<GroupPo> groupPoList = groupRepository.findAll();
        if(groupPoList.size() > 0){
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
        if(machinePoList.size() > 0){
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
    public List<ScheduleInputDto.Order> getOrderInput(){
        List<OrderPo> orderPoList = orderRepository.findAll();
        if(orderPoList.size() > 0){
            List<ScheduleInputDto.Order> orderList = new ArrayList<>();
            for(OrderPo eachOrder: orderPoList){
                String orderId = eachOrder.getOrderId();
                String itemId = eachOrder.getItemId();
                int itemCount = eachOrder.getItemCount();
                Date deadLine = eachOrder.getDeadLine();
                //TODO
            }
            return orderList;
        }
        return null;
    }
}
