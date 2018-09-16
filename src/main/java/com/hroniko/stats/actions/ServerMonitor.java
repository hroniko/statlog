package com.hroniko.stats.actions;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

import static com.hroniko.stats.utils.constants.CommandConstants.*;

@Service
public class ServerMonitor {

    private final static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    /* Получение загруженности процессора */
    public String getCPUPercent(String hostname){
        String usageCPU= CommandLineSSHProcessor.shell(hostname, GET_CPU_USAGE_PERCENT_v3);
        if (usageCPU.equals("")) return "-";
        Double usageCPUd = new Double(usageCPU);
        usageCPU = decimalFormat.format(usageCPUd);
        return usageCPU;
    }

    /* Получение загруженности памяти */
    public String getMemoryPercent(String hostname){
        String totalMem = CommandLineSSHProcessor.shell(hostname, GET_MEMORY_TOTAL);
        if (totalMem.equals("")) return "-";
        Double totalMemory = new Double(totalMem); // в килобайтах


        String freeMem = CommandLineSSHProcessor.shell(hostname, GET_MEMORY_FREE);
        if (freeMem.equals("")) return "-";
        Double freeMemory = new Double(freeMem); // в килобайтах

        Double usageMemory = (totalMemory - freeMemory)/totalMemory * 100;
        String usageMem = decimalFormat.format(usageMemory);

        return usageMem;
    }

}
