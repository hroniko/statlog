package com.hroniko.stats.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class OneLog implements Comparator<OneLog> {
    private BigInteger id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;
//    private String date;
//    private String time;
    private String ip;
    private String status;
    private String type;
    private String rest;
    private String bodyIn;
    private String bodyOut;

    private String hostname;

    public BigInteger getId() {
        return id;
    }

    public OneLog setId(BigInteger id) {
        this.id = id;
        return this;
    }

    public Date getDatetime() {
        return datetime;
    }

    public OneLog setDatetime(Date datetime) {
        this.datetime = datetime;
        return this;
    }

    //    public String getDate() {
//        return date;
//    }
//
//    public OneLog setDate(String date) {
//        this.date = date;
//        return this;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public OneLog setTime(String time) {
//        this.time = time;
//        return this;
//    }

    public String getIp() {
        return ip;
    }

    public OneLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OneLog setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getType() {
        return type;
    }

    public OneLog setType(String type) {
        this.type = type;
        return this;
    }

    public String getRest() {
        return rest;
    }

    public OneLog setRest(String rest) {
        this.rest = rest;
        return this;
    }

    public String getBodyIn() {
        return bodyIn;
    }

    public OneLog setBodyIn(String bodyId) {
        this.bodyIn = bodyId;
        return this;
    }

    public String getBodyOut() {
        return bodyOut;
    }

    public OneLog setBodyOut(String bodyOut) {
        this.bodyOut = bodyOut;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public OneLog setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String convertDateTimeAsString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.datetime);
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        strb.append(getId());
        strb.append(" ~ ");
        strb.append(getHostname());
        strb.append(" ~ ");
        strb.append(convertDateTimeAsString());
        strb.append(" ~ ");
//        strb.append(getDate());
//        strb.append(" ~ ");
//        strb.append(getTime());
//        strb.append(" ~ ");
        strb.append(getIp());
        strb.append(" ~ ");
        strb.append(getStatus());
        strb.append(" ~ ");
        strb.append(getRest());
        strb.append(" ~ ");
        strb.append(getBodyIn());
        strb.append(" ~ ");
        strb.append(getBodyOut());
        return strb.toString();
    }

    @Override
    public int compare(OneLog o1, OneLog o2) {
        if (o1.getDatetime().before(o2.getDatetime())) return -1;
        if (o1.getDatetime().after(o2.getDatetime())) return 1;

        // и тут еще раз, но по id
        return Long.compare(o1.getId().longValue(), o2.getId().longValue());
    }
}
