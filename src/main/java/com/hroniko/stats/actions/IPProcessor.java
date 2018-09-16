package com.hroniko.stats.actions;

import com.hroniko.stats.repo.HostnameDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.hroniko.stats.utils.constants.ConnectionConstants.HOSTNAME;

@Service
public class IPProcessor {

    @Autowired
    HostnameDictionary hostnameDictionary;

    public Map<String, String> getClientIpInfo(HttpServletRequest request){
        Map<String, String> result = new HashMap<>();
        String innerIp = request.getRemoteAddr();
        if (innerIp == null){
            result.put("ip", "undefined ip");
            result.put("hostname", "unnamed pc");
            result.put("username", "unnamed user");
            return result;
        }
        if (innerIp.equals("127.0.0.1")){
            result.put("ip", "localhost");
            result.put("hostname", "local pc");
            result.put("username", "local user");
            return result;
        }
        result.put("ip", innerIp);
        // И нужно узнать имя хоста и имя пользователя:
        String hostname = hostnameDictionary.convert(HOSTNAME, innerIp);
        String username = hostnameDictionary.getUsernameByHostmane(hostname);
        result.put("hostname", hostname);
        result.put("username", username);
        return result;

    }

}
