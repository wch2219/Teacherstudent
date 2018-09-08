package com.jiaoshizige.teacherexam.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;


/**
 * Created by Administrator on 2017/8/25.
 */

public class CancelOrOkDialog extends Dialog {

    public CancelOrOkDialog(Context context, String title) {
        //使用自定义Dialog样式
        super(context,R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.product_phone_pop_window_avtivity);
        //点击外部不可消失
        setCancelable(false);

        //设置标题
        TextView titleTv = (TextView) findViewById(R.id.dialog_content);
        titleTv.setText(title);

        findViewById(R.id.product_phone_call_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                //取消
                cancel();
            }
        });
        TextView mSure = (TextView) findViewById(R.id.product_phone_call_out);
        mSure.setText("确定");
        findViewById(R.id.product_phone_call_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ok();
            }
        });
    }

    //确认
    public void ok() {

    }
}
