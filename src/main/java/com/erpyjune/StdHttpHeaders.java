package com.erpyjune;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oj.bae on 2016. 1. 3..
 */
public class StdHttpHeaders {
    private static Logger logger = Logger.getLogger(StdHttpHeaders.class.getName());
    Map<String, String> header=null;

    public Map<String, String> getHeader() {
        return header;
    }

    public void putHeader(String name, String value) {
        if (header==null) {
            logger.error(" Header map is null !!");
        } else {
            header.put(name, value);
        }
    }

    public StdHttpHeaders () {
        header = new HashMap<String, String>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        header.put("Accept-Encoding", "gzip, deflate, sdch");
        header.put("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
        header.put("Cache-Control","no-cache");
        header.put("Pragma","no-cache");
        header.put("Upgrade-Insecure-Requests","1");
        header.put("Connection","keep-alive");
        // for PC
        header.put("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
    }
}
