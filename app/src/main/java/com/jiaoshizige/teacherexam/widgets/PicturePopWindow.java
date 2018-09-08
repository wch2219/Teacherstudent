package com.jiaoshizige.teacherexam.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;

/**
 * Created by Administrator on 2017/a1/6 0006.
 */

public class PicturePopWindow extends PopupWindow implements View.OnClickListener {
    private View mContentView;
    private RelativeLayout take_photo, choose;
    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;
    private TextView dismiss;

    public static final int POP_WINDOW_ITEM_1 = 1;
    public static final int POP_WINDOW_ITEM_2 = 2;
    public static final int POP_WINDOW_ITEM_3 = 3;
    private OnSelectItemListener listener;

    public void setOnSelectItemListener(OnSelectItemListener listener) {
        this.listener = listener;
    }

    public PicturePopWindow(Activity activity) {
        super(activity);
        this.mContext = activity;
        this.mActivity = activity;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.my_image_pop_window, null);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPopupWindow.setOutsideTouchable(true);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        take_photo = (RelativeLayout) mContentView.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);
        choose = (RelativeLayout) mContentView.findViewById(R.id.choose);
        choose.setOnClickListener(this);
        dismiss = (TextView) mContentView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                if (listener != null) {
                    listener.selectItem("拍照", POP_WINDOW_ITEM_1);     //回调接口
                }
                break;
            case R.id.choose:
                if (listener != null) {
                    listener.selectItem("从手机相册选择", POP_WINDOW_ITEM_2);
                }
                break;
            case R.id.dismiss:
                if (listener != null) {
                    listener.selectItem("取消", POP_WINDOW_ITEM_3);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 退出popupwindow
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * popupwindow是否正在显示
     */
    public boolean isShowing() {
        if(mPopupWindow != null) {
            return mPopupWindow.isShowing();
        }
        return false;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            setBackgroundAlpha(1f);
        }
    }


}
