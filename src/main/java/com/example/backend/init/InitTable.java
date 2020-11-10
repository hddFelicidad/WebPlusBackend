package com.example.backend.init;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.data.OrderRepository;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.po.OrderPo;
import com.example.backend.service.LegacySystemService;
import com.example.backend.service.impl.controllerWS.attendanceService.CalendarEntity;
import com.example.backend.service.impl.controllerWS.erpService.ResourceEntity;
import com.example.backend.service.impl.controllerWS.orderService.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.var;

import java.util.List;

@Component("initTable")
public class InitTable {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    LegacySystemService legacySystemService;

    public void tableInit(){
        System.out.println("Group init begin ...");
        groupInit();
        System.out.println("Machine init begin ...");
        machineInit();
        System.out.println("Order init begin ...");
        orderInit();
    }

    public void groupInit(){
        var groups = groupRepository.findAll();
        if(groups.isEmpty()){
            List<ResourceEntity> resourceInfoList = legacySystemService.getResourceTeamInfo();
            List<CalendarEntity> calendarInfoList = legacySystemService.getCalendarInfo();
            for(ResourceEntity resourceInfo: resourceInfoList){
                if(resourceInfo.getResourceName().equals("班组")){
                    String groupName = resourceInfo.getResourceId();
                    int memberCount = Integer.parseInt(resourceInfo.getResourceNum());
                    String groupId = "";
                    if(groupName.startsWith("UKK")){
                        groupId = "0";
                    }else{
                        groupId = groupName.substring(0, groupName.indexOf("组"));
                    }
                    String className = "";

                    int classCode = 0;
                    for(CalendarEntity calendarInfo: calendarInfoList){
                        if (calendarInfo.getResourceCode().equals(groupName)){
                            classCode = calendarInfo.getClassCode();
                            break;
                        }
                    }
                    switch (classCode){
                        case 0:
                            className = "ALL";
                            break;
                        case 1:
                            className = "DAY";
                            break;
                        case 2:
                            className = "NIGHT";
                            break;
                        default:
                            break;
                    }

                    GroupPo groupPo = new GroupPo(null, groupName, groupId,
                            memberCount, className, "2020-11-01");
                    groupRepository.save(groupPo);
                }
            }
        }
    }

    public void machineInit(){
        var machines = machineRepository.findAll();
        if(machines.isEmpty()){
            List<ResourceEntity> resourceInfoList = legacySystemService.getResourceTeamInfo();
            for(ResourceEntity resourceInfo: resourceInfoList){
                if(resourceInfo.getResourceName().equals("线体")){
                    String machineName = resourceInfo.getResourceId();
                    String machineId = machineName.substring(4);
                    if(machineId.startsWith("0")){
                        machineId = machineName.substring(5);
                    }
                    int count = Integer.parseInt(resourceInfo.getResourceNum());

                    for(int i = 0; i < count; i++){
                        MachinePo machinePo = new MachinePo(null, machineName,
                                machineId, "2020-11-01");
                        machineRepository.save(machinePo);
                    }
                }
            }
        }
    }

    public void orderInit(){
        var orders = orderRepository.findAll();
        if(orders.isEmpty()){
            List<OrderEntity> orderInfoList = legacySystemService.getAllOrders();
            for(OrderEntity orderInfo: orderInfoList){
                OrderPo orderPo = new OrderPo(orderInfo.getId(), orderInfo.getMaterialId(),
                        orderInfo.getNumber().intValue(), orderInfo.getDdl().toGregorianCalendar().getTime());
                orderRepository.save(orderPo);
            }
        }
    }
}
