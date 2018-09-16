package com.hroniko.stats.controllers;

import com.hroniko.stats.actions.LogicalProcessor;
import com.hroniko.stats.actions.StatProcessor;
import com.hroniko.stats.entity.OneLog;
import com.hroniko.stats.entity.time.TimeInterval;
import com.hroniko.stats.entity.time.TimeIntervalBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
public class StatController {


    @Autowired
    LogicalProcessor logicalProcessor;

    @Autowired
    StatProcessor statProcessor;

    @RequestMapping("/getAll")
    @ResponseBody
    List<String> getAll (@RequestParam(value = "host", required = true) String hostname) {
        List<String> result = statProcessor.getLog(hostname)
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .map(Object::toString)
                .collect(toList());
        return result;
    }

    @RequestMapping("/getGood")
    @ResponseBody
    List<String> getGood (@RequestParam(value = "host", required = true) String hostname) {
        List<String> result = statProcessor.getLogWithGoodResponce(hostname)
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .map(Object::toString)
                .collect(toList());
        return result;
    }

    @RequestMapping("/getBad")
    @ResponseBody
    List<String> bad (@RequestParam(value = "host", required = true) String hostname) {
        System.out.println("Bad rest rest");
        List<String> result = statProcessor.getLogWithBadResponce(hostname)
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .map(Object::toString)
                .collect(toList());
        // res.forEach(System.out::println);
        return result;
    }

    @RequestMapping("/getCountByIp")
    @ResponseBody
    Map getCountByIp (@RequestParam(value = "host", required = true) String hostname) {
        System.out.println("getCountByIp");

        Map<String, Long> map = statProcessor.getCountByIp(hostname);
        return map;
    }

    @RequestMapping("/getCountByUsername")
    @ResponseBody
    List getCountByUsername (@RequestParam(value = "host", required = true) String hostname) {
        // System.out.println("getCountByUsername");
        return statProcessor.getCountByUsername(hostname);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    List<String> getByName ( @RequestParam(value = "host", required = true) String hostname, @RequestParam(value = "user", required = true) String user) {
        List<String> result = statProcessor.getLog(hostname)
                .filter(log -> log.getHostname().equals(user))
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .map(Object::toString)
                .collect(toList());
        return (result == null ? new ArrayList<>() : result);
    }

    @RequestMapping(value = "/getEntity", method = RequestMethod.GET)
    @ResponseBody
    List<OneLog> getEntity (@RequestParam("host") String host) {
        List<OneLog> result = statProcessor.getLog(host)
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .limit(5)
                .collect(toList());
        return result;
    }

    /* Получение всех запросов в виде сущностей */
    @RequestMapping(value = "/getAllEntity", method = RequestMethod.GET)
    @ResponseBody
    List<OneLog> getAllEntity (@RequestParam("host") String host) {
        List<OneLog> result = statProcessor.getLog(host)
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                // .limit(15)
                .collect(toList());
        return result;
    }

    /* Получение всех запросов в виде сущностей начиная с номера такого-то */
    @RequestMapping(value = "/getAllEntityNext", method = RequestMethod.GET)
    @ResponseBody
    List<OneLog> getAllEntityNext (@RequestParam("host") String host, @RequestParam("position") Long position, @RequestParam("volume") Long volume) {
        List<OneLog> result = statProcessor.getLog(host)
                .filter(log -> log.getId().longValue() > position) // только те, номер которых выше
                .filter(log -> log.getId().longValue() <= (position + volume)) // и не превышая объем выборки
//                .sorted((el1, el2) -> Long.compare(el2.getId().longValue(), el1.getId().longValue()))
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .collect(toList());
        return result;
    }


    @RequestMapping(value = "/getCountStat", method = RequestMethod.GET)
    @ResponseBody
    List<TimeInterval> getCountStat (@RequestParam("host") String host,
                                     @RequestParam("start") String start,
                                     @RequestParam("end") String end,
                                     @RequestParam("interval") String interval) {
        return statProcessor.getCountRestByIntervals(host, start, end, interval);

    }

    @RequestMapping(value = "/getCountStatGood", method = RequestMethod.GET)
    @ResponseBody
    List<TimeInterval> getCountStatGood (@RequestParam("host") String host,
                                     @RequestParam("start") String start,
                                     @RequestParam("end") String end,
                                     @RequestParam("interval") String interval) {
        return statProcessor.getCountRestByIntervalsGood(host, start, end, interval);

    }

    @RequestMapping(value = "/getCountStatBad", method = RequestMethod.GET)
    @ResponseBody
    List<TimeInterval> getCountStatBad (@RequestParam("host") String host,
                                         @RequestParam("start") String start,
                                         @RequestParam("end") String end,
                                         @RequestParam("interval") String interval) {
        return statProcessor.getCountRestByIntervalsBad(host, start, end, interval);

    }

    /* Формирование точек графика для заданного интервала и шага */
    @RequestMapping(value = "/getCountStatGoodAndBad", method = RequestMethod.GET)
    @ResponseBody
    List<TimeIntervalBulk> getCountStatBulk(@RequestParam("host") String host,
                                             @RequestParam("start") String start,
                                             @RequestParam("end") String end,
                                             @RequestParam("interval") String interval) {
        return statProcessor.getCountRestByIntervalsBulk(host, start, end, interval);
    }

    /* Формирование точек графика для последних 24 часов и шага */
    @RequestMapping(value = "/getCountStatGoodAndBad24h", method = RequestMethod.GET)
    @ResponseBody
    List<TimeIntervalBulk> getCountStatBulk24h(@RequestParam("host") String host,
                                            @RequestParam("interval") String interval) {
        return statProcessor.getCountRestByIntervalsBulk(host, interval);

    }
    /* Получение текущего количества логов в сейвере заданного хоста */
    @RequestMapping("/getCountLogsInSaver")
    @ResponseBody
    Long getCountLogsInSaver (@RequestParam(value = "host", required = true) String hostname) {
        return statProcessor.getLog(hostname).count();
    }

    /*------------------------*/


    /* Получение количества всего, хороших и плохих запросов за все время */
    @RequestMapping("/getAllAndGoodAndBadCountRest")
    @ResponseBody
    public Map<String, String> getAllAndGoodAndBadCountRest(@RequestParam(value = "host", required = true) String hostname) {
        return statProcessor.getAllAndGoodAndBadCountRest(hostname);
    }


    /* Получение распределения по типу запросов (GET/POST/PUT/DELETE) за все время */
    @RequestMapping("/getCountOfGetPostPutDelete")
    @ResponseBody
    public Map<String, Long> getCountOfGetPostPutDelete(@RequestParam(value = "host", required = true) String hostname) {
        return statProcessor.getCountOfGetPostPutDelete(hostname);
    }
}
