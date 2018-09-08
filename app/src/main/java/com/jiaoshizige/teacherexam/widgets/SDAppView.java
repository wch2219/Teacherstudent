package com.jiaoshizige.teacherexam.widgets;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


import com.jiaoshizige.teacherexam.model.busmodel.BusIsLogin;
import com.jiaoshizige.teacherexam.utils.SDViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;


/**
 * 如果手动的new对象的话Context必须传入Activity对象
 *
 * @author Administrator
 * @date 2016-6-29 上午11:27:25
 */
public class SDAppView extends LinearLayout implements
        View.OnClickListener,
        Application.ActivityLifecycleCallbacks
{

    private boolean isRegisterEventBus = false;
    private boolean interceptTouchEvent = false;

    public SDAppView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        baseInit();
    }

    public SDAppView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        baseInit();
    }

    public SDAppView(Context context)
    {
        super(context);
        baseInit();
    }

    public void setInterceptTouchEvent(boolean interceptTouchEvent)
    {
        this.interceptTouchEvent = interceptTouchEvent;
    }


    public Activity getActivity()
    {
        Context context = getContext();
        if (context instanceof Activity)
        {
            return (Activity) context;
        } else
        {
            return null;
        }
    }

    private void baseInit()
    {
        int layoutId = onCreateContentView();
        if (layoutId != 0)
        {
            setContentView(layoutId);
        }
        baseConstructorInit();
    }

    /**
     * 基类构造方法调用的初始化方法，如果重写此方法初始化需要注意：
     * 1.子类不应该在此方法内访问子类定义属性时候直接new的属性，如：private String value = "value"，否则value的值将为null
     * 2.子类应该在此方法内初始化属性
     */
    protected void baseConstructorInit()
    {

    }

    /**
     * 重写此方法返回布局id
     *
     * @return
     */
    protected int onCreateContentView()
    {
        return 0;
    }


    public void setContentView(int layoutId)
    {
        LayoutInflater.from(getContext()).inflate(layoutId, this, true);
        ButterKnife.bind(this);
    }

    public <V extends View> V find(int id)
    {
        View view = findViewById(id);
        return (V) view;
    }



    /**
     * 为了统一规范，子类可以重写此方法初始化，并在合适时间调用
     */
    protected void init()
    {

    }



    @Override
    public void onClick(View v)
    {
    }



    public boolean isVisible()
    {
        return View.VISIBLE == getVisibility();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (interceptTouchEvent)
        {
            super.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean dispatchTouchEventActivity(MotionEvent ev)
    {
        if (isVisible())
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if (SDViewUtil.isTouchView(this, ev))
                    {
                        if (onTouchDownInside(ev))
                        {
                            return true;
                        }
                    } else
                    {
                        if (onTouchDownOutside(ev))
                        {
                            return true;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        return false;
    }

    protected boolean onTouchDownOutside(MotionEvent ev)
    {
        return false;
    }

    protected boolean onTouchDownInside(MotionEvent ev)
    {
        return false;
    }

    public boolean dispatchKeyEventActivity(KeyEvent event)
    {
        if (isVisible())
        {
            switch (event.getAction())
            {
                case KeyEvent.ACTION_DOWN:
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    {
                        return onBackPressed();
                    }
                    break;

                default:
                    break;
            }
        }
        return false;
    }

    public boolean onBackPressed()
    {
        return false;
    }

    public void registerEventBus()
    {
        if (!isRegisterEventBus)
        {
            EventBus.getDefault().register(this);
            isRegisterEventBus = true;
        }
    }

    public void unregisterEventBus()
    {
        EventBus.getDefault().unregister(this);
        isRegisterEventBus = false;
    }

    @Override
    protected void onAttachedToWindow()
    {
        registerEventBus();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        unregisterEventBus();
        super.onDetachedFromWindow();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {

    }

    @Override
    public void onActivityResumed(Activity activity)
    {

    }

    @Override
    public void onActivityPaused(Activity activity)
    {

    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {

    }
    @Subscribe
    public void isLogin(BusIsLogin isLogin){

    }
}
