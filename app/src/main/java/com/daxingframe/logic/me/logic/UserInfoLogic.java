package com.daxingframe.logic.me.logic;

import com.daxingframe.R;
import com.daxingframe.frame.logic.BaseLogic;
import com.daxingframe.frame.network.volley.custom.InfoResultRequest;
import com.daxingframe.logic.me.parser.UserInfoParser;
import com.daxingframe.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * [description about this class]
 * 用户逻辑处理
 * @author jack
 * @version [DaxingFrame, 2016/03/03 16:08]
 */
public class UserInfoLogic extends BaseLogic {

    public UserInfoLogic(Object subscriber) {
        super(subscriber);
    }

    /**
     * 用户登录
     * @param account  用户名
     * @param password 密码
     */
    public void userlogin(String account, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", account);
        map.put("password", password);
        InfoResultRequest request = new InfoResultRequest(R.id.userlogin, Constants.USER_LOGIN, map,
                new UserInfoParser(), this);
        sendRequest(request, R.id.userlogin);
    }

}
