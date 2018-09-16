package com.hroniko.stats.utils.converters;

import com.hroniko.stats.actions.CommandLineSSHProcessor;
import com.hroniko.stats.repo.HostnameDictionary;
import com.hroniko.stats.utils.splitters.IpSplitter;

import static com.hroniko.stats.utils.constants.CommandConstants.GET_HOSTNAME_BY_ID;
// unused now
public class IpToHostnameConverter {

    static HostnameDictionary hostnameDictionary ;

    public static String convert(String server, String ip){
        if (!hostnameDictionary.isContains(ip)){
            // если в словаре нет такого айпи, нужно его подгрузить и повесить в словарь:
            String hostname = getHostnameByIp(server, ip);
            hostnameDictionary.add(ip, hostname);
        }
        return hostnameDictionary.get(ip);
    }

    private static String getHostnameByIp(String server, String ip){
        String hostname = CommandLineSSHProcessor.shell(server, GET_HOSTNAME_BY_ID + ip);
        return IpSplitter.split(hostname);
    }
}
