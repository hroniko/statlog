package com.hroniko.stats.utils.timeline;

import com.hroniko.stats.entity.time.TimeInterval;
import com.hroniko.stats.entity.time.TimeIntervalBulk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IntervalGenerator {

    public static List<TimeInterval> create(Date startDate, Date endDate, String interval){
        Long msInterval = 1L;
        if (interval.contains("m")){
            // работаем с минутами
            msInterval = new Long(interval.replace("m", "").trim());
            msInterval *= 60 * 1000; // переводим в мс
        } else {
            if (interval.contains("h")){
                // Работаем с часами
                msInterval = new Long(interval.replace("h", "").trim());
                msInterval *= 60 * 60 * 1000; // переводим в мс
            }
        }
        List<TimeInterval> intervals = new ArrayList<>();

        while (true){
            Date intervDate = new Date(startDate.getTime() + msInterval);
            if (startDate.after(endDate)) break; //выход из цикла, если вышли за правую границу
            TimeInterval timeInterval = new TimeInterval(startDate, intervDate.after(endDate) ? endDate : intervDate); // TimeInterval timeInterval = new TimeInterval(startDate, intervDate); //
            intervals.add(timeInterval);
            startDate = intervDate;
        }
        return intervals;
    }


    public static List<TimeIntervalBulk> createBulk(Date startDate, Date endDate, String interval){
        Long msInterval = 1L;
        if (interval.contains("m")){
            // работаем с минутами
            msInterval = new Long(interval.replace("m", "").trim());
            msInterval *= 60 * 1000; // переводим в мс
        } else {
            if (interval.contains("h")){
                // Работаем с часами
                msInterval = new Long(interval.replace("h", "").trim());
                msInterval *= 60 * 60 * 1000; // переводим в мс
            }
        }
        List<TimeIntervalBulk> intervals = new ArrayList<>();

        while (true){
            Date intervDate = new Date(startDate.getTime() + msInterval);
            if (startDate.after(endDate)) break; //выход из цикла, если вышли за правую границу
            TimeIntervalBulk timeInterval = new TimeIntervalBulk(startDate, intervDate.after(endDate) ? endDate : intervDate); // TimeInterval timeInterval = new TimeInterval(startDate, intervDate); //
            intervals.add(timeInterval);
            startDate = intervDate;
        }
        return intervals;
    }
}
