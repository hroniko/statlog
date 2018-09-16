package com.hroniko.stats.search.restentity;

import com.hroniko.stats.entity.OneLog;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PutRequest extends OneLog {

    public PutRequest(OneLog log) {
        this.setId(log.getId());
        this.setDatetime(log.getDatetime());
        this.setIp(log.getIp());
        this.setStatus(log.getStatus());
        this.setType(log.getType());
        this.setRest(log.getRest());
        this.setBodyIn(log.getBodyIn());
        this.setBodyOut(log.getBodyOut());
        this.setHostname(log.getHostname());
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        strb.append("{");
            strb.append("\"id\":\""); strb.append(getId()); strb.append("\",");
            strb.append("\"hostname\":\""); strb.append(getHostname()); strb.append("\",");
            strb.append("\"datetime\":\""); strb.append(convertDateTimeAsString()); strb.append("\",");
            strb.append("\"ip\":\""); strb.append(getIp()); strb.append("\",");
            strb.append("\"status\":\""); strb.append(getStatus()); strb.append("\",");
            strb.append("\"rest\":\""); strb.append(getRest()); strb.append("\",");
            strb.append("\"bodyIn\":\""); strb.append(getBodyIn()); strb.append("\",");
            strb.append("\"bodyOut\":\""); strb.append(getBodyOut()); strb.append("\"");

        strb.append("}");

        return strb.toString();
    }
}
