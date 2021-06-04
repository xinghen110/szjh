package com.magustek.szjh.utils.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * REST api 接口（controller）专用包装类
 *
 * */
public class BaseResponse {

    public static final String SUCCESS = "9f9af029585ba014e07cd3910ca976cf56160616";      //成功-200
    public static final String ONEMORECOMPANY = "09d66f6e5482d9b0ba91815c350fd9af3770819b";    //有多个公司-260
    public static final String ERROR = "15aa0c7e8fbd2923db7041d012e8838d66b9572d";        //出错-600
    public static final String REDIRECT = "3bb18d9ab531def40a51e637a236689460f8d373";     //跳转-601
    public static final String EMPTY = "73fb9760f330bcf6d3b61d28a67ccc8ba37a7f8f";        //无数据-602
    public static final String NOTLOGIN = "8d255e1e608e20d07f0fcfbcb95bc14abffba589";    //未登录-603
    public static final String FORBIDDEN = "f8d0f85975e49b959799cc52847110cc940b9db1";    //无权限-604


    private Object data;
    private String stateCode = SUCCESS;
    private String msg = "";

    public String toJson(){
        return JSONObject.toJSONString(this);
    }

    //禁止检测循环引用
    public String toJSONUncheck(){
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

    public Object getData() {
        return data;
    }

    public BaseResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public String getStateCode() {
        return stateCode;
    }

    public BaseResponse setStateCode(String stateCode) {
        this.stateCode = stateCode;
        /*
         * 由于springmvc的controller 使用单例模式，response会一直存在内存中，
         * 因此在设置baseresponse时需要先设置statecode，并同时清空data、msg变量。
         * */
        this.data = null;
        this.msg = "";
        return this;
    }

    public BaseResponse changeStateCode(String stateCode){
        this.stateCode = stateCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
