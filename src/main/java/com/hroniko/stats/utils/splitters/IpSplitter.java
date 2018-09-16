package com.hroniko.stats.utils.splitters;

import java.util.Arrays;
import java.util.List;

public class IpSplitter {

    private final static String NC_POSTFIX = "netcracker.com";

    public static String split(String rowResult){
        // 154.10.234.10.in-addr.arpa domain name pointer wsmvr-154.netcracker.com.
        if (rowResult == null) return "unnamed host";
        if (rowResult.contains("not found")) return "unnamed host";
        // if (rowResult.contains("127.0.0.1")) return "portal";
        // if (rowResult.contains("wsmvr-154")) return "anbe0118";

        String[] splitText = rowResult.split(" ");
        List<String> splitList = Arrays.asList(splitText);
        return splitList.stream()
                .filter(str -> str.toLowerCase().contains(NC_POSTFIX))
                .map(String::trim)
                .map(str -> str.replace(".netcracker.com.", ""))
                .findFirst()
                .orElse("unnamed host");
    }

}
