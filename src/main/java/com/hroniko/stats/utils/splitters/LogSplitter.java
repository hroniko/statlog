package com.hroniko.stats.utils.splitters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// для того, чтобы сплитить по разделителю, не удаляя разделители
public class LogSplitter {

    public static List<String> split(CharSequence input, String pattern) {
        List<String> result = split(input, Pattern.compile(pattern));
        return result;
    }

    private static List<String> split(CharSequence input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        int start = 0;
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(input.subSequence(start, matcher.start()).toString());
            result.add(matcher.group());
            start = matcher.end();
        }
        if (start != input.length()) result.add(input.subSequence(start, input.length()).toString());

        List newResult = new ArrayList();
        for (int i = 2; i < result.size(); i=i+2){
            newResult.add(result.get(i-1).replace("\n", "") + result.get(i)); // "\n" - удаляем, там они лишние
        }

        return newResult;

        //return result.toArray(new String[0]);
    }
}
