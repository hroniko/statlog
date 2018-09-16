package com.hroniko.stats.search;

import com.hroniko.stats.search.restentity.PutRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddToIndexService {
    final static String URI_SEARCH_ENGINE = "http://localhost:9200/servers/";

    public void addLogToIndex(String server, PutRequest log){
        String fullUri = URI_SEARCH_ENGINE + server + "/" + log.getId();
        new RestTemplate().put(fullUri, log);
//        Object obj = new RestTemplate().postForEntity("http://graph.facebook.com/pivotalsoftware", log, PutResponse.class);
    }


}
