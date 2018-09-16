package com.hroniko.stats.actions;

import com.hroniko.stats.local.RWLocalFiles;
import com.hroniko.stats.repo.HostnameDictionary;
import com.hroniko.stats.repo.LogRepository;
import com.hroniko.stats.repo.LogSaver;
import com.hroniko.stats.search.AddToIndexService;
import com.hroniko.stats.search.restentity.PutRequest;
import com.hroniko.stats.utils.converters.EntityConverter;
import com.hroniko.stats.utils.splitters.LogSplitter;
import com.hroniko.stats.utils.converters.TimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hroniko.stats.utils.constants.CommandConstants.*;
import static com.hroniko.stats.utils.constants.ConnectionConstants.ACTIVE_LOGFILE;
import static com.hroniko.stats.utils.constants.ConnectionConstants.HOSTNAME;
import static com.hroniko.stats.utils.constants.ConnectionConstants.HOSTNAME_LIST;

@Scope(value = "singleton")
@Service
public class LogicalProcessor {

    @Autowired
    LogRepository logRepository;
    // LogSaver logSaver;
    @Autowired
    TimeConverter timeConverter;
    @Autowired
    HostnameDictionary hostnameDictionary;
    @Autowired
    ServerMonitor serverMonitor;
    @Autowired
    AddToIndexService addToIndexService;

    private Boolean flagReadFiles = false;

    private Boolean flagWork = false; // Флаг, указывающий, что текущий шаг не выполнен

    public void runForAllHostname(){
        if (flagWork) return; // Если предыдущее задание не выполнено, новое не запускаем
        flagWork = true;

        String[] hostnames = HOSTNAME_LIST.split(",");
        List<String> hostnameList = Arrays.asList(hostnames);
        for (String hostname : hostnameList) {
            if (hostname != null){
                run(hostname.trim());
            }

        }
        flagWork = false;
        flagReadFiles = true; // чтобы повторно не читать
//
//        Arrays.stream(HOSTNAME_LIST.split(","))
//                .filter(x -> (x != null))
//                .map(String::trim)
//                .map(host -> run(host))
//                .collect(Collectors.toList());
    }

