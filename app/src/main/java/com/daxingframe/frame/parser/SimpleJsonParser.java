package com.daxingframe.frame.parser;

import com.alibaba.fastjson.JSONObject;
import com.daxingframe.frame.model.InfoResult;

/**
 * [description about this class]
 * 通用的解析器(一般用于判断是否请求接口成功)
 * @author jack
 * @version [JackFrame, 2015/11/13 13:39]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class SimpleJsonParser extends JsonParser{

    private Object resultObj;//需要回传的值或者对象

    public SimpleJsonParser(){

    }

    public SimpleJsonParser(Object resultObj){

        this.resultObj = resultObj;
    }

    @Override
    public InfoResult doParse(byte[] responseResult) throws Exception {
        return null;
    }

    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject jsonObject) {
        if(infoResult.isSuccess()){
            infoResult.setExtraObj(resultObj);
        }
    }
}
