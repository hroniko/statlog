package com.hroniko.stats.entity.count;

public class IpCount {
    private String ip;
    private String name;
    private Long count;

    public IpCount() {
    }

    public IpCount(String ip, String name, Long count) {
        this.ip = ip;
        this.name = name;
        this.count = count;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
