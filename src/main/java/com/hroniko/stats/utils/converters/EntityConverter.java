package com.hroniko.stats.utils.converters;

import com.hroniko.stats.entity.OneLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityConverter {

    public static OneLog convert(String rawLog){
        if (rawLog == null) return null;
        if (rawLog.length() < 10) return null;

        OneLog log = new OneLog();

        String[] logArr = rawLog.split("\r\n|\r|\n"); // Сплитим по строкам, в идеале не больше трех строк
        try {
            // Первую строку надо распилить по пробелам
            if (logArr.length > 0)
            {
                String[] firstStr = logArr[0].split("\\s+");
                if ((firstStr.length < 7) || (firstStr[3].length() < 3)) // если не хватает полей или айпи адрес пуст (то есть это не лог), то отдадим нулл
                    return null;
                // Иначе просетчиваем все поля
//                log.setDate(firstStr[0])
//                        .setTime(firstStr[1].split(",")[0])

                log.setDatetime(stringToDate(firstStr[0] + " " + firstStr[1].split(",")[0]))
                        .setIp(firstStr[3]
                                .replace("[", "")
                                .replace("]", "")
                        )
                        //.setHostname(IpToHostnameConverter.convert(log.getIp()))
                        .setStatus(firstStr[4]
                                .replace("STATUS:", "")
                        )
                        .setType(firstStr[5])
                        .setRest(firstStr[6])
                ;
            }

            // Вторую строку надо просто обрезать спереди
            if (logArr.length > 1){
                if (logArr[1].contains("REQUEST PAYLOAD:")){
                    log.setBodyIn(logArr[1].replace("REQUEST PAYLOAD:", ""));
                }
                if (logArr[1].contains("RESPONSE PAYLOAD:")){ // для случая запроса, у которого нет боди в запросе
                    log.setBodyOut(logArr[1].replace("RESPONSE PAYLOAD:", ""));
                }
            }

            // Третью строку надо тоже обрезать спереди
            if (logArr.length > 2){
                log.setBodyOut(logArr[2].replace("RESPONSE PAYLOAD:", ""));
            }

        } catch (Exception e){
            return null;
        }

        return log;
    }


    /* Utils */
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date stringToDate(String serverDateString){
        try{
            return dateFormat.parse(serverDateString);
        } catch (Exception e){
            return new Date();
        }
    }

}
