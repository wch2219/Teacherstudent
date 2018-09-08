package com.jiaoshizige.teacherexam.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

public abstract class MBaseFragment extends Fragment {

    /** Fragment当前状态是否可见 */
    protected boolean  isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }else {
            isVisible=false;
            onInvisible();
        }
    }
    /* 可见*/
    protected void  onVisible() {
//        lazyLoad();
    }

    /* 不可见*/
    protected void  onInvisible() {

    }
    /* 延迟加载子类必须重写此方法*/

//    protected abstract  void  lazyLoad();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化控件
     * 设置监听
     * @param savedInstanceState
     */
    protected abstract void onInitViews(Bundle savedInstanceState);
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onInitViews(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void refresh(BusRefreshClass refreshClass){
        if (refreshClass.mClassName.equals(getClass().getSimpleName())) {
            refresh();
        } else if("refresh".equals(refreshClass.mClassName)){//全页面刷新
            refresh();
        }
    }
    protected void refresh(){

    }

}
