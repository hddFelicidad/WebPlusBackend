package com.example.backend.service.impl;

import com.example.backend.data.BomRepository;
import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.TimeIntervalDto;
import com.example.backend.po.BomPo;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.LegacySystemService;
import com.example.backend.service.ScheduleInitService;
import com.example.backend.service.ScheduleService;
import com.example.backend.service.TimerService;
import com.example.backend.service.impl.controllerWS.erpService.BomEntity;
import com.example.backend.vo.ResponseVO;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class ScheduleInitServiceImpl implements ScheduleInitService {
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
    BomRepository bomRepository;
    @Autowired
    LegacySystemService legacySystemService;

    @Override
    public ResponseVO scheduleInit(Date startDate) {
        System.out.println("Start to init schedule ...");
        ScheduleInputDto input = new ScheduleInputDto(getGroupInput(), getMachineInput(), getOrderInput());
        scheduleService.schedule(input, startDate);
        return ResponseVO.buildSuccess();
    }

    @Override
    public ScheduleInputDto getScheduleInput() {
        return new ScheduleInputDto(getGroupInput(), getMachineInput(), getOrderInput());
    }

    /**
     * 获取人力资源信息
     *
     * @return
     */
    public List<ScheduleInputDto.Group> getGroupInput() {
        List<GroupPo> groupPoList = groupRepository.findAll();
        if (!groupPoList.isEmpty()) {
            List<ScheduleInputDto.Group> groupList = new ArrayList<>();
            for (GroupPo eachGroup : groupPoList) {
                ScheduleInputDto.Group tmpGroup = new ScheduleInputDto.Group();
                tmpGroup.setId(eachGroup.getGroupId());
                tmpGroup.setName(eachGroup.getGroupName());
                tmpGroup.setMemberCount(eachGroup.getMemberCount());
                List<TimeIntervalDto> workIntervals = new ArrayList<>();
                switch (eachGroup.getClassName()) {
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
     *
     * @return
     */
    public List<ScheduleInputDto.Machine> getMachineInput() {
        List<MachinePo> machinePoList = machineRepository.findAll();
        if (!machinePoList.isEmpty()) {
            List<ScheduleInputDto.Machine> machineList = new ArrayList<>();
            for (MachinePo eachMachine : machinePoList) {
                ScheduleInputDto.Machine machine = new ScheduleInputDto.Machine(String.valueOf(eachMachine.getId()),
                        eachMachine.getMachineName(), eachMachine.getMachineId());
                machineList.add(machine);
            }
            return machineList;
        }
        return null;
    }

    /**
     * 获取订单
     *
     * @return
     */
    public List<ScheduleInputDto.Order> getOrderInput() {
        List<OrderPo> orderPoList = orderRepository.findAll();
        if (!orderPoList.isEmpty()) {
            List<ScheduleInputDto.Order> orderList = new ArrayList<>();
            for (OrderPo eachOrder : orderPoList) {
                String orderId = eachOrder.getOrderId();
                String orderName = "订单" + orderId;
                Date ddl = eachOrder.getDeadLine();

                String itemId = eachOrder.getItemId();
                int itemCount = eachOrder.getItemCount();

                var bomPoList = bomRepository.findBomPosByBomId(itemId);
                if(!bomPoList.isEmpty()){
                    for(BomPo bomPo: bomPoList){
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

                        ScheduleInputDto.Order order = new ScheduleInputDto.Order(orderId + "-" + process, orderName + "-" + process, false, needHour,
                                workCount, new HashSet<>(availableGroupList), new HashSet<>(availableMachineList), ddl);
                        orderList.add(order);
                    }
                }
            }
            return orderList;
        }
        return null;
    }
}
