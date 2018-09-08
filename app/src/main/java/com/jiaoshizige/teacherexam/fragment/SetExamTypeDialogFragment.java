package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ExamSubjectSelect;

/**
 * Created by Administrator on 2018/6/5.
 */

@SuppressLint("ValidFragment")
public class SetExamTypeDialogFragment extends DialogFragment {
    private LinearLayout mToSet;
    private Activity mContext;
    @SuppressLint("ValidFragment")
    public SetExamTypeDialogFragment(Activity context) {
        this.mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.select_exam_type_popwindow, container);
        mToSet = (LinearLayout)dialogView.findViewById(R.id.button);
        mToSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExamSubjectSelect.class));
            }
        });
        return dialogView;
    }
    public void dialogDismiss(){
        dismiss();
    }
}
