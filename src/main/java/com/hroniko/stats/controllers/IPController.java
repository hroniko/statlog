package com.hroniko.stats.controllers;

import com.hroniko.stats.actions.IPProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IPController {

    @Autowired
    IPProcessor ipProcessor;

    /* Получение внутреннего (vpn) ip клиента, возвращает 127.0.0.1, если клиент на той же машине, что и сервер!!!! */
    @RequestMapping("/getMyIp")
    @ResponseBody
    public static Map<String, String> getRequestRemoteAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        Map<String, String> result = new HashMap<>();
        result.put("ip", request.getRemoteAddr());
        return result;
    }

    /* Получение информации о клиенте */
    @RequestMapping("/getClientIpInfo")
    @ResponseBody
    public Map<String, String> getClientIpInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        return ipProcessor.getClientIpInfo(request);
    }
}
