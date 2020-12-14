package com.winry;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CommonFunc {
    private final String INPUTLOG = "input.log";
    private final String OUTPUTLOG = "output.log";
    private String PATH;

    public void setPATH(String path) {
        this.PATH = path;
    }
    public String getPATH() {
        return PATH;
    }

    /**
     * 로그 파일을 읽음.
     * @param path
     * @return
     */
    public List<LogVo> readAFile(String path) {
        setPATH(path);
        String filePath = getPATH() + "/"+ INPUTLOG;

        BufferedReader reader = null;
        List<LogVo> resultList = new ArrayList<>();

        try {
            File file = new File(filePath);

            if (file.exists()){
                reader = new BufferedReader(new FileReader(file));
                boolean readYn = true;
                while (readYn) {
                    String line = reader.readLine();
                    if (line == null) {
                        readYn = false;
                    } else {
                        LogVo vo = new LogVo();
                        String[] arrVal = line.split("\\[");
                        for (int i = 1; i<arrVal.length; i++) {
                            if (i==1) vo.setStatus(arrVal[i].replaceAll("\\]", ""));
                            if (i==2) vo.setUrl(arrVal[i].replaceAll("\\]", ""));
                            if (i==3) vo.setBrowser(arrVal[i].replaceAll("\\]", ""));
                            if (i==4) vo.setTimestamp(arrVal[i].replaceAll("\\]", ""));
                        }
                        resultList.add(vo);
                    }
                }
            }
        } catch (Exception e) {
            // Exception 처리
            System.out.println("파일을 찾을 수 없습니다.::"+ e.getMessage());
        } finally {
            try {
                reader.close();
           } catch (IOException e) {
                // Exception 처리
                System.out.println("파일을 찾을 수 없습니다.::"+ e.getMessage());
            }
        }

        return resultList;
    }

    /**
     * 로그 파일의 데이터를 분석하여 통계 내용을 파일에 저장
     * @param logList
     */
    public void writeAFile(List<LogVo> logList) {
        // 최다 호출 API KEY
        String topApiKey = getTopApiKey(logList);

        // 상위 3개의 API Service ID와 각각의 요청 수
        Map<String, Long> topServiceID = getTopServiceID(logList);

        // 웹브라우저별 사용 비율
        Map<String, Long> browserRate = getBrowserRate(logList);


        String filePath = getPATH() + "/"+ OUTPUTLOG;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            StringBuffer buffer = new StringBuffer();
            buffer.append("최다호출 API KEY\n");
            buffer.append(topApiKey +"\n");
            buffer.append("\n");

            buffer.append("상위 3개의 API Service ID와 각각의 요청 수\n");
            Set set = topServiceID.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                buffer.append(key +" : "+ topServiceID.get(key) +"\n");
            }
            buffer.append("\n");

            buffer.append("웹브라우저별 사용 비율\n");
            set = browserRate.keySet();
            iterator = set.iterator();
            logList = logList.stream()
                    .filter(x -> x.getStatus().equals("200"))
                    .collect(Collectors.toList());
            int totalLine = logList.size();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                int val = Math.toIntExact(browserRate.get(key));
                Double rate = ((double) 100 / totalLine) * val;
                buffer.append(key +" : "+ Math.round(rate) +"%\n");

            }
            buffer.append("\n");
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("파일 작성이 완료 되었습니다.");
        }
    }

    /**
     * URL에 있는 ApiKey를 추출
     * @param val
     * @return
     */
    public String getApiKey(String val) {
        String result = "";

        if (val != null && !val.equals("")) {
            URL url = null;
            try {
                url = new URL(val);
                if (url.getQuery() != null) {
                    String query = url.getQuery();
                    String apikey = query.split("\\&")[0];
                    result = apikey.replaceAll("apikey=", "");
                }
            } catch (MalformedURLException e) {
                //Exception 처리
                System.out.println(val);
//                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * URL에 있는 Service ID를 추출
     * @param val
     * @return
     */
    public String getServiceID(String val) {
        String result = "";

        if (val != null && !val.equals("")) {
            URL url = null;
            try {
                url = new URL(val);
                if (url.getPath() != null) {
                    String path = url.getPath();
                    result = path.replaceAll("/search/", "");
                }
            } catch (MalformedURLException e) {
                //Exception 처리
                System.out.println("URL 형식이 아닙니다."+ e.getMessage());
//                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 로그 리스트에서 정상(200) 데이터를 추출하여 apikey로 group by 함.
     * @param logList
     * @return
     */
    public String getTopApiKey(List<LogVo> logList) {
        String topApiKey = "";

        Map<String, Long> apiKeyMap = logList.stream()
                .filter(x -> x.getStatus().equals("200"))
                .collect(Collectors.groupingBy(x -> {
                    return getApiKey(x.getUrl());
                }, Collectors.counting()));

        Set<String> _sorted = apiKeyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .limit(1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Iterator ite = _sorted.iterator();

        while (ite.hasNext()) {
            topApiKey = (String) ite.next();
        }

        return topApiKey;
    }

    /**
     * 로그 리스트에서 정상(200) 데이터를 추출하여 Service ID로 group by 함.
     * @param logList
     * @return
     */
    public Map<String, Long> getTopServiceID(List<LogVo> logList) {
        Map<String, Long> serviceMap = logList.stream()
                .filter(x -> x.getStatus().equals("200"))
                .collect(Collectors.groupingBy(x -> {
                    return getServiceID(x.getUrl());
                }, Collectors.counting()));

        Map<String, Long> _sorted = serviceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return _sorted;
    }

    /**
     * 로그 리스트에서 정상(200) 데이터를 추출하여 Browser로 group by 함.
     * @param logList
     * @return
     */
    public Map<String, Long> getBrowserRate(List<LogVo> logList) {
        Map<String, Long> browserMap = logList.stream()
                .filter(x -> x.getStatus().equals("200"))
                .collect(Collectors.groupingBy(LogVo::getBrowser, Collectors.counting()));

        Map<String, Long> _sorted = browserMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return _sorted;
    }
}