    public String run(String hostname) {



        // 0 Добавляем новый сейвер в репозиторий, если его там нет
        logRepository.add(hostname);
        LogSaver logSaver = logRepository.get(hostname);

        // 1 Определяем разницу во времени между удаленным сервером TBAPI и локальным компьютером
        String serverTime = CommandLineSSHProcessor.shell(hostname, GET_SERVER_DATETIME);
        timeConverter.setDeltaTime(serverTime);

        // 2 Поднимаем все файлы - локальные копии из локальной директории
        if (!flagReadFiles){
            RWLocalFiles.getAllFileNamesInLocalDir(hostname)
                    // Вешаем названия в сейвер, чтобы знать, какие файлы не грузить с сервера
                    .filter(str -> !str.equals(ACTIVE_LOGFILE))
                    .filter(str -> !logSaver.isContains(str))
                    // .limit(5)  //// TODO Временно для проверки поисковых запросов, УБРАТЬ! 2018-05-14
                    .peek(str ->  logSaver.add(str, str)) // просто вешаем эти имена в мапе, чтобы знать, что эти файлы больше не надо грузить с сервера
                    .map(filename -> RWLocalFiles.readFromFile(hostname, filename)) // загружаем текст из файлов, тут вернется поток строк для каждого файла
                    .map(text -> LogSplitter.split(text, "[^\"\']20\\d\\d([-])\\d\\d([-])\\d\\d\\s\\d\\d")) // Разделяем по датам все записи
                    .flatMap(List::stream) // Чтобы работать не с потоком списков логов, а отдельными логами
                    .map(EntityConverter::convert) // Конвертируем в поток логов-сущностей
                    .filter(log -> (log != null))
                    .map(log -> log.setDatetime(timeConverter.convert(log.getDatetime())))  // Конвертируем в правильное время - время локальной машины
                    .map(log -> log.setHostname(hostnameDictionary.convert(hostname, log.getIp())))
                    .map(log -> log.setHostname(hostnameDictionary.getUsernameByHostmane(log.getHostname())))
                    .peek(log -> logSaver.add(log)) // и вешаем все сущности в сейвер
                    //.peek(log -> addToIndexService.addLogToIndex(hostname, new PutRequest(log))) // и отправляем все логи в поисковый движок
                    .peek(log -> {
                        try{
                            addToIndexService.addLogToIndex(hostname, new PutRequest(log));
                        } catch (Exception e){
                            // Что-то сделать
                        }

                    }) // и отправляем все логи в поисковый движок

                    .collect(Collectors.toList()); // Чтобы запустить расчет
        }




        // 3 Получаем все файлы (имена) с серверв и выбираем только те, которые являются логами TBAPI
        String allFiles = CommandLineSSHProcessor.shell(hostname, GET_ALL_FILE_NAME);
        List<String> allFileList = Arrays.asList( allFiles.split("\r\n|\r|\n"));
        List<String> allLogFiles = allFileList.stream()
                .map(String::trim)
                .filter(str -> str.contains("rest_tbapi"))
                .filter(str -> str.contains(".log"))
                .sorted()
                .collect(Collectors.toList());

        // Если в папке нет файлов тбапи логов, то дальше нет смысла выполнять вычисления
        if (allLogFiles == null || allLogFiles.isEmpty()) return "none";
         System.out.println(allLogFiles);

        // Оставляем только те, которых нет в локальной памяти в сейвере и которые не rest_tbapi.log (не основной файл, в который сейчас ведется запись),
        // и для них получаем содержимое

        //List<OneLog> logFiles =
                allLogFiles.stream()
                .filter(filename -> !filename.equals(ACTIVE_LOGFILE))
                .filter(filename -> !logSaver.isContains(filename))
                .peek(filename ->  logSaver.add(filename, filename)) // просто вешаем эти имена в мапе, чтобы знать, что эти файлы больше не надо грузить с сервера
                .map(filename -> {
                            String text = CommandLineSSHProcessor.shell(hostname, GET_TEXT_FOR_FILE + filename); // загружаем текст из файлов
                            List<String> splitText = LogSplitter.split(text, "[^\"\']20\\d\\d([-])\\d\\d([-])\\d\\d\\s\\d\\d"); // Разделяем по датам все записи
                            RWLocalFiles.writeToFile(HOSTNAME, filename, splitText.stream()); // А тут сохраняем их локальные копии на диске, чтобы в новом сеансе не грузить
                            return splitText;
                        })
//                .map(str -> CommandLineSSHProcessor.shell(GET_TEXT_FOR_FILE + str)) // загружаем текст из файлов
//                .map(text -> LogSplitter.split(text, "[^\"\']20\\d\\d([-])\\d\\d([-])\\d\\d\\s\\d\\d")) // Разделяем по датам все записи
                .flatMap(List::stream) // Чтобы работать не с потоком списков логов, а отдельными логами
                .map(EntityConverter::convert) // Конвертируем в поток логов-сущностей
                .filter(log -> (log != null))
                .map(log -> log.setDatetime(timeConverter.convert(log.getDatetime())))  // Конвертируем в правильное время - время локальной машины
                // .map(log -> log.setTime(timeConverter.convert(log.getTime())))  // Конвертируем в правильное время - время локальной машины
                .map(log -> log.setHostname(hostnameDictionary.convert(hostname, log.getIp())))
                .map(log -> log.setHostname(hostnameDictionary.getUsernameByHostmane(log.getHostname())))
                .peek(log -> logSaver.add(log)) // и вешаем все сущности в сейвер
                        //.peek(log -> addToIndexService.addLogToIndex(hostname, new PutRequest(log))) // и отправляем все логи в поисковый движок
                        .peek(log -> {
                            try{
                                addToIndexService.addLogToIndex(hostname, new PutRequest(log));
                            } catch (Exception e){
                                // Что-то сделать
                            }

                        }) // и отправляем все логи в поисковый движок
                .collect(Collectors.toList()); // Чтобы запустить расчет


        // 4 Теперь забираем новые строки из активного лог-файла
        // Определяем, сколько всого строк в файле
        String stringCountInActiveLogFile = CommandLineSSHProcessor.shell(hostname, GET_WORD_COUNT_IN_FILE + ACTIVE_LOGFILE).split(" ")[0];
        Long stringCount = new Long(stringCountInActiveLogFile);
        // Нужно проверить, если их стало меньше, то лог файл заново создан, надо активировтаь сброс из сейвера активного хранилища
        if (stringCount < logSaver.getActiveSize()){
            logSaver.clearActive();
        } else { // иначе проверяем позицию, вдруг мы уже все прочитали из этого файла
            if (stringCount > logSaver.getActivePosition()){
                String newText = CommandLineSSHProcessor.shell(hostname, GET_TEXT_FOR_ACTIVE_FILE_WITH_POSITION  // загружаем текст из активного файла
                        .replace("pos1", logSaver.getActivePosition().toString())
                        .replace("pos2", stringCount.toString())
                        + ACTIVE_LOGFILE);

                LogSplitter.split(newText, "[^\"\']20\\d\\d([-])\\d\\d([-])\\d\\d\\s\\d\\d").stream() // Разделяем по датам все записи
                        .map(EntityConverter::convert) // Конвертируем в поток логов-сущностей
                        .filter(log -> (log != null))
                        .map(log -> log.setDatetime(timeConverter.convert(log.getDatetime())))  // Конвертируем в правильное время - время локальной машины
                        //.map(log -> log.setTime(timeConverter.convert(log.getTime())))  // Конвертируем в правильное время - время локальной машины
                        .map(log -> log.setHostname(hostnameDictionary.convert(hostname, log.getIp())))
                        .map(log -> log.setHostname(hostnameDictionary.getUsernameByHostmane(log.getHostname())))
                        .peek(log -> logSaver.addActive(log)) // и вешаем все сущности в сейвер
                        //.peek(log -> addToIndexService.addLogToIndex(hostname, new PutRequest(log))) // и отправляем все логи в поисковый движок
                        .peek(log -> {
                            try{
                                addToIndexService.addLogToIndex(hostname, new PutRequest(log));
                            } catch (Exception e){
                                // Что-то сделать
                            }

                        }) // и отправляем все логи в поисковый движок
                        .collect(Collectors.toList()); // Чтобы запустить расчет
                logSaver.setActivePosition(stringCount);
                logSaver.setActiveSize(stringCount);
            }
        }

        // 5 Подгружаем иенформацию о загруженности процессора и использованной памяти на сервере
//        logSaver.setUsageCPU(serverMonitor.getCPUPercent(hostname));
//        logSaver.setUsageMemory(serverMonitor.getMemoryPercent(hostname));

        // System.out.println(size);

        logSaver.print();

        return  "ok";
    }


}
