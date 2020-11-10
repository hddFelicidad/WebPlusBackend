package com.example.backend.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitBean {

    @Autowired
    private InitTable table;

    @PostConstruct
    public void init(){
        System.out.println("Init begin ...");
        table.tableInit();
        System.out.println("Init end ...");
    }
}
