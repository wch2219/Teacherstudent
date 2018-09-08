package com.jiaoshizige.teacherexam.utils;

/**
 * Created by Administrator on 2017/a1/22 0022.
 */

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;

/**
 * 自定义一个控件让RadioButton跟随点击ListView的变化而变化
 */
public class ChoiceView extends FrameLayout implements Checkable {

    RadioButton child;
    TextView mText;

    public ChoiceView(Context context) {
        super(context);
        View.inflate(context, R.layout.examtype,this);
        mText = (TextView) findViewById(R.id.tv_item);
        child = (RadioButton) findViewById(R.id.rb_item);
    }

    @Override
    public void setChecked(boolean checked) {
        child.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return child.isChecked();
    }

    @Override
    public void toggle() {
        child.toggle();
    }

    public void setTextView(String text){
        mText.setText(text);
    }
}
