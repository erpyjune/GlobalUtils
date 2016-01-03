package com.erpyjune;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by oj.bae on 2016. 1. 2..
 */
public class StdProperties {
    String saveFilePathCrawlSeedPrefix=null;
    String saveFilePathBodyPrefix=null;
    String proxyServerListDataPath=null;
    String userAgent=null;

    public String getSaveFilePathCrawlSeedPrefix() {
        return this.saveFilePathCrawlSeedPrefix;
    }

    public void setSaveFilePathCrawlSeedPrefix(String saveFilePathCrawlSeedPrefix) {
        this.saveFilePathCrawlSeedPrefix = saveFilePathCrawlSeedPrefix;
    }

    public String getSaveFilePathBodyPrefix() {
        return this.saveFilePathBodyPrefix;
    }

    public void setSaveFilePathBodyPrefix(String saveFilePathBodyPrefix) {
        this.saveFilePathBodyPrefix = saveFilePathBodyPrefix;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getProxyServerListDataPath() {
        return proxyServerListDataPath;
    }

    public void setProxyServerListDataPath(String proxyServerListDataPath) {
        this.proxyServerListDataPath = proxyServerListDataPath;
    }

    public StdProperties() throws Exception {
        if (saveFilePathCrawlSeedPrefix!=null) return;
        if (saveFilePathBodyPrefix != null) return;
        if (userAgent != null) return;

        InputStream is = getClass().getClassLoader().getResourceAsStream("copywang.properties");
        Properties props = new Properties();
        props.load(is);
        saveFilePathBodyPrefix = props.getProperty("copywang.body.savepath.prefix");
        saveFilePathCrawlSeedPrefix = props.getProperty("copywang.seed.crawl.save.prefix");
        proxyServerListDataPath = props.getProperty("proxy.server.list.filepath");
        userAgent = props.getProperty("crawl.user.agent");
    }

    public static void main(String[] args) throws Exception {
        StdProperties stdProperties = new StdProperties();
        System.out.println("path --> "+stdProperties.getProxyServerListDataPath());
    }
}
