package com.daxingframe.frame.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daxingframe.frame.model.InfoResult;
import com.daxingframe.frame.interfaces.IresponseParserListener;

/**
 * [description about this class]
 * 把网络数据统一转成InfoResult
 * @author jack
 * @version [DaxingFrame, 2016/03/02 16:21]
 */
public abstract class JsonParser implements IresponseParserListener{

    @Override
    public InfoResult doParse(String response) throws Exception {

        JSONObject jsonObject = JSON.parseObject(response);
        InfoResult infoResult = new InfoResult();
        infoResult.setSuccess(jsonObject.getString("state").equals("0"));
        infoResult.setDesc(jsonObject.getString("errMsg"));
        //infoResult.setErrorCode(jsonObject.getString("code"));
        parseResponse(infoResult,jsonObject);
        return infoResult;
    }

    @Override
    public InfoResult doParse(byte[] responseResult) throws Exception {
        return null;
    }

    protected abstract void parseResponse(final InfoResult infoResult, final JSONObject jsonObject);
}
