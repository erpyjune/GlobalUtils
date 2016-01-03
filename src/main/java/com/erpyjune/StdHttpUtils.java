package com.erpyjune;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by oj.bae on 2016. 1. 2..
 */
public class StdHttpUtils {
    private static Logger logger = Logger.getLogger(StdHttpUtils.class.getName());

    private String crawlUrl="";
    private String crawlData="";
    private String crawlEncoding="euc-kr"; // UTF-8, EUC-KR

    // POST data form param.
    Map<String, String> postFormDataParam=null;
    Map<String, String> requestHeader=null;
    Map<String, String> responseHeaders=null;

    private int reponseCode;
    private int socketTimeout=10000;
    private int connectionTimeout=10000;
    private String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
    private String REFERER = "http://search.google.com";
    private String CONNECTION = "keep-alive";
    private String ACCEPT = "text/html, */*; q=0.01";
    private String ACCEPT_LANG = "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4";
    private String Content_Type = "application/x-www-form-urlencoded; charset=UTF-8";

    // set method.
    public void setCrawlUrl(String url) {
        this.crawlUrl = url;
    }

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public void setCrawlEncode(String type) {
        this.crawlEncoding = type;
    }
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    public String getCrawlData() {
        return this.crawlData;
    }
    public void setCrawlData(String crawlData) {
        this.crawlData = crawlData;
    }
    public int getReponseCode() {
        return reponseCode;
    }

