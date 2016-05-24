package com.demo.utils.HttpManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by yaoyu on 16/3/8.
 */
public class HttpClientUtils {
    private static Logger LOGGER= LoggerFactory.getLogger(HttpClientUtils.class);
    private static HttpClientBuilder clientBuilder=null;

    public static PoolingHttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public static void setConnectionManager(PoolingHttpClientConnectionManager connectionManager) {
        HttpClientUtils.connectionManager = connectionManager;
    }

    public static HttpClientBuilder getClientBuilder() {
        return clientBuilder;
    }

    public static void setClientBuilder(HttpClientBuilder clientBuilder) {
        HttpClientUtils.clientBuilder = clientBuilder;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public static void setRequestConfig(RequestConfig requestConfig) {
        HttpClientUtils.requestConfig = requestConfig;
    }

    private static RequestConfig requestConfig=null;
    private static PoolingHttpClientConnectionManager connectionManager=null;

    public static int CONNECT_TIMEOUT=3000;
    public static int SOCKET_TIMEOUT=3000;
    public static int MAX_CONNECTTION=3000;
    public static int MAX_HOST_CONNECTION=10;
    public static String CONTENT_TYPE="content-type";
    public static String GZIP="gzip";
    public static String UTF8="utf-8";
    public static Charset UTF_8=Charset.forName(UTF8);

    static{
        requestConfig=RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).
                setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .build();

        connectionManager=new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTTION);
        connectionManager.setDefaultMaxPerRoute(MAX_HOST_CONNECTION);

        clientBuilder= HttpClients.custom();
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(){
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context){
                boolean r=!(exception instanceof SocketTimeoutException);
                LOGGER.error("RetryRequest :",exception.getMessage());
                return r;
            }
        });
    }

    public static CloseableHttpClient getHttpClient(){
        return clientBuilder.build();
    }

    public static InputStream executeToStream(final HttpRequestBase request,int timeout) throws Exception{
        InputStream in=null;
        try{
            CloseableHttpResponse response=executeToResp(request,timeout);
            HttpEntity entity=response.getEntity();
            int code=response.getStatusLine().getStatusCode();
            in=entity.getContent();
            if (code== HttpStatus.SC_OK){
                if (isGzip(entity)){
                    in=new GZIPInputStream(in);
                }
                return in;
            }else {
                LOGGER.error("Request{} code{}:",request.getURI(),code);
                throw new Exception("code"+code);
            }
        }
        catch (Exception e){
            String parm="";
            if (request instanceof HttpEntityEnclosingRequestBase){
                HttpEntityEnclosingRequestBase r=(HttpEntityEnclosingRequestBase) request;
                HttpEntity entity=r.getEntity();
                parm=IOUtils.toString(entity.getContent());
            }
           // LOGGER.error("httpFailed url={},parm={},msg={}",request.getURI(),parm,e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static CloseableHttpResponse executeToResp(HttpRequestBase request,int timeout) throws Exception{
        InputStream in=null;
            if (timeout>0){
                RequestConfig reqConf=RequestConfig.custom().setSocketTimeout(timeout).
                        setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).
                        build();
                request.setConfig(reqConf);
            }else {
                request.setConfig(requestConfig);
            }
            return getHttpClient().execute(request);
    }

    private static boolean isGzip(HttpEntity entity){
        Header encode=entity.getContentEncoding();
        return null!=encode&& StringUtils.isNotEmpty(encode.getValue())
                &&encode.getValue().contains(GZIP);
    }

    public static <T> T postEncodeParams(String uri, Map<String,Object> params,Map<String,String> headers,T resultType) throws Exception{
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        if (params!=null){
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
            }
        }
        return post(uri,new UrlEncodedFormEntity(list,UTF_8),headers,resultType);
    }

    public static <T> T post(String uri,HttpEntity body,Map<String,String> headers,T resultType) throws Exception{
        InputStream in=null;
        try {
           HttpPost post=new HttpPost(uri);
           if (body!=null){
               post.setEntity(body);
           }
           if (headers!=null){
               for (Map.Entry<String, String> entry : headers.entrySet()) {
                   post.addHeader(entry.getKey(),entry.getValue());
               }
           }
           if (resultType!=null&&resultType.getClass().equals(String.class)){
               in=executeToStream(post,CONNECT_TIMEOUT);
               return (T) IOUtils.toString(in,UTF_8);
           }
           if (resultType!=null&&resultType.getClass().equals(Byte.class)){
               in=executeToStream(post,CONNECT_TIMEOUT);
               return (T)IOUtils.toByteArray(in);
           }
           if (resultType!=null&&resultType.getClass().equals(InputStream.class)){
               return (T)executeToStream(post,CONNECT_TIMEOUT);
           }
           else {
               throw new IllegalArgumentException("result type cannot being parse:{}");
           }
       }catch (Exception e) {
            //LOGGER.error("post failed url{},params{},headers{}:",uri,body.toString(),headers.toString(),e.getMessage());
            throw e;
        }finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static <T> T get(String uri,Map<String,String> params,Map<String,String> headers,T resultType) throws Exception{
        InputStream in=null;
        try {
            StringBuilder sb=new StringBuilder(uri);
            if (params!=null&&!params.isEmpty()){
                if (sb.indexOf("?")<0){
                    sb.append("?");
                }else {
                    sb.append("&");
                }
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),UTF8)).append("&");
                }
                uri=sb.substring(0,sb.length()-1);
            }
            HttpGet get=new HttpGet(uri);
            if (headers!=null){
                for (Map.Entry<String,String> entry:headers.entrySet()){
                    get.addHeader(entry.getKey(),entry.getValue());
                }
            }
            if (resultType!=null&&resultType.getClass().equals(String.class)){
                in=executeToStream(get,CONNECT_TIMEOUT);
                return (T) IOUtils.toString(in,UTF_8);
            }
            if (resultType!=null&&resultType.getClass().equals(Byte.class)){
                in=executeToStream(get,CONNECT_TIMEOUT);
                return (T)IOUtils.toByteArray(in);
            }
            if (resultType!=null&&resultType.getClass().equals(InputStream.class)){
                return (T)executeToStream(get,CONNECT_TIMEOUT);
            }
            else {
                throw new IllegalArgumentException("result type cannot being parse:{}"+resultType);
            }
        }catch (Exception e){
            //.error("get failed url{},params{},headers{}:",uri,params.toString(),headers.toString(),e.getMessage());
            throw e;
        }finally {
            IOUtils.closeQuietly(in);
        }
    }
}
