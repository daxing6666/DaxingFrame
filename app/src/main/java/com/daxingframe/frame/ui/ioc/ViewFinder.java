package com.daxingframe.frame.ui.ioc;

import android.app.Activity;
import android.view.View;

/**
 * [description about this class]
 * View查找器
 * @author jack
 * @version [DaxingFrame, 2016/03/14 17:28]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ViewFinder {

    private View view;
    private Activity activity;

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    public ViewFinder(View view) {
        this.view = view;
    }

    /**
     * 根据id查找 view
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return activity != null? activity.findViewById(id) : view.findViewById(id);
    }
}