    public void addPostRequestParam(String name, String value) {
        postFormDataParam.put(name, value);
    }
    public void clearPostRequestParam() {
        postFormDataParam.clear();
    }
    public Map<String, String> getPostRequestParam() {
        return postFormDataParam;
    }
    public void setPostFormDataParam(Map<String, String> map) { this.postFormDataParam = map; }
    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }
    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }


    ///////////////////////////////////////////////////////////////////////////
    // crawling method...
    public String HttpCrawlGetMethod1() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.crawlUrl);
        HttpEntity entity = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line=null;

        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request header %s::%s", entry.getKey().trim(), entry.getValue().trim()));
            httpGet.setHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        CloseableHttpResponse response = httpClient.execute(httpGet);
        reponseCode = response.getStatusLine().getStatusCode();

        logger.info(" Response Code : " + response.getStatusLine());

        try {
            entity = response.getEntity();
            if (entity != null) {
                br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), this.crawlEncoding));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } finally {
            if(br!=null) br.close();
            response.close();
        }

        EntityUtils.consume(entity);

        if (sb.length() <= 0) {
            this.crawlData = "";
        } else {
            this.crawlData = sb.toString();
        }

        return this.crawlData;
    }


    ///////////////////////////////////////////////////////////////////////////
    public String HttpCrawlGetMethod2() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(this.crawlUrl);

        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request header %s::%s", entry.getKey().trim(), entry.getValue().trim()));
            request.setHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        reponseCode = response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), this.crawlEncoding));

        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        if (result.length() <= 0) {
            this.crawlData = "";
        } else {
            this.crawlData = result.toString();
        }

        rd.close();

        return this.crawlData;
    }


    ///////////////////////////////////////////////////////////////////////////
    public int HttpCrawlGetDataTimeout() throws Exception {
        Map<String, String> tmpRequestHeaders = new HashMap<String, String>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(socketTimeout)
//                .setRedirectsEnabled(true)
//                .setMaxRedirects(3)
//                .setProxy(new HttpHost("153.121.75.130", 8080, "http"))
                .build();


        HttpGet httpGet = new HttpGet(crawlUrl);

        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request header %s:%s", entry.getKey(), entry.getValue()));
            httpGet.addHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        httpGet.setConfig(requestConfig);

        // crawling execute.
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
        reponseCode = closeableHttpResponse.getStatusLine().getStatusCode();

        // set response header
        Header[] tmpHeaders =  closeableHttpResponse.getAllHeaders();
        for (Header header : tmpHeaders) {
            tmpRequestHeaders.put(header.getName(), header.getValue());
//            logger.info(String.format(" HEADER %s:%s", header.getName(), header.getValue()));
        }
        responseHeaders = tmpRequestHeaders;

        try {
            //HttpEntity httpEntity = closeableHttpResponse.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(closeableHttpResponse.getEntity().getContent(), crawlEncoding));

            String line;
            StringBuffer result = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }

            if (result.length() <= 0) {
                this.crawlData = "";
            } else {
                this.crawlData = result.toString();
            }
        } finally {
            closeableHttpResponse.close();
        }

        return closeableHttpResponse.getStatusLine().getStatusCode();
    }


    ////////////////////////////////////////////////////////////////////////////////
    public int HttpCrawlGetDataProxyTimeout(String ip, int port) throws Exception {
        Map<String, String> tmpRequestHeaders = new HashMap<String, String>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(socketTimeout)
//                .setRedirectsEnabled(true)
//                .setMaxRedirects(3)
                .setProxy(new HttpHost(ip, port, "http"))
                .build();


        HttpGet httpGet = new HttpGet(crawlUrl);

        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request header %s:%s", entry.getKey(), entry.getValue()));
            httpGet.addHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        httpGet.setConfig(requestConfig);

        // crawling execute.
        CloseableHttpResponse closeableHttpResponse;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
            reponseCode = closeableHttpResponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.info(Arrays.toString(e.getStackTrace()));
            logger.info(String.format(" http connection fail : %s, %d", ip, port));
            return 707;
        }

        // set response header
        Header[] tmpHeaders =  closeableHttpResponse.getAllHeaders();
        for (Header header : tmpHeaders) {
            tmpRequestHeaders.put(header.getName(), header.getValue());
//            logger.info(String.format(" HEADER %s:%s", header.getName(), header.getValue()));
        }
        responseHeaders = tmpRequestHeaders;

        try {
            //HttpEntity httpEntity = closeableHttpResponse.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(closeableHttpResponse.getEntity().getContent(), crawlEncoding));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }

            if (result.length() <= 0) {
                this.crawlData = "";
            } else {
                this.crawlData = result.toString();
            }
        } catch (SocketTimeoutException e) {
            logger.info(" http read socket timeout exception ");
        } finally {
            closeableHttpResponse.close();
        }

        return closeableHttpResponse.getStatusLine().getStatusCode();
    }


    ///////////////////////////////////////////////////////////////////////////
    public void HttpCrawlGetRedirectUrl() throws Exception {
        String value;
        Map<String,List<String>> headers;
        Map<String, String> responseHeader = new HashMap<String, String>();

        URLConnection con = new URL(crawlUrl).openConnection();

        // set request headers
        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
            logger.info(String.format(" Set request headers - %s:%s", entry.getKey(), entry.getValue()));
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }

        // crawling
        con.connect();
        InputStream is = con.getInputStream();
        crawlData = con.getURL().toString(); // redirect url

        // set response headers
        headers = con.getHeaderFields();
        for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            Iterator iterator = values.iterator();
            while(iterator.hasNext()) {
                value = (String) iterator.next();
                responseHeader.put(key, value);
//                logger.info(String.format("HEADER %s:%s", key, value));
            }
        }

        responseHeaders = responseHeader;

        is.close();
    }


    ///////////////////////////////////////////////////////////////////////////
    public int HttpCrawlPostMethod() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(crawlUrl);

        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request header %s::%s", entry.getKey().trim(), entry.getValue().trim()));
            httpPost.setHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("mode", "categorymain"));
        urlParameters.add(new BasicNameValuePair("categoryid", "9420101"));
        urlParameters.add(new BasicNameValuePair("startnum", "41"));
        urlParameters.add(new BasicNameValuePair("endnum", "80"));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        urlParameters.size();

        HttpResponse response = client.execute(httpPost);

        reponseCode =response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), this.crawlEncoding));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        if (result.length() <= 0) {
            this.crawlData = "";
        } else {
            this.crawlData = result.toString();
        }

        rd.close();

        return reponseCode;
    }


    ///////////////////////////////////////////////////////////////////////////
    /// Http request post
    ///////////////////////////////////////////////////////////////////////////
    public void HttpPostGetData() throws Exception {
        String jsonQueryString = "{\n" +
                "\t\"query\" : {\n" +
                "    \t\"multi_match\": {\n" +
                "        \t\"query\":                \"nike\",\n" +
                "        \t\"type\":                 \"best_fields\", \n" +
                "        \t\"fields\":               [ \"product_name^2\", \"brand_name^1\", \"keyword^1\" ]\n" +
                "    \t}\n" +
                "    }\n" +
                "}";
        HttpPost httpPost = new HttpPost(crawlUrl);
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("Referer", REFERER);
        httpPost.setConfig(RequestConfig.custom().
                setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .build());

//        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//        urlParameters.add(new BasicNameValuePair("mode", "categorymain"));
//        urlParameters.add(new BasicNameValuePair("categoryid", "94201"));
//        urlParameters.add(new BasicNameValuePair("startnum", "41"));
//        urlParameters.add(new BasicNameValuePair("endnum", "200"));
//        urlParameters.size();


        logger.info("jsonquery : " + jsonQueryString);
        StringEntity stringEntity = new StringEntity(jsonQueryString);
        httpPost.setEntity(stringEntity);
//        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpPost);

        logger.info("repose code : " + response.getStatusLine().getStatusCode());
        reponseCode =response.getStatusLine().getStatusCode();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), this.crawlEncoding));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        if (result.length() <= 0) {
            this.crawlData = "";
        } else {
            this.crawlData = result.toString();
        }

        bufferedReader.close();
    }


    ///////////////////////////////////////////////////////////////////////////
    /// Http request post
    ///////////////////////////////////////////////////////////////////////////
    public int HttpXPUT() throws IOException {

        //String data = "{\"title\" : \"good morning\", \"name\" : \"erpy\", \"date\" : \"20141015\", \"id\" : 123}";
        int responseReturnCode;
        URL url = new URL(crawlUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        //httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(5000);
        httpConn.setReadTimeout(5000);
        httpConn.setRequestMethod("POST"); // PUT, DELETE, POST, GET

        OutputStreamWriter osw = new OutputStreamWriter(httpConn.getOutputStream());
        osw.write(crawlData);
        osw.flush();
        osw.close();

        //System.out.println("HTTP Response Code : " + httpConn.getResponseCode());
        responseReturnCode = httpConn.getResponseCode();
        httpConn.disconnect();

        return responseReturnCode;
    }


    ///////////////////////////////////////////////////////////////////////////
    /// Http request POST.
    ///////////////////////////////////////////////////////////////////////////
    public int getDataHttpPosTimeout() throws Exception {
        HttpPost post = new HttpPost(crawlUrl);
        post.setConfig(RequestConfig.custom().
                setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .build());

        logger.info(" Crawling target : " + crawlUrl);

        // add request headers.
        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
//            logger.info(String.format(" Set request Header %s:%s", entry.getKey().trim(), entry.getValue().trim()));
            post.setHeader(entry.getKey().trim(), entry.getValue().trim());
        }

        // set param
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry : postFormDataParam.entrySet()) {
//            logger.info(String.format(" Set request Param %s:%s", entry.getKey().trim(), entry.getValue().trim()));
            urlParameters.add(new BasicNameValuePair(entry.getKey().trim(), entry.getValue().trim()));
        }
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(post);

        reponseCode =response.getStatusLine().getStatusCode();

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), crawlEncoding));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        if (result.length() <= 0) {
            this.crawlData = "";
        } else {
            this.crawlData = result.toString();
        }

        return reponseCode;
    }


    public int simpleHttpUrlGetProxy(String ip, int port) throws Exception {
        URL obj = new URL(this.crawlUrl);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection(proxy);

        // optional default is GET
        con.setRequestMethod("GET");
        con.usingProxy();
        con.setConnectTimeout(this.connectionTimeout);
        con.setReadTimeout(this.socketTimeout);

        // set header
        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
            con.setRequestProperty(entry.getKey().trim(), entry.getValue().trim());
        }

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), this.crawlEncoding));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        this.crawlData = response.toString();

        return responseCode;
    }


    public int simpleHttpUrlGet() throws Exception {
        URL obj = new URL(this.crawlUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.usingProxy();
        con.setConnectTimeout(this.connectionTimeout);
        con.setReadTimeout(this.socketTimeout);

        // set header
        for(Map.Entry<String, String> entry : requestHeader.entrySet()) {
            con.setRequestProperty(entry.getKey().trim(), entry.getValue().trim());
        }

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), this.crawlEncoding));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        this.crawlData = response.toString();

        return responseCode;
    }
}
