package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.MessageCenterAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.SwipeListLayout;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/20 0020.
 * (废弃)
 */

public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.list_view)
    ListView mListView;
    private int page = 1;
    private int page_size = 10;
    private MessageCenterAdapter mMessageCenterAdapter;
    private View mHead;
    private String type;
    private Set<SwipeListLayout> sets = new HashSet();
    private LinearLayout mS;
    private TextView mDot;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.message_center_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();

        personPush();
        systemPush();
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("消息中心");
        mContext=this;
        mHead = View.inflate(this, R.layout.head_view, null);
        mS= (LinearLayout) mHead.findViewById(R.id.item_message);
        mDot= (TextView) mHead.findViewById(R.id.red_dot);
        mListView.addHeaderView(mHead);
        mMessageCenterAdapter = new MessageCenterAdapter(this);
        mListView.setAdapter(mMessageCenterAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast("头部点击事件");
                startActivity(new Intent(MessageCenterActivity.this, WangXiaoActivity.class));
            }
        });


        personPush();
        systemPush();
    }

    private void personPush() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 1);
        map.put("page", page);
        map.put("page_size", page_size);
        Log.e("*********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.Post(ApiUrl.PUSH, map, new MyCallBack<MessageCenterResponse>() {
            @Override
            public void onSuccess(MessageCenterResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    mMessageCenterAdapter.setmList(result.getData());
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                ToastUtil.showShortToast(ex.getMessage());
            }
        });
    }
    private void systemPush(){
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 2);
        map.put("page", page);
        map.put("page_size", page_size);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Log.e("*********map", map.toString());
        Xutil.Post(ApiUrl.PUSH, map, new MyCallBack<MessageCenterResponse>() {
            @Override
            public void onSuccess(MessageCenterResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    for (int i=0;i<result.getData().size();i++){
                        if (result.getData().get(i).getIs_read().equals("0")){
                            mDot.setVisibility(View.VISIBLE);
                        }else {
                            mDot.setVisibility(View.GONE);
                        }
                    }
                }else {
                    mDot.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShortToast(ex.getMessage());
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
}
