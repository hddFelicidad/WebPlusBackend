package com.example.backend.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class CommonUtil {

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
}
