package com.daxingframe.frame.ui.ioc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;

import com.daxingframe.frame.ui.ioc.event.ContentView;
import com.daxingframe.frame.ui.ioc.event.OnCheckedChanged;
import com.daxingframe.frame.ui.ioc.event.OnClick;
import com.daxingframe.frame.ui.ioc.event.OnItemClick;
import com.daxingframe.frame.ui.ioc.event.OnItemLongClick;
import com.daxingframe.frame.ui.ioc.event.OnLongClick;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 利用注解实现View初始化和事件绑定
 * @author jack
 * @version [Android-BaseLine, 2014-9-15]
 */
public class ViewUtils
{
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * 注解View与布局文件
     * @param activity
     */
    public static void inject(Activity activity)
    {
        injectContentView(activity);
        inject(activity, new ViewFinder(activity));
    }
    /**
     * 注解View、事件
     * @param classObj class对象
     * @param contentView 父View对象
     */
    public static void inject(Object classObj, View contentView)
    {
        inject(classObj, new ViewFinder(contentView));
    }

    private static void inject(Object classObj, ViewFinder finder)
    {
        try
        {
            injectViews(classObj, finder);
            injectListeners(classObj, finder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 注册该activity的布局文件
     * 通过传入的activity对象，获得它的Class类型,判断是否写了ContentView这个注解,
     * 如果写了读取它的value,然后得到setContentView这个方法，使用invoke进行调用
     * @param activity
     */
    public static void injectContentView(Activity activity){

        Class<? extends Activity> c = activity.getClass();
        // 查询类上是否存在ContentView注解
        ContentView contentView = c.getAnnotation(ContentView.class);
        if(contentView != null){

            int contentViewLayoutId = contentView.value();
            try{

                //获取的是类自身声明的所有方法，包含public、protected和private方法。
                //Method method = c.getDeclaredMethod(METHOD_SET_CONTENTVIEW,int.class);

                //获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法
                Method method = c.getMethod(METHOD_SET_CONTENTVIEW, int.class);
                method.setAccessible(true);
                /**
                 * Object invoke(Object obj,Object... args)
                 * 该方法中obj是执行该方法的主调,后面的args是执行该
                 * 方法时传入该方法的实参
                 * */
                method.invoke(activity,contentViewLayoutId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * View注解
     * @param classObj
     * @param viewFinder
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static void injectViews(Object classObj, ViewFinder viewFinder) throws IllegalAccessException, IllegalArgumentException
    {
        Class<?> clazz = classObj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            //如果一个注解指定注释类型是存在于此元素上此方法返回true，否则返回false
            if (field.isAnnotationPresent(ViewInject.class))
            {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                int id = viewInject.value();
                if (id != -1)
                {
                    field.setAccessible(true);
                    View v = viewFinder.findViewById(id);
                    if (v != null)
                    {
                        try
                        {
                            /**
                             * setXxx(Object obj, Xxx val):将obj对象的该Field设置成val值,此处的Xxx对应8个
                             * 基本类型,如果该属性的类型是引用类型,则取消set后面的Xxx
                             */
                            field.set(classObj, v);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 事件注解
     * @param classObj
     * @param viewFinder
     * @throws Exception
     */
    private static void injectListeners(Object classObj, ViewFinder viewFinder) throws Exception
    {
        Class<?> clazz = classObj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
        {
            try
            {
                if (method.isAnnotationPresent(OnClick.class))
                {
                    setOnClickListener(classObj, viewFinder, method);
                }
                else if (method.isAnnotationPresent(OnLongClick.class))
                {
                    setOnLongClickListener(classObj, viewFinder, method);
                }
                else if (method.isAnnotationPresent(OnItemClick.class))
                {
                    setOnItemClickListener(classObj, viewFinder, method);
                }
                else if (method.isAnnotationPresent(OnItemLongClick.class))
                {
                    setOnItemLongClickListener(classObj, viewFinder, method);
                }
                else if(method.isAnnotationPresent(OnCheckedChanged.class))
                {
                    setOnCheckedChangeListener( classObj,  viewFinder,  method);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单击事件绑定
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnClickListener(Object classObj, ViewFinder viewFinder, Method method)
    {
        OnClick onclick = method.getAnnotation(OnClick.class);
        int[] ids = onclick.value();
        if (ids != null)
        {
            for (int id : ids)
            {
                View view = viewFinder.findViewById(id);
                if (view != null)
                {
                    view.setOnClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * 长按事件绑定
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnLongClickListener(Object classObj, ViewFinder viewFinder, Method method)
    {
        OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
        int[] ids = onLongClick.value();
        if (ids != null)
        {
            for (int id : ids)
            {
                View view = viewFinder.findViewById(id);
                if (view != null)
                {
                    view.setOnLongClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * Item单击事件
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnItemClickListener(Object classObj, ViewFinder viewFinder, Method method)
    {
        OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
        int[] ids = onItemClick.value();
        if (ids != null)
        {
            for (int id : ids)
            {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof AbsListView)
                {
                    ((AbsListView) view).setOnItemClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * Item长按事件
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnItemLongClickListener(Object classObj, ViewFinder viewFinder, Method method)
    {
        OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
        int[] ids = onItemLongClick.value();
        if (ids != null)
        {
            for (int id : ids)
            {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof AbsListView)
                {
                    ((AbsListView) view).setOnItemLongClickListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }

    /**
     * checkbox选中事件
     * @param classObj
     * @param viewFinder
     * @param method
     */
    private static void setOnCheckedChangeListener(Object classObj, ViewFinder viewFinder, Method method)
    {
        OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
        int[] ids = onCheckedChanged.value();
        if (ids != null)
        {
            for (int id : ids)
            {
                View view = viewFinder.findViewById(id);
                if (view != null && view instanceof CompoundButton)
                {
                    ((CompoundButton) view).setOnCheckedChangeListener(new EventListener(classObj, method.getName()));
                }
            }
        }
    }
}
