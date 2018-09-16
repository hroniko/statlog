package com.hroniko.stats.search;

import com.hroniko.stats.entity.OneLog;
import com.hroniko.stats.search.restentity.GetResponse;
import com.hroniko.stats.search.restentity.getresponse.Hit;
import com.hroniko.stats.utils.NeoStringBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    final static String URI_SEARCH_ENGINE = "http://localhost:9200/servers/";

    // Поиск логов, содержащих слово, для заданного сервера
    public List<OneLog> findLogsContainsWord(String server, String word){
//        String fullUri = URI_SEARCH_ENGINE + server + "/_search";
        String fullUri = URI_SEARCH_ENGINE + server + "/_search" + "?size=10000"; // Чтобы все сущности возвращались (ну как минимум 10000)
        ResponseEntity<GetResponse> responseEntity = new RestTemplate().postForEntity(fullUri, generateRequestForAllSearch(word), GetResponse.class);
        GetResponse response = responseEntity.getBody();
        List<OneLog> result = response
                .getHits()
                .getHits()
                .stream()
                .map(Hit::get_source)
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .collect(Collectors.toList());
        return result;
    }

    // Поиск логов, содержащих слово в конкретном поле, для заданного сервера
    public List<OneLog> findLogsContainsWordInField(String server, String field, String word){
//        String fullUri = URI_SEARCH_ENGINE + server + "/" + server + "/_search";
        String fullUri = URI_SEARCH_ENGINE + server + "/_search" + "?size=10000"; // Чтобы все сущности возвращались (ну как минимум 10000)ы
        ResponseEntity<GetResponse> responseEntity = new RestTemplate().postForEntity(fullUri, generateRequestForFieldSearch(field, word), GetResponse.class);
        GetResponse response = responseEntity.getBody();
        List<OneLog> result = response
                .getHits()
                .getHits()
                .stream()
                .map(Hit::get_source)
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .collect(Collectors.toList());
        return result;
    }

    // Поиск логов, содержащих слово в конкретных полях, для заданного сервера, с использованием нечеткой логики
    public List<OneLog> fuzzyFindLogsContainsWordInField(String server, String[] fields, String word){
//        String fullUri = URI_SEARCH_ENGINE + server + "/" + server + "/_search";
        String fullUri = URI_SEARCH_ENGINE + server + "/_search" + "?size=100"; // Чтобы все сущности возвращались (ну как минимум 10000)ы
        ResponseEntity<GetResponse> responseEntity =  new RestTemplate().postForEntity(fullUri, generateRequestForFuzzySearch(fields, word), GetResponse.class);
        GetResponse response = responseEntity.getBody();
        List<OneLog> result = response
                .getHits()
                .getHits()
                .stream()
                .map(Hit::get_source)
                .sorted((el1, el2) -> el1.compare(el2, el1)) // сорировка по дате
                .collect(Collectors.toList());
        return result;
    }

    /* ---------------------------------------------------------- */
    // Генерирование запроса для поиска по всем полям
    private String generateRequestForAllSearch(String word){
        StringBuilder request = new StringBuilder();
        request.append("{");
            request.append("\"query\" : ");
                request.append("{");
                    request.append("\"query_string\" : ");
                        request.append("{");
                            request.append("\"query\" : ");
                            request.append("\""); request.append(word); request.append("\"");
                        request.append("}");
                request.append("}");
        request.append("}");
        return request.toString();
    }


    // Генерирование запроса для поиска по конкретному полю
    private String generateRequestForFieldSearch(String field, String word){
        StringBuilder request = new StringBuilder();
        request.append("{");
            request.append("\"query\" : ");
                request.append("{");
                    request.append("\"match\" : ");
                        request.append("{");
                            request.append("\""); request.append(field); request.append("\""); request.append(" : ");
                            request.append("\""); request.append(word); request.append("\"");
                request.append("}");
            request.append("}");
        request.append("}");
        return request.toString();
    }


    // Генерирование запроса для нечеткого поиска
    private String generateRequestForFuzzySearch(String[] fields, String word){

        /*
        {
            query: {
                more_like_this: {
                    fields: ["bodyIn", "bodyOut"],
                    like: request,
                    min_term_freq: 1,
                    max_query_terms: 12
                }
            }
        }
        * */

        NeoStringBuilder nsb = new NeoStringBuilder();
        return nsb
                .add("{")
                .add("\"query\" : ")
                .add("{")
                .add("\"more_like_this\" : ")
                .add("{")
                .add("\"fields\" : ")
                .add(Arrays.stream(fields)
                        .map(x -> "\"" + x + "\"")
                        .collect(Collectors.joining(", ", "[", "]"))
                )
                .add(",")
                .add("\"like\" : ")
                .add("\"")
                .add(word)
                .add("\",")
                .add("\"min_term_freq\" : ")
                .add("1,")
                .add("\"max_query_terms\" : ")
                .add("1200")
                .add("}")
                .add("}")
                .add("}")
                .toString();

        // Arrays.asList(fields).stream().map(x -> "\"" + x + "\"").collect(Collectors.joining(", ", "[", "]"));

    }




}
