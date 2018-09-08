package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.WangXiaoAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.SwipeListLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class WangXiaoActivity extends BaseActivity {
    @BindView(R.id.class_list)
    ListView mListView;
    private int page = 1;
    private int page_size = 10;
    private Context mContext;
    private String messageId;
    private Intent intent;
    private Set<SwipeListLayout> sets = new HashSet();
    private WangXiaoAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.wang_xiao_layout;
    }

    @Override
    protected void initView() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        setSubTitle().setText("");
        setToolbarTitle().setText("系统公告");
        mContext = this;
        mAdapter = new WangXiaoAdapter(mContext);
        mListView.setAdapter(mAdapter);

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

        push();
    }

    private void push() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type",2);
        map.put("page", page);
        map.put("page_size", page_size);
        Log.e("***********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PUSH, map, new MyCallBack<MessageCenterResponse>() {
            @Override
            public void onSuccess(MessageCenterResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    mAdapter.setmList(result.getData());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }

}
