package com.daxingframe.logic.me.parser;

import com.alibaba.fastjson.JSONObject;
import com.daxingframe.frame.model.InfoResult;
import com.daxingframe.frame.parser.JsonParser;

/**
 * [description about this class]
 *
 * @author jack
 * @version [DaxingFrame, 2016/03/03 16:10]
 */
public class UserInfoParser extends JsonParser {
    @Override
    protected void parseResponse(InfoResult infoResult, JSONObject jsonObject) {
        if (infoResult.isSuccess()) {
        }
    }
}