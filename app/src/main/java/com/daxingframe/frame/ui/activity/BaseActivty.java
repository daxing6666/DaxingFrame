package com.daxingframe.frame.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.daxingframe.R;
import com.daxingframe.frame.interfaces.ILogic;
import com.daxingframe.frame.logic.BaseLogic;
import com.daxingframe.frame.model.InfoResult;
import com.daxingframe.frame.ui.ioc.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * [description about this class]
 *
 * @author jack
 * @version [DaxingFrame, 2016/03/03 14:46]
 */
public abstract class BaseActivty extends AppCompatActivity {

    public Toast toast = null;
    private List<BaseLogic> logics = new ArrayList<BaseLogic>(); // 存储BaseLogic
    private boolean isDestroyed; // Activity是否已销毁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        afterSetContentView();
    }

//    @Override
//    public void setContentView(int layoutResID)
//    {
//        super.setContentView(layoutResID);
//        afterSetContentView();
//    }

//    @Override
//    public void setContentView(View view)
//    {
//        super.setContentView(view);
//        afterSetContentView();
//    }
//
//    @Override
//    public void setContentView(View view, ViewGroup.LayoutParams params)
//    {
//        super.setContentView(view, params);
//        afterSetContentView();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        for(BaseLogic logic : logics) {
            unregister(logic);
        }
    }

    /**
     * 不希望使用默认的注解来初始化View
     * setContentView之后调用, 进行view的初始化等操作
     */
    protected void afterSetContentView()
    {
        loadData();
    }

    /**
     * 加载数据
     */
    protected void loadData()
    {

    }

    /**
     * 注册BaseLogic, Activity销毁时是自动取消解绑
     * @param logic
     * @param <T>
     * @return
     */
    protected <T extends BaseLogic> T registerLogic(BaseLogic logic)
    {
        logics.add(logic);
        return (T)logic;
    }

    /**
     * EventBus订阅者事件通知的函数, UI线程
     *
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        if (!isDestroyed && !isFinishing())
        {
            if (msg.obj instanceof InfoResult) {
            }
            onResponse(msg);
        }
    }

    /**
     * 解绑当前订阅者
     * @param iLogics
     */
    protected void unregister(ILogic... iLogics)
    {
        for(ILogic iLogic : iLogics) {
            if (iLogic != null)
            {
                iLogic.cancelAll();
                iLogic.unregister(this);
            }
        }
    }

    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 跳转到某个Activity
     * @param activity
     * @param targetActivity
     */
    public void switchTo(Activity activity,Class<? extends Activity> targetActivity){
        switchTo(activity, new Intent(activity, targetActivity));
    }

    public void switchTo(Activity activity,Intent intent){
        activity.startActivity(intent);
    }

    /**
     * 跳转到某个Activity携带参数
     * @param activity
     * @param targetActivity
     * @param params
     */
    public void switchTo(Activity activity,Class<? extends Activity> targetActivity,Map<String,Object> params){
        if( null != params ){
            Intent intent = new Intent(activity,targetActivity);
            for(Map.Entry<String, Object> entry : params.entrySet()){
                setValueToIntent(intent, entry.getKey(), entry.getValue());
            }
            switchTo(activity, intent);
        }
    }

    public void setValueToIntent(Intent intent, String key, Object val) {
        if (val instanceof Boolean)
            intent.putExtra(key, (Boolean) val);
        else if (val instanceof Boolean[])
            intent.putExtra(key, (Boolean[]) val);
        else if (val instanceof String)
            intent.putExtra(key, (String) val);
        else if (val instanceof String[])
            intent.putExtra(key, (String[]) val);
        else if (val instanceof Integer)
            intent.putExtra(key, (Integer) val);
        else if (val instanceof Integer[])
            intent.putExtra(key, (Integer[]) val);
        else if (val instanceof Long)
            intent.putExtra(key, (Long) val);
        else if (val instanceof Long[])
            intent.putExtra(key, (Long[]) val);
        else if (val instanceof Double)
            intent.putExtra(key, (Double) val);
        else if (val instanceof Double[])
            intent.putExtra(key, (Double[]) val);
        else if (val instanceof Float)
            intent.putExtra(key, (Float) val);
        else if (val instanceof Float[])
            intent.putExtra(key, (Float[]) val);
    }


    /**
     * 设置Activity全屏显示
     * @param activity
     * @param isFull
     */
    public void setFullScreen(Activity activity,boolean isFull){
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 隐藏系统标题栏
     * @param activity
     */
    public void hideTitleBar(Activity activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置Activity的显示方向为横向
     * @param activity
     */
    public void setScreenHorizontal(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置Activity的显示方向为垂直方向
     * @param activity
     */
    public void setScreenVertical(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     *  隐藏软件输入法
     * @param activity
     */
    public void hideSoftInput(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * (就一般的应用而言只显示错误信息就可以了,所以此方法接口调用成功不显示提示,接口调用失败显示失败信息)
     * @param msg
     * @return
     */
    protected boolean chenckResponse(Message msg){
        return chenckResponse(msg,null,null,true);
    }
    /**
     * @param msg
     * @param successDesc  接口调用成功之后提示语
     * @param errorDesc    接口调用失败之后提示语
     * @param showErrorMsg 是否显示错误信息
     * @return
     */
    protected boolean chenckResponse(Message msg, String successDesc, String errorDesc, boolean showErrorMsg){

        if(msg.obj instanceof InfoResult){

            InfoResult result = (InfoResult)msg.obj;
            if(result.isSuccess()){
                {
                    if(!TextUtils.isEmpty(successDesc)){
                        showToast(successDesc);
                    }
                    return true;
                }
            }else{

                if(showErrorMsg){
                    if(!TextUtils.isEmpty(errorDesc)){
                        showToast(errorDesc);
                    }else if(!TextUtils.isEmpty(result.getDesc())){
                        showToast(result.getDesc());
                    }else{
                        showToast(getString(R.string.requesting_failure));
                    }
                }
                return false;
            }
        }else{
            if(showErrorMsg){
                if(!TextUtils.isEmpty(errorDesc)){
                    showToast(errorDesc);
                }else{
                    showToast(getString(R.string.requesting_failure));
                }
            }
            return false;
        }
    }
    public abstract void onResponse(Message msg);

}
