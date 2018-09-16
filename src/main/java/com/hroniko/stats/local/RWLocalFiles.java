package com.hroniko.stats.local;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Класс для чтени и записи логов в локальную директорию программы
public class RWLocalFiles {
    private  static final String PATH_TO_DIR = "."+ File.separator + "src"+ File.separator + "main"+ File.separator + "resources"+ File.separator + "locallog";

    /* Запись потока строк в файл */
    public static Boolean writeToFile(String serverName, String fileName, Stream<String> streamRawLogs){
        Path pathToFile = Paths.get(PATH_TO_DIR + File.separator + serverName + File.separator + fileName);
        try {
            Files.write(pathToFile, streamRawLogs.collect(Collectors.toList()), StandardCharsets.UTF_8);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    /* Чтение одного файла в стринг */
    public static String readFromFile(String serverName, String fileName){
        Path pathToFile = Paths.get(PATH_TO_DIR + File.separator + serverName + File.separator + fileName);
        try (Stream<String> lineStream = Files.lines(pathToFile, StandardCharsets.UTF_8)) {
            //List<String> res = lineStream.collect(Collectors.toList());
            String res = lineStream.collect(Collectors.joining(System.lineSeparator()));
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    /* Чтение одного файла в список */
    public static List<String> readFromFile(File file){
        Path pathToFile = Paths.get(file.getPath());
        try (Stream<String> lineStream = Files.lines(pathToFile, StandardCharsets.UTF_8)) {
            return lineStream.collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    /* Чтение списка файла в мапу списков */
    public static Map<String, List<String>> readAllFiles(List<File> files){
        Map<String, List<String>> result = new HashMap<>();
        files.forEach(file -> result.put(file.getName(), readFromFile(file)));
//        for (File file : files) {
//            result.put(file.getName(), readFromFile(file));
//        }
        return result;
    }

    /* Чтение списка файла в мапу списков */
    public static Map<String, List<String>> readAllFiles(String directory){
        List<File> files = getAllFilesInLocalDir(directory);
        Map<String, List<String>> result = new HashMap<>();
        files.forEach(file -> result.put(file.getName(), readFromFile(file)));
        return result;
    }

    /* Получение списка всех файлов из локальной директории */
    public static List<File> getAllFilesInLocalDir(String directory){
        File dir = new File(PATH_TO_DIR + File.separator + directory);
        File[] arrFiles = dir.listFiles();
        return Arrays.asList(arrFiles);
        // File dir = new File(getClass().getClassLoader().getResource("myFolderName").getFile());
    }

    /* Получение списка всех файлов из локальной директории */
    public static Stream<String> getAllFileNamesInLocalDir(String directory){
        File dir = new File(PATH_TO_DIR + File.separator + directory);
        File[] arrFiles = dir.listFiles();
        if (arrFiles == null) return null;
        List<File> files = Arrays.asList(arrFiles);
        return files.stream()
                .map(File::getName);
    }
}
