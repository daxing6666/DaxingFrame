package com.daxingframe;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daxingframe.R;
import com.daxingframe.frame.ui.activity.BaseActivty;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * [description about this class]
 * 登录界面
 *
 * @author jack
 * @version [DaxingFrame, 2016/03/03 16:30]
 */
public class RegisterActivity extends BaseActivty {

    @Bind(R.id.btn_login)
    public Button btnLogin;
    @Bind(R.id.tv)
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void afterSetContentView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResponse(Message msg) {
        switch (msg.what) {
        }
    }

    @OnClick(R.id.btn_login)
    public void onClick(View view) {

    }
}
