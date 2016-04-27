package com.daxingframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.daxingframe.frame.model.InfoResult;
import com.daxingframe.frame.ui.activity.BaseActivty;
import com.daxingframe.frame.ui.ioc.ViewInject;
import com.daxingframe.frame.ui.ioc.event.ContentView;
import com.daxingframe.frame.ui.ioc.event.OnClick;
import com.daxingframe.frame.ui.view.loading.progresslayout.ProgressLayout;
import com.daxingframe.logic.me.logic.UserInfoLogic;
import com.daxingframe.util.SPDBHelper;

/**
 * [description about this class]
 * 登录界面
 * @author jack
 * @version [DaxingFrame, 2016/03/03 16:30]
 */
@ContentView(value = R.layout.activity_login)
public class UserLoginActivity extends BaseActivty {
    private UserInfoLogic userLogic;
    @ViewInject(R.id.btn_login)
    public Button btnLogin;
    private SPDBHelper spdbHelper;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
    }

    @Override
    protected void afterSetContentView() {
        spdbHelper = new SPDBHelper();
        userLogic = new UserInfoLogic(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister(userLogic);
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick(R.id.btn_login)
    public void OnClick(View view){
        switch(view.getId()){

            case R.id.btn_login:
                break;
            default:break;
        }
    }

    @Override
    public void onResponse(Message msg) {
        switch (msg.what){
            /**
             * 登录返回
             */
            case R.id.userlogin:
                InfoResult infoResult = (InfoResult)msg.obj;
                showToast(infoResult.getDesc());
                break;
            default:break;
        }
    }
}
