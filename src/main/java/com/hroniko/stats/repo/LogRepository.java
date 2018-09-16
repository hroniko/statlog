package com.hroniko.stats.repo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Scope(value = "singleton")
@Service
public class LogRepository {

    private Map<String, LogSaver> logSaverMap = new HashMap<>();

    public String add(String hostname){
        if (logSaverMap == null){
            logSaverMap = new HashMap<>();
        }
        if (logSaverMap.containsKey(hostname)){
            return hostname; // !!! Заново не создаем, не делаем двойную работу
        }
        logSaverMap.put(hostname, new LogSaver());
        return hostname;
    }

    public LogSaver get(String hostname){
        return logSaverMap.get(hostname);
    }

    public void delete(String hostname){
        if(this.logSaverMap.containsKey(hostname))
            this.logSaverMap.remove(hostname);
    }

    public void clear(){
        logSaverMap = new HashMap<>();
    }

    public Boolean isContains(String hostname){
        return this.logSaverMap.containsKey(hostname);
    }

}
