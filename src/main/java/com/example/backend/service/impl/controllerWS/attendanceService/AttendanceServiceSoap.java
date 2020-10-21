
package com.example.backend.service.impl.controllerWS.attendanceService;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "AttendanceServiceSoap", targetNamespace = "http://Service/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AttendanceServiceSoap {


    /**
     * 
     * @return
     *     returns java.util.List<com.example.backend.service.impl.controllerWS.attendanceService.ClassEntity>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getClassInfo", targetNamespace = "http://Service/", className = "com.example.backend.service.impl.controllerWS.attendanceService.GetClassInfo")
    @ResponseWrapper(localName = "getClassInfoResponse", targetNamespace = "http://Service/", className = "com.example.backend.service.impl.controllerWS.attendanceService.GetClassInfoResponse")
    @Action(input = "http://Service/AttendanceServiceSoap/getClassInfoRequest", output = "http://Service/AttendanceServiceSoap/getClassInfoResponse")
    public List<ClassEntity> getClassInfo();

    /**
     * 
     * @return
     *     returns java.util.List<com.example.backend.service.impl.controllerWS.attendanceService.CalendarEntity>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCalendarInfo", targetNamespace = "http://Service/", className = "com.example.backend.service.impl.controllerWS.attendanceService.GetCalendarInfo")
    @ResponseWrapper(localName = "getCalendarInfoResponse", targetNamespace = "http://Service/", className = "com.example.backend.service.impl.controllerWS.attendanceService.GetCalendarInfoResponse")
    @Action(input = "http://Service/AttendanceServiceSoap/getCalendarInfoRequest", output = "http://Service/AttendanceServiceSoap/getCalendarInfoResponse")
    public List<CalendarEntity> getCalendarInfo();

}
