package com.jiaoshizige.teacherexam.mycourse.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseFragment;

/**
 * Created by Administrator on 2018/8/21.
 */

public class CourseTaskFragment extends MBaseFragment {
    @Override
    protected void onInitViews(Bundle savedInstanceState) {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_tast, container, false);
    }
}