package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.PopupwindowAdapter;
import com.jiaoshizige.teacherexam.model.NoteAndHomeWorkResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 * maxiao
 * 动态添加数据的popupwindow
 */

public class AllPopupwindow extends PopupWindow {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<NoteaWorkChapterResponse.mData> mDatas = new ArrayList<>();
    private View popupView;
    private ListView mContentLv;
    private PopupwindowAdapter mAdapter;
    AdapterView.OnItemClickListener itemClickListener;
    private TextView mPopDismess;
    private String mFlag;

    public AllPopupwindow(Context context, List<NoteaWorkChapterResponse.mData> mDatas, String flag){
        this.mFlag = flag;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        popupView = mInflater.inflate(R.layout.all_popupwindow_layout,null);
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //初始化控件
        initPopView();
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        //需要动画效果的话可以设置
        //this.setAnimationStyle(R.style.PopupWindowAnimation);
        this.update();
    }
    private void initPopView() {
        mPopDismess = (TextView) popupView.findViewById(R.id.pop_dismiss);
        mContentLv = (ListView) popupView.findViewById(R.id.pop_list);
        mContentLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mAdapter = new PopupwindowAdapter(mContext, mDatas,this,mFlag);
        mContentLv.setAdapter(mAdapter);

        mPopDismess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteAndHomeWorkResponse.mList mData = (NoteAndHomeWorkResponse.mList) parent.getAdapter().getItem(position);


                Log.e("TAG","sdsdsdsdsdsd");

            }
        });
    }
   public void setOnItemSelectedListener(AdapterView.OnItemClickListener itemClickListener) {
        if (null != itemClickListener && null != mContentLv) {
            mContentLv.setOnItemClickListener(itemClickListener);

            dismiss();
        }
   }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
    public void Dismiss(){
        dismiss();
    }
}
