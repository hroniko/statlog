package com.hroniko.stats.repo;

import com.hroniko.stats.actions.CommandLineSSHProcessor;
import com.hroniko.stats.utils.splitters.IpSplitter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.hroniko.stats.utils.constants.CommandConstants.GET_HOSTNAME_BY_ID;

@Scope(value = "singleton")
@Service
public class HostnameDictionary {

    private Map<String, String> hostnameMap = new HashMap<>();

    public String add(String ip, String hostname){
        if (hostnameMap == null){
            hostnameMap = new HashMap<>();
        }
        if (hostnameMap.containsKey(ip)){
            return ip; // hostnameMap.remove(ip);
        }
        if (hostname != null){
            hostnameMap.put(ip, hostname);
        }

        return ip;
    }

    public String get(String ip){
        return (hostnameMap.get(ip) == null ? "unnamed" : hostnameMap.get(ip));
    }

    public void delete(String ip){
        if(this.hostnameMap.containsKey(ip))
            this.hostnameMap.remove(ip);
    }

    public void clear(){
        hostnameMap = new HashMap<>();
    }

    public Boolean isContains(String ip){
        return this.hostnameMap.containsKey(ip);
    }



    public String convert(String server, String ip){
        if (!isContains(ip)){
            // если в словаре нет такого айпи, нужно его подгрузить и повесить в словарь:
            String hostname = getHostnameByIp(server, ip);
            add(ip, hostname);
        }
        return get(ip);
    }

    private String getHostnameByIp(String server, String ip){
        if (ip.contains("127.0.0.1")) return "portal";
        String hostname = CommandLineSSHProcessor.shell(server, GET_HOSTNAME_BY_ID + ip);
        return IpSplitter.split(hostname);
    }


    private static final Map<String, String> usernameMap = new HashMap<>();
    static {
        usernameMap.put("wsmvr-308", "Vitalii Bokov");
        usernameMap.put("wsmvr-072", "Alexander Kuritsyn");
        usernameMap.put("wsmvr-231", "Maksim Likhotin");
        usernameMap.put("wsmvr-302", "Andrei Donskikh");
        usernameMap.put("wsmvr-176", "Konstantin Sivolapov");
        usernameMap.put("wsmvr-108", "Iaroslav Katsenko");
        usernameMap.put("wsmvr-154", "Anatolii Bedarev");
    }

    public String getUsernameByHostmane(String hostname){
        if (usernameMap.containsKey(hostname)){
            return usernameMap.get(hostname);
        }
        return hostname;
    }

}
