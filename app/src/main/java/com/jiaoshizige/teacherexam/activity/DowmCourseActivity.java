package com.jiaoshizige.teacherexam.activity;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class DowmCourseActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.down_course_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的缓存");
    }
}
