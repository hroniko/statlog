package com.hroniko.stats.entity.time;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TimeInterval {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private Long count;
    private Long middleTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // , timezone = "Europe/Moscow"
    private Date middleDate;

    public TimeInterval() {
    }

    public TimeInterval(Date startDate, Date endDate) {
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

}
