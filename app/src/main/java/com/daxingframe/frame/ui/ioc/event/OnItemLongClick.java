package com.daxingframe.frame.ui.ioc.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [description about this class]
 * item长按事件注解
 * @author jack
 * @version [DaxingFrame, 2016/03/14 11:55]
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnItemLongClick {

    int[] value();
}
