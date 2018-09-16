package com.hroniko.stats.utils.converters;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Scope(value = "singleton")
@Service
public class TimeConverter {
    private static Long deltaTime = 0L;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setDeltaTime(String serverDateString){
        Date localDate = new Date();
        try{
            Date serverDate =  dateFormat.parse(serverDateString);
            deltaTime = localDate.getTime() - serverDate.getTime();
        } catch (Exception e){
            deltaTime = 0L;
        }
    }

    public String convert(String serverDateTime){
        try{
            Date serverDate =  dateFormat.parse(serverDateTime);
            serverDate = new Date(serverDate.getTime() + deltaTime);
            return dateFormat.format(serverDate);
        } catch (Exception e){
            return serverDateTime;
        }
    }

    public Date convert(Date serverDateTime){
        try{
            return serverDateTime =  new Date(serverDateTime.getTime() + deltaTime);
        } catch (Exception e){
            return serverDateTime;
        }
    }

    public String onlyTime(String dateTime){
        Pattern pt = Pattern.compile("\\d\\d([:])\\d\\d([:])\\d\\d");
        Matcher mt = pt.matcher(dateTime);
        if (mt.find()) {
            return mt.group();
        } else {
            return dateTime;
        }
    }

    // А это чтобы просто конвертировать строку в дату без сдвига на дельту
    public Date simpleConvert(String dateTime){
        try{
            return dateFormat.parse(dateTime);
        } catch (Exception e){
            return new Date();
        }
    }


}
