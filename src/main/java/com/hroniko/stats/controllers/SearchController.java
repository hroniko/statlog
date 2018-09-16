package com.hroniko.stats.controllers;

import com.hroniko.stats.entity.OneLog;
import com.hroniko.stats.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class SearchController {
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/findLog", method = RequestMethod.GET)
    @ResponseBody
    List<OneLog> findLog (@RequestParam(value = "host", required = true) String hostname,
                          @RequestParam(value = "word", required = true) String word) {
        List<OneLog> result = searchService.findLogsContainsWord(hostname, word);
        return (result == null ? new ArrayList<>() : result);
    }

    @RequestMapping(value = "/fuzzyFindLog", method = RequestMethod.GET)
    @ResponseBody
    List<OneLog> fuzzyFindLog (@RequestParam(value = "host", required = true) String hostname,
                               @RequestParam(value = "fields", required = true) String[] fields,
                               @RequestParam(value = "word", required = true) String word) {
        List<OneLog> result = searchService.fuzzyFindLogsContainsWordInField(hostname, fields, word);
        return (result == null ? new ArrayList<>() : result);
    }

}
