package com.daxingframe.frame.model;

/**
 * [description about this class]
 * 网络请求返回的数据
 * @author jack
 * @version [DaxingFrame, 2016/03/02 11:57]
 */
public class InfoResult {

    private boolean success;
    private String errorCode;
    private String desc;
    private Object extraObj;
    private Object otherObj;
    private long count;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getExtraObj() {
        return extraObj;
    }

    public void setExtraObj(Object extraObj) {
        this.extraObj = extraObj;
    }

    public Object getOtherObj() {
        return otherObj;
    }

    public void setOtherObj(Object otherObj) {
        this.otherObj = otherObj;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
