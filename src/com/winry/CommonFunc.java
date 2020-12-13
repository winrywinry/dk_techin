package com.winry;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommonFunc {
    private final String INPUTLOG = "input.log";
    private final String OUTPUTLOG = "output.log";

    public List<LogVo> readAFile(String path) {
        String filePath = path + "/"+ INPUTLOG;

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
            e.printStackTrace();
        } finally {
            try {
                reader.close();
           } catch (IOException e) {
                // Exception 처리
                e.printStackTrace();
            }
        }

        return resultList;
    }

    private String getStatus(String val) {
        String status = "";
        if (val != null && !val.equals("") && !val.equals("null")) {
            if (val.indexOf("[") != -1) {
                String[] arrVal = val.split("\\[");
                status = arrVal[1].replace("]", "");
            }
        }

        return status;
    }

    public String getUrl(String val) {
        String url = "";
        if (val != null && !val.equals("") && !val.equals("null")) {
            if (val.indexOf("[") != -1) {
                String[] arrVal = val.split("\\[");
                url = arrVal[2].replace("]", "");
            }
        }

        return url;
    }

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
}
