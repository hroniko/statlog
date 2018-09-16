package com.hroniko.stats.controllers;

import com.hroniko.stats.actions.ServerMonitorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class ServerMonitorController {

    @Autowired
    ServerMonitorProcessor serverMonitorProcessor;

    /* Получение информации о загруженности сервера, количестве пользователей, отправивших запросы за последние 30 мин,
     * и количестве запросов за последние 30 минут */
    @RequestMapping("/getServerMonitor")
    @ResponseBody
    public Map<String, String> getServerMonitor(@RequestParam(value = "host", required = true) String hostname) {
        return serverMonitorProcessor.getServerMonitor(hostname);
    }
}
