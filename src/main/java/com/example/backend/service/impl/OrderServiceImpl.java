package com.example.backend.service.impl;

import com.example.backend.service.OrderService;
import com.example.backend.service.impl.controllerWS.orderService.OrderEntity;
import com.example.backend.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    LegacySystemServiceImpl legacySystemService;

    @Override
    public List<OrderVo> getAllOrders() {
        List<OrderEntity> orderEntityList = legacySystemService.getAllOrders();
        List<OrderVo> orderVoList = new ArrayList<>();
        for(OrderEntity eachOrder: orderEntityList){
            OrderVo tempOrder = new OrderVo();
            tempOrder.setId(eachOrder.getId());
            tempOrder.setItemId(eachOrder.getMaterialId());
            tempOrder.setItemCount(eachOrder.getNumber().intValue());
            tempOrder.setDeadLine(eachOrder.getDdl().toGregorianCalendar().getTime());
            orderVoList.add(tempOrder);
        }

        return orderVoList;
    }

    @Override
    public int insertOrder(OrderVo order) {
        OrderEntity content = new OrderEntity();
        content.setId(order.getId());
        content.setMaterialId(order.getItemId());
        content.setNumber(Long.valueOf(order.getItemCount()));
        content.setDdl(convertToXMLGregorianCalendar(order.getDeadLine()));
        return legacySystemService.insertOrder(content);
    }

    public XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }
}
