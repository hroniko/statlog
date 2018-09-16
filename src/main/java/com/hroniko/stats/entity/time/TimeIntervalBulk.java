package com.hroniko.stats.entity.time;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeIntervalBulk {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private Long middleTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // , timezone = "Europe/Moscow"
    private Date middleDate;

    private Map<String, Long> bulkCount; // для балкового хранения значений с ключами

    public TimeIntervalBulk() {
    }

    public TimeIntervalBulk(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        setMiddle();
        setMiddleDate();

    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        setMiddle();
        setMiddleDate();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        setMiddle();
        setMiddleDate();
    }


    public Long getMiddleTime() {
        return middleTime;
    }

    public void setMiddleTime(Long middleTime) {
        this.middleTime = middleTime;
    }

    private void setMiddleDate(){
        this.middleDate = new Date((startDate.getTime() + endDate.getTime())/2);
    }

    private void setMiddle(){
        this.middleTime = (startDate.getTime() + endDate.getTime())/2;
    }

    public Map<String, Long> getBulkCount() {
        return bulkCount;
    }

    public void setBulkCount(Map<String, Long> bulkCount) {
        this.bulkCount = bulkCount;
    }

    public void addNewCount(String name, Long count){
        if(bulkCount == null) bulkCount = new HashMap<>();
        if(bulkCount.containsKey(name)){
            bulkCount.remove(name);
        }
        bulkCount.put(name, count);
    }
}
