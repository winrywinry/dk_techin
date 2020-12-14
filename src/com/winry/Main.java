package com.winry;

import sun.rmi.runtime.Log;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String path = System.getProperty("user.dir");

        CommonFunc func = new CommonFunc();
        List<LogVo> logList = func.readAFile(path);

        func.writeAFile(logList);
    }

}
