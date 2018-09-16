package com.hroniko.stats.repo;

import com.hroniko.stats.entity.OneLog;
// import org.springframework.context.annotation.Scope;
// import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// @Scope(value = "singleton")
// @Service
public class LogSaver {

    public LogSaver() {
    }

    private Map<String, String> logFilesMap = new HashMap<>();

    public String add(String fileName, String fileText){
        if (logFilesMap == null){
            logFilesMap = new HashMap<>();
        }
        if (logFilesMap.containsKey(fileName)){
            logFilesMap.remove(fileName);
        }
        logFilesMap.put(fileName, fileText);
        return fileName;
    }

    public String get(String fileName){
        return logFilesMap.get(fileName);
    }

    public void delete(String fileName){
        if(this.logFilesMap.containsKey(fileName))
            this.logFilesMap.remove(fileName);
    }

    public void clear(){
        logFilesMap = new HashMap<>();
    }

    public Boolean isContains(String fileName){
        return this.logFilesMap.containsKey(fileName);
    }

    public void print(){
        logFilesMap.forEach((k, v) -> System.out.println(k + " : " + v.length()));
    }

    //////////////////////

    private Map<BigInteger, OneLog> oneLogMap = new HashMap<>();
    private BigInteger id = new BigInteger("0");
    private BigInteger generateId(){
        id = id.add(BigInteger.ONE);
        activeId = id; //////
        return id;
    }

    public OneLog add(OneLog log){
        if (oneLogMap == null){
            oneLogMap = new HashMap<>();
        }
        if (log == null){
            return null;
        }
        if (log.getId() == null){
            log.setId(generateId());
        }
        oneLogMap.put(id, log);
        System.out.println(id + "  ~  " + log.getIp() + " ~ " + log.convertDateTimeAsString() + " ~ " + log.getRest());
        return log;
    }

    public OneLog get(BigInteger logId){
        return oneLogMap.get(logId);
    }

    public void delete(BigInteger logId){
        if(this.oneLogMap.containsKey(logId))
            this.oneLogMap.remove(logId);
    }


    // И мапа для активного лог-файла
    private Map<BigInteger, OneLog> activeLogMap = new HashMap<>();
    private BigInteger activeId = new BigInteger("0");
    private BigInteger generateActiveId(){
        activeId = activeId.add(BigInteger.ONE);
        return activeId;
    }

    public OneLog addActive(OneLog log){
        if (activeLogMap == null){
            activeLogMap = new HashMap<>();
        }
        if (log == null){
            return null;
        }
        if (log.getId() == null){
            log.setId(generateActiveId());
        }
        activeLogMap.put(activeId, log);
        System.out.println("a - " + activeId + "  ~  " + log.getIp() + " ~ " + log.convertDateTimeAsString() + " ~ " + log.getRest());
        return log;
    }

    public OneLog getActive(BigInteger logId){
        return activeLogMap.get(logId);
    }

    public void deleteActive(BigInteger logId){
        if(this.activeLogMap.containsKey(logId))
            this.activeLogMap.remove(logId);
    }

    public void clearActive(){
        // ЗАкомментировал 2018-05-10, так как часть данных терялась при пересоздании файла TBAPI на сервере
        // this.activeLogMap = new HashMap<>();
        // activeId = new BigInteger("0");
        // activeSize = 1L;
        activePosition = 1L;
    }

    // И еще храним предыдущий размер активного логфайла и позицию (номер строки), до которой дочитали в прошлый раз
    private Long activeSize = 1L;
    private Long activePosition = 1L;

    public Long getActiveSize() {
        return activeSize;
    }

    public void setActiveSize(Long activeSize) {
        this.activeSize = activeSize;
    }

    public Long getActivePosition() {
        return activePosition;
    }

    public void setActivePosition(Long activePosition) {
        this.activePosition = activePosition;
    }

    public Stream<OneLog> getAllLogAsStream(){
        List<OneLog> allLogList = new ArrayList<>(oneLogMap.values());
        List<OneLog> activeLogList = new ArrayList<>(activeLogMap.values());
        return Stream.concat(allLogList.stream(), activeLogList.stream());
    }

    /* ----------------------------------------------- */
    // И еще храним значения загруженности CPU и памяти
    private String usageCPU = "-";
    private String usageMemory = "-";

    public String getUsageCPU() {
        return usageCPU;
    }

    public void setUsageCPU(String usageCPU) {
        this.usageCPU = usageCPU;
    }

    public String getUsageMemory() {
        return usageMemory;
    }

    public void setUsageMemory(String usageMemory) {
        this.usageMemory = usageMemory;
    }
}
