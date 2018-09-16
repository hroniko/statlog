package com.hroniko.stats.utils.constants;

public interface CommandConstants {
    String GET_SERVER_TIME = "date";
    String GET_SERVER_DATETIME ="date +'%Y-%m-%d %_H:%M:%S'";
    String GET_ALL_FILE_NAME = "ls /u02/netcracker/tbapi/tbapi-spring-boot";
    String GET_TEXT_FOR_FILE = "cat /u02/netcracker/tbapi/tbapi-spring-boot/";
    String GET_WORD_COUNT_IN_FILE = "wc -l /u02/netcracker/tbapi/tbapi-spring-boot/";
    String GET_TEXT_FOR_ACTIVE_FILE_WITH_POSITION = "sed -n pos1,pos2p /u02/netcracker/tbapi/tbapi-spring-boot/";
    String GET_HOSTNAME_BY_ID = "host ";

    /* Server Monitor's commands */
    String GET_CPU_USAGE_PERCENT_v1 = "top -bn1 | grep \"Cpu(s)\" | sed \"s/.*, \\([0-9,\\.]*\\)%* id.*/\\1/\" | awk '{print 100 - $1\"%\"}'"; // 16%
    String GET_CPU_USAGE_PERCENT_v2 = "top -bn1 | awk '/Cpu/ { cpu = 100 - $8 \"%\" }; END   { print cpu }'"; // 16%
    String GET_CPU_USAGE_PERCENT_v3 = "grep 'cpu ' /proc/stat | awk '{usage=($2+$4)*100/($2+$4+$5)} END {print usage}'"; // 14.874 without persent %
    String GET_CPU_USAGE_PERCENT_v4 = "grep 'cpu ' /proc/stat | awk '{usage=($2+$4)*100/($2+$4+$5)} END {print usage \"%\"}'"; // 14.874%

    String GET_MEMORY_TOTAL = "cat /proc/meminfo | grep MemTotal | awk '{print $2}'"; // return in kb
    String GET_MEMORY_FREE = "cat /proc/meminfo | grep MemFree | awk '{print $2}'"; // return in kb
}
