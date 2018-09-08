package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.WelcomeActivity;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class GuidePageAdapter extends PagerAdapter {
    private List<View> mViewList;
    private WelcomeActivity mContext;

    public GuidePageAdapter(List<View> mViewList,WelcomeActivity activity) {
        this.mViewList = mViewList;
        this.mContext = activity;
    }

    /**
     * @return 返回页面的个数
     */

    @Override
    public int getCount() {
        if (mViewList!=null){
            return mViewList.size();
        }
        return 0;
    }

    /**
     * 判断对象是否生成界面
     * @param view
     * @param object
     * @return
     */

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==object;
    }


    /**
     * 初始化position位置的界面
     * @param container
     * @param position
     * @return
     */

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        if(position == 2){
            ImageView imageView = (ImageView) mViewList.get(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sphelper.save(mContext, "isFirstUse", "isfiestuser", true);
                    if(SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING).equals("1") && SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) != null){
                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }else{
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        mContext.finish();
                    }
                }
            });
        }
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
