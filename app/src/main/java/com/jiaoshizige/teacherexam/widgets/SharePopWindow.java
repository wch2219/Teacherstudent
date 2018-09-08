package com.jiaoshizige.teacherexam.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jiaoshizige.teacherexam.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/a1/9 0009.
 */

public class SharePopWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;
    private View mContentView;

    private LinearLayout mWeiXin;
    private LinearLayout mPengYouQuan;
    private LinearLayout mQQ;
    private LinearLayout mKongJian;
    private LinearLayout mDuanXin;
    private LinearLayout mSina;
    private Button mCancleBtn;


    public static final int POP_WINDOW_ITEM_1 = 1;
    public static final int POP_WINDOW_ITEM_2 = 2;
    public static final int POP_WINDOW_ITEM_3 = 3;
    public static final int POP_WINDOW_ITEM_4 = 4;
    public static final int POP_WINDOW_ITEM_5 = 5;
    public static final int POP_WINDOW_ITEM_6 = 6;


    private OnSelectItemListener listener;

    public void setOnSelectItemListener(OnSelectItemListener listener) {
        this.listener = listener;
    }

    public SharePopWindow(Activity activity) {
        super(activity);
        ButterKnife.bind(activity);
        this.mContext = activity;
        this.mActivity = activity;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.item_share_pop_window_layout, null);
        mWeiXin = (LinearLayout) mContentView.findViewById(R.id.share_weixin);
        mPengYouQuan = (LinearLayout) mContentView.findViewById(R.id.share_pengyouquan);
        mQQ = (LinearLayout) mContentView.findViewById(R.id.share_qq);
        mKongJian= (LinearLayout) mContentView.findViewById(R.id.share_kongjian);
        mDuanXin= (LinearLayout) mContentView.findViewById(R.id.share_duanxin);
        mSina= (LinearLayout) mContentView.findViewById(R.id.share_weibo);
        mCancleBtn = (Button) mContentView.findViewById(R.id.share_pop_button);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPopupWindow.setOutsideTouchable(true);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
//        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        mWeiXin.setOnClickListener(this);
        mPengYouQuan.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mKongJian.setOnClickListener(this);
        mDuanXin.setOnClickListener(this);
        mSina.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_weixin:
                if (listener != null) {
                    listener.selectItem("微信", POP_WINDOW_ITEM_1);     //回调接口
                }
                break;
            case R.id.share_pengyouquan:
                if (listener != null) {
                    listener.selectItem("朋友圈", POP_WINDOW_ITEM_2);     //回调接口
                }
                break;
            case R.id.share_qq:
                if (listener != null) {
                    listener.selectItem("QQ", POP_WINDOW_ITEM_3);     //回调接口
                }
                break;
            case R.id.share_kongjian:
                if (listener != null) {
                    listener.selectItem("QQ空间", POP_WINDOW_ITEM_4);     //回调接口
                }
                break;
            case R.id.share_duanxin:
                if (listener != null) {
                    listener.selectItem("短信", POP_WINDOW_ITEM_5);     //回调接口
                }
                break;
            case R.id.share_weibo:
                Log.e("wwwww","www3");
                if (listener != null) {
                    Log.e("wwwww","www4");
                    listener.selectItem("新浪微博", POP_WINDOW_ITEM_6);     //回调接口
                }
                break;
            case R.id.share_pop_button:
                dismiss();
                break;
        }
    }
    /**
     * 退出popupwindow
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
//            LiveActivity activity=new LiveActivity();
//            activity.Resume();
            mPopupWindow.dismiss();
        }
    }
    /**
     * popupwindow是否正在显示
     */
    public boolean isShowing() {
        if (mPopupWindow != null) {
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
