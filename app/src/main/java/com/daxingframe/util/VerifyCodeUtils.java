package com.daxingframe.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import com.daxingframe.frame.ui.activity.BaseActivty;

/**
 * author jack
 */
public class VerifyCodeUtils
{
    private static CountDownTimer sCountDownTimer;


    public static void getVerifyCode(final BaseActivty activity, final Button button, long millisInFuture)
    {
        if (sCountDownTimer == null)
        {
            sCountDownTimer = new CountDownTimer(millisInFuture, 1000)
            {
                public void onTick(long millisUntilFinished)
                {
                    button.setClickable(false);//设置不能点击
                    String second = (int) (millisUntilFinished / 1000) + "";
                    button.setText(second + "秒后可重发");//设置倒计时时间
                    //设置按钮为灰色，这时是不能点击的
                    Spannable span = new SpannableString(button.getText().toString());//获取按钮的文字
                    span.setSpan(new ForegroundColorSpan(Color.RED), 0, second.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//讲倒计时时间显示为红色
                    button.setText(span);
                }

                @Override
                public void onFinish()
                {
                    button.setText("重新获取验证码");
                    button.setClickable(true);//重新获得点击
                }
            };
            sCountDownTimer.start();
        }
    }

    public static void cancle()
    {
        if (null != sCountDownTimer)
        {
            sCountDownTimer.cancel();
            sCountDownTimer = null;
        }
    }
}
