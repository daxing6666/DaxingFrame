package com.daxingframe.frame.interfaces;

import com.daxingframe.frame.model.InfoResult;

/**
 * [description about this class]
 * 网络请求返回数据时,通过此接口回调解析器,返回解析结果InfoResult
 * @author jack
 * @version [DaxingFrame, 2016/03/02 16:16]
 */
public interface IresponseParserListener {

    InfoResult doParse(final String response) throws Exception;
    InfoResult doParse(final byte[] responseResult) throws Exception;
}
