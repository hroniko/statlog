package com.hroniko.stats.actions;

import com.hroniko.stats.entity.OneLog;
import com.hroniko.stats.entity.count.IpCount;
import com.hroniko.stats.entity.time.TimeInterval;
import com.hroniko.stats.entity.time.TimeIntervalBulk;
import com.hroniko.stats.repo.HostnameDictionary;
import com.hroniko.stats.repo.LogRepository;
import com.hroniko.stats.utils.converters.TimeConverter;
import com.hroniko.stats.utils.timeline.IntervalGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hroniko.stats.utils.constants.ConnectionConstants.HOSTNAME;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class StatProcessor {

    private final static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    @Autowired
    LogRepository logRepository;
    //LogSaver logSaver;

    @Autowired
    TimeConverter timeConverter;

    @Autowired
    HostnameDictionary hostnameDictionary;

    // Получение стрима логов, которые зафиксировали плохой ответ (не OK 200 статус)
    public Stream<OneLog> getLogWithBadResponce(String hostname){
        // return logSaver.getAllLogAsStream()
        return logRepository.get(hostname).getAllLogAsStream()
                .filter(log -> !log.getStatus().equals("200")); // Все, у которых статус не 200
    }

    // Получение стрима логов, которые зафиксировали хороший ответ (OK 200 статус)
    public Stream<OneLog> getLogWithGoodResponce(String hostname){
        // return logSaver.getAllLogAsStream()
        return logRepository.get(hostname).getAllLogAsStream()
                .filter(log -> log.getStatus().equals("200")); // Все, у которых статус 200
    }

    public Stream<OneLog> getLog(String hostname){
        // return logSaver.getAllLogAsStream();
        return logRepository.get(hostname).getAllLogAsStream();
    }

    // Группировака по IP всех логов (мапа логов)
    public Map<String, List<OneLog>> getGroupingByIp(String hostname){
        // return logSaver.getAllLogAsStream()
        return logRepository.get(hostname).getAllLogAsStream()
                .collect(groupingBy(OneLog::getIp));
    }

    // Группировака по IP всех логов (количество)
    public Map<String, Long> getCountByIp(String hostname){
        // return logSaver.getAllLogAsStream()
        return logRepository.get(hostname).getAllLogAsStream()
                .collect(groupingBy(OneLog::getIp, counting()));
    }

    // Группировака по username всех логов (количество)
    public List<IpCount> getCountByUsername(String hostname){
        Map<String, Long> countByIp = getCountByIp(hostname);
        List<IpCount> result = countByIp.keySet().stream()
                .map(item -> new IpCount(item, hostnameDictionary.convert(hostname, item), countByIp.get(item)))
                .sorted((el1, el2) -> Long.compare(el2.getCount(), el1.getCount()))
                .limit(10)
                .collect(Collectors.toList());
        return result;
    }


    // Определение распределения запросов по временным интервалам
    public List<TimeInterval> getCountRestByIntervals(String hostname, String startDate, String endDate, String splitInterval){
        Date start = timeConverter.simpleConvert(startDate);
        Date end = timeConverter.simpleConvert(endDate);
        List<TimeInterval> intervals = IntervalGenerator.create(start, end, splitInterval); // генерируем интервалы
        // Обходим список и распихиваем логи по интервалам
        intervals = intervals.stream()
                .peek(interval -> interval.setCount(getLog(hostname)
                        .filter(log -> log.getDatetime().after(interval.getStartDate()))
                        .filter(log -> log.getDatetime().before(interval.getEndDate()))
                        .count()
                        )
                  )
                .collect(Collectors.toList());
        // И еще к последнему надо добавить все, что накопилось
//        TimeInterval finalTimeInterval = intervals.get(intervals.size()-1);
//        finalTimeInterval.setCount(getLog(hostname)
//                .filter(log -> log.getDatetime().after(finalTimeInterval.getStartDate()))
//                .count());
        return intervals;


//        for (TimeInterval timeInterval : intervals) {
//            //
//            Long count = getLog(hostname)
//                    .filter(log -> log.getDatetime().after(timeInterval.getStartDate()))
//                    .filter(log -> log.getDatetime().before(timeInterval.getEndDate()))
//                    .count();
//            timeInterval.setCount(count);
//
//        }
    }


    // Определение распределения запросов по временным интервалам
    public List<TimeInterval> getCountRestByIntervalsGood(String hostname, String startDate, String endDate, String splitInterval){
        Date start = timeConverter.simpleConvert(startDate);
        Date end = timeConverter.simpleConvert(endDate);
        List<TimeInterval> intervals = IntervalGenerator.create(start, end, splitInterval); // генерируем интервалы
        // Обходим список и распихиваем логи по интервалам
        intervals = intervals.stream()
                .peek(interval -> interval.setCount(getLogWithGoodResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .collect(Collectors.toList());
        // System.out.println("Good count: " + intervals.size());
        return intervals;
    }


    // Определение распределения запросов по временным интервалам
    public List<TimeInterval> getCountRestByIntervalsBad(String hostname, String startDate, String endDate, String splitInterval){
        Date start = timeConverter.simpleConvert(startDate);
        Date end = timeConverter.simpleConvert(endDate);
        List<TimeInterval> intervals = IntervalGenerator.create(start, end, splitInterval); // генерируем интервалы
        // Обходим список и распихиваем логи по интервалам
        intervals = intervals.stream()
                .peek(interval -> interval.setCount(getLogWithBadResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .collect(Collectors.toList());
        // System.out.println("Bad count: " + intervals.size());
        return intervals;
    }

    // Определение распределения запросов по временным интервалам (плохих и хороших, балковый список)
    public List<TimeIntervalBulk> getCountRestByIntervalsBulk(String hostname, String startDate, String endDate, String splitInterval){
        Date start = timeConverter.simpleConvert(startDate);
        Date end = timeConverter.simpleConvert(endDate);
        List<TimeIntervalBulk> intervals = IntervalGenerator.createBulk(start, end, splitInterval); // генерируем интервалы
        intervals = intervals.stream()
                .peek(interval -> interval.addNewCount("good", getLogWithGoodResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .peek(interval -> interval.addNewCount("bad", getLogWithBadResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .collect(Collectors.toList());
        return intervals;
    }

    public List<TimeIntervalBulk> getCountRestByIntervalsBulk(String hostname, String splitInterval){
        Date end = new Date(); // Текущее время
        Date start = new Date(end.getTime() - 24 * 60  * 60 * 1000); // на 24 часа назад
        List<TimeIntervalBulk> intervals = IntervalGenerator.createBulk(start, end, splitInterval); // генерируем интервалы
        intervals = intervals.stream()
                .peek(interval -> interval.addNewCount("good", getLogWithGoodResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .peek(interval -> interval.addNewCount("bad", getLogWithBadResponce(hostname)
                                .filter(log -> log.getDatetime().after(interval.getStartDate()))
                                .filter(log -> log.getDatetime().before(interval.getEndDate()))
                                .count()
                        )
                )
                .collect(Collectors.toList());
        return intervals;
    }


    /* Получение мапы количества хороших, плохих и всего рестов */
    public Map<String, String> getAllAndGoodAndBadCountRest(String hostname){
        Map<String, String> result = new HashMap<>();
        Long goodCount = getLogWithGoodResponce(hostname).count();
        Long badCount = getLogWithBadResponce(hostname).count();
        Long allCount = goodCount + badCount;

        Double goodPercent = goodCount.doubleValue() / allCount * 100;
        Double badPercent = badCount.doubleValue() / allCount * 100;

        result.put("goodCount", goodCount.toString());
        result.put("badCount", badCount.toString());
        result.put("allCount", allCount.toString());
        result.put("goodPercent", decimalFormat.format(goodPercent));
        result.put("badPercent", decimalFormat.format(badPercent));
        return result;
    }


    /* Получение распределения по типу запросов (GET/POST/PUT/DELETE) */
    public Map<String, Long> getCountOfGetPostPutDelete(String hostname){
        return logRepository.get(hostname).getAllLogAsStream()
                .collect(groupingBy(OneLog::getType, counting()));
    }


}
