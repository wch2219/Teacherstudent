package com.jiaoshizige.teacherexam.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public abstract class MPBaseFragment extends Fragment {

    /** Fragment当前状态是否可见 */
    protected boolean  isVisible;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()){
//            isVisible=true;
//            onVisible();
//        }else {
//            isVisible=false;
//            onInvisible();
//        }
//    }
    /* 可见*/
//    protected void  onVisible() {
////        lazyLoad();
//    }
//
//    /* 不可见*/
//    protected void  onInvisible() {
//
//    }
//    /* 延迟加载子类必须重写此方法*/
//
//    protected void lazyLoad() {
//
//    }


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
    }

}
