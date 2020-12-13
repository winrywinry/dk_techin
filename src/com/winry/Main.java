package com.winry;

import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String path = System.getProperty("user.dir");
        System.out.println("Working Directory = " + path);

        CommonFunc func = new CommonFunc();
        List<LogVo> logList = func.readAFile(path);

        Map<String, String> urlList = logList.stream()
                .map(x -> {
                    return func.getApiKey(x.getUrl());
                })
                .collect(Collectors.groupingBy(x -> x));

//        String url = func.getUrl(logList.get(0).toString());
//
//        List<String> urlList = logList.stream()
//                .map(x -> {
//                    x = func.getUrl(x);
//                    return x;
//                })
//                .map(x -> {
//                    x = func.getApiKey(x);
//                    return x;
//                })
//                .filter(x -> !x.equals(""))
//                .collect(Collectors.toList());
//
//        System.out.println(urlList.toString());


    }

}
