package com.magustek.szjh.utils.http;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.config.HttpConnectConfig;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.OdataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;


@Slf4j
@Component
public class HttpUtils {
    private final HttpConnectConfig config;
    private final RestTemplate restTemplate;

    private final String odataTokenHeader = "X-CSRF-TOKEN";
    private String odataString = "/sap/opu/odata/sap/";
    private String odataClient = "&sap-client=";

    @Autowired
    public HttpUtils(HttpConnectConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }
    //单条记录
    public Map<String, Object> getMapByUrl(String url, Object params, HttpMethod method) throws Exception{
        String data = getStringByUrl(url, params, method);
        return getMap(data);
    }
    //多条记录
    public List<Map<String,Object>> getListByUrl(String url, Object params, HttpMethod method) throws Exception{
        String data = getStringByUrl(url, params, method);
        return getList(data);
    }
    //JSON
    public String getResultByUrl(String url, Object params, HttpMethod method){
        try {
            return getStringByUrl(url, params, method);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * http请求返回一个String
     * @param url 请求路径
     * @param params 请求参数
     * @param method 请求方法
     * @return 返回一个String 请求失败返回一个null
     */
    private String getStringByUrl(String url, Object params, HttpMethod method) throws Exception{
        //设置http头
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> mt = new ArrayList<>(1);
        mt.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mt);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        StringBuilder urlString = new StringBuilder();

        if("odata".equals(config.getType())) {
            //拼接url
            urlString.append(config.getOdataIp()).append(":").append(config.getOdataPort())
                    .append(odataString)
                    .append(url)
                    .append(odataClient)
                    .append(config.getOdataClient());

            //设置sap token，如果获取失败，则最多重试5次。
            String token = "";
            int i = 0;
            while (Strings.isNullOrEmpty(token)){
                token = this.getOdataToken();
                i++;
                if(i>5){
                    log.error("获取sap token失败！url：{}",urlString);
                    break;
                }
            }
            headers.add(odataTokenHeader, token);
        }
        if("esb".equals(config.getType())) {
            //拼接url
            String esbString = "";
            if(HttpMethod.GET.equals(method)) {
                esbString = "/ESBWeb/servlets/15300.CM.OdataGet@1.0@zn.cm.xt?";
            }
            if(HttpMethod.POST.equals(method)) {
                esbString = "/ESBWeb/servlets/15301.CM.OdataPost@1.0@zn.cm.xt?";
            }
            if(HttpMethod.PUT.equals(method)) {
                esbString = "/ESBWeb/servlets/15303.CM.OdataPut@1.0@zn.cm.xt?";
            }
            if(HttpMethod.DELETE.equals(method)) {
                esbString = "/ESBWeb/servlets/15302.CM.OdataDelete@1.0@zn.cm.xt?";
            }
            urlString.append(config.getEsbIp())
                    .append(esbString)
                    .append(url)
                    .append(odataClient)
                    .append(config.getOdataClient());
        }
        //设置suffix
        String format = "$format=json";
        if(HttpMethod.GET.equals(method)) {
            if (urlString.indexOf("?") != -1) {
                if (urlString.indexOf(format) == -1) {
                    urlString.append("&").append(format);
                }
            } else {
                if (urlString.indexOf(format) == -1) {
                    urlString.append("?").append(format);
                }
            }
        }

        log.info("---" + method.name() + "  " + urlString.toString());
        if(params!=null){
            log.info("--- params :"+JSON.toJSONString(params));
        }
        long start = System.currentTimeMillis();
        //执行http请求
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response;
        response = restTemplate.exchange(
                urlString.toString(),
                method,
                requestEntity,
                String.class);
        log.info("--- "+ ContextUtils.getUserName()+" odata调用耗时：【"+(System.currentTimeMillis()-start)/1000.0+"】"+method.name()+"  "+urlString.toString());
        return checkResponse(response);
    }

    /**
     * 获取odata的token
     * */
    private String getOdataToken(){
        //拼接获取token的url
        StringBuilder tokenString = new StringBuilder();
        tokenString.append(config.getOdataIp()).append(":").append(config.getOdataPort())
                .append(odataString)
                .append(OdataUtils.Token)
                .append("?")
                .append(odataClient)
                .append(config.getOdataClient());
        //设置header
        HttpHeaders headers = new HttpHeaders();
        headers.add(odataTokenHeader, "Fetch");
        //设置账号密码
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(config.getOdataUser(), config.getOdataPass()));
        ResponseEntity<String> responseEntity;
        try{
            //执行odata调用
            responseEntity = restTemplate.exchange(tokenString.toString(), HttpMethod.GET, new HttpEntity(headers), String.class);
        }catch (HttpServerErrorException e){
            log.error("获取token失败：{}", e.getMessage());
            return "";
        }
        //从http header中获取token
        List<String> headerList = responseEntity.getHeaders().get(odataTokenHeader);
        if(ClassUtils.isEmpty(headerList)){
            log.error("获取token失败：{}-{}",responseEntity.getStatusCodeValue(),responseEntity.getBody());
        }
        return headerList.get(headerList.size() - 1);
    }

    /**
     * 校验调用结果
     * */
    private String checkResponse(ResponseEntity<String> response) throws Exception{

        HttpStatus statusCode = response.getStatusCode();
        log.info("响应状态：" + statusCode.value());
        if(!statusCode.is2xxSuccessful()){
            log.error("---------调用httpClient出错：" + response.getBody());
            throw new Exception("---------调用httpClient出错返回状态值" + statusCode.value());
        }
        return response.getBody();
    }
    //单条记录
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(String data) {
        if (!data.isEmpty()) {
            Map<String, Object> dataMap = (Map<String, Object>) JSON.parseObject(data).get("d");
            dataMap.remove("__metadata");
            return dataMap;
        } else {
            return null;
        }
    }
    //多条记录
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getList(String data){
        if (!data.isEmpty()) {
            Map<String, Object> dataMap = (Map<String, Object>) JSON.parseObject(data).get("d");
            List<Map<String, Object>> ListMap = (List<Map<String, Object>>) dataMap.get("results");
            for (Map<String, Object> m : ListMap) {
                m.remove("__metadata");
            }
            return ListMap;
        } else {
            return null;
        }
    }
}
