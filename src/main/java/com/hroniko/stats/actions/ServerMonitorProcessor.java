package com.hroniko.stats.actions;

import com.hroniko.stats.entity.OneLog;
import com.hroniko.stats.repo.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ServerMonitorProcessor {

    @Autowired
    LogRepository logRepository;

    @Autowired
    ServerMonitor serverMonitor;

    public Map<String, String> getServerMonitor(String hostname) {

        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - 30 * 60 * 1000); // на 30 минут назад
        Map<String, String> result = new HashMap<>();
//        result.put("cpu", logRepository.get(hostname).getUsageCPU());
//        result.put("memory", logRepository.get(hostname).getUsageMemory());

        result.put("cpu", serverMonitor.getCPUPercent(hostname));
        result.put("memory", serverMonitor.getMemoryPercent(hostname));

        // Определяем количество уникальных клиентов, отправлявших запрос на сервер в течение последних 30 минут:
        Integer user30minCount = logRepository.get(hostname).getAllLogAsStream()
                .filter(log -> log.getDatetime().after(startDate))
                .collect(groupingBy(OneLog::getIp, counting())).size();
        result.put("user30min", user30minCount.toString());

        // Определяем количество запросов, отправленных на сервер за последние 30 минут
        Long rest30minCount = logRepository.get(hostname).getAllLogAsStream()
                .filter(log -> log.getDatetime().after(startDate)).count();
        result.put("rest30min", rest30minCount.toString());

        return result;
    }

}
