package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.BookClassifyAdapter;
import com.jiaoshizige.teacherexam.adapter.BookClassifyTypeAdapter;
import com.jiaoshizige.teacherexam.adapter.PopupwindowAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CatBookResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/a1/21.
 * 图书分类全部popupwindow
 */

public class BookClassifyPopupWindow extends PopupWindow {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<CatBookResponse.mChild> mData;
    private View popupView;
    private MScrollerGridView mType;
    private MScrollerGridView mClassify;
    private PopupwindowAdapter mAdapter;
    private TextView mPopDismess;
    private BookClassifyAdapter mBookClassifyAdapter;
    private BookClassifyTypeAdapter mBookAdapter;


    //默认第几项
    private int clickTemp = -1;
    private int mPo;


    public BookClassifyPopupWindow(Context context, List<CatBookResponse.mChild> data) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
        popupView = mInflater.inflate(R.layout.book_classify_popupwindow, null);
        mBookClassifyAdapter = new BookClassifyAdapter(context);
        mBookAdapter = new BookClassifyTypeAdapter(context);
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        initPopView();
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.update();
    }

    private void initPopView() {
        mPopDismess = (TextView) popupView.findViewById(R.id.pop_dismiss);
        mType = (MScrollerGridView) popupView.findViewById(R.id.type);
        mClassify = (MScrollerGridView) popupView.findViewById(R.id.classify);
        mClassify.setAdapter(mBookClassifyAdapter);
        mType.setAdapter(mBookAdapter);
        checkBook();
//        mType.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        mClassify.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mPopDismess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        mClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mBookClassifyAdapter.setSelection(position);
                mBookClassifyAdapter.notifyDataSetChanged();
                mData = mBookClassifyAdapter.getItem(position);
                Log.e("**********size", mData.size() + "");
                mBookAdapter.setmList(mData);
                mType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBookAdapter.setSelection(position);
                        mBookAdapter.notifyDataSetChanged();
                        CatBookResponse.mChild mChild= (CatBookResponse.mChild) parent.getAdapter().getItem(position);

                        GloableConstant.getInstance().setCatId(mChild.getId());
                        GloableConstant.getInstance().setTypeName(mChild.getCat_name());
                        Log.e("*******", GloableConstant.getInstance().getCatId() + "/*/*/*/***" + GloableConstant.getInstance().getTypeName());
                        dismiss();


                    }
                });
//
            }
        });

    }

    /**
     * 图书筛选
     *
     * @param
     */
    private void checkBook() {
        Map<String, Object> map = new HashMap<>();
        GloableConstant.getInstance().showmeidialog(mContext);
        Xutil.Post(ApiUrl.CHECKBOOK, map, new MyCallBack<CatBookResponse>() {
            @Override
            public void onSuccess(final CatBookResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    Log.e("********size", result.getData().size() + "");
                    mBookClassifyAdapter.setmList(result.getData());

                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }


    //public void setOnItemSelectedListener(AdapterView.OnItemClickListener itemClickListener) {
//        if (null != itemClickListener && null != mType) {
//            mType.setOnItemClickListener(itemClickListener);
//            dismiss();
//        }
//    if (null != itemClickListener && null != mClassify) {
//        mClassify.setOnItemClickListener(itemClickListener);
//        dismiss();
//    }
//    }

    public void setSelection(int position) {
        clickTemp = position;
    }

    private void specialUpdate() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }


    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void Dismiss() {
        dismiss();
    }
}
