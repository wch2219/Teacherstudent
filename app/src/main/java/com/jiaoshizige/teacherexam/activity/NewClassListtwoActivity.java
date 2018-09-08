package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.NewPrivateCourseFragmentTwoAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassListResponse;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.NewCourseListListResponse;
import com.jiaoshizige.teacherexam.model.NewCourseListResponse;
import com.jiaoshizige.teacherexam.model.NewCourseResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/11.
 */

public class NewClassListtwoActivity extends BaseActivity {
    @BindView(R.id.class_list)
    RecyclerView mCourseList;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    List<ClassListResponse.mData> mList = new ArrayList<>();
    // private String mPage = "1";
    private String mPageSize = "15";
    private int mPageNum = 1;
    private Context mContext;
    List<NewCourseResponse.mNozero> mDataList = new ArrayList<>();
    private NewCourseResponse.mZero mZero;

    @Override
    protected int getLayoutId() {
        return R.layout.information_list_activity;
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("0元体验专区");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = 1;
                getCourse("10", "", "");
            }

            @Override
            public void loadMore() {
                mPageNum++;
                getCourse("10", "", "");
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = 1;
                getCourse("10", "", "");
            }
        });
        getCourse("10", "", "");
    }

    @Subscribe()
    public void onMessageEvent(MessageEvent event) {
        Log.e("+++++++++++++++", "zhixiong" + event.getMessage());
        if (event.getMessage().equals("更换考试类型")) {
            mPageNum = 1;
            getCourse("10", "", "");
        }
    }

    private void getCourse(String type_id, String order, String sort) {
        Map<String, String> map = new HashMap<>();
        map.put("type_id", type_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order", order);
        map.put("sort", sort);
        map.put("page", String.valueOf(mPageNum));
        map.put("page_size", mPageSize);
        Log.d("********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(NewClassListtwoActivity.this);
        Xutil.GET(ApiUrl.NEWCOURSE, map, new MyCallBack<NewCourseResponse>() {
            @Override
            public void onSuccess(final NewCourseResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    Log.d("**********size", result.getData().getNozero().size() + "/////");

                    if (result.getData().getNozero().size() > 0 && result.getData().getNozero() != null) {
                        if (mPageNum == 1) {
                            mDataList.clear();
                            mDataList = result.getData().getNozero();
                        } else {
                            if (null == mDataList) {
                                mDataList = result.getData().getNozero();
                            } else {
                                mDataList.addAll(result.getData().getNozero());
                            }
                        }
                        mCourseList.setLayoutManager(new LinearLayoutManager(NewClassListtwoActivity.this));
                        mCourseList.setAdapter(new CommRecyclerViewAdapter<NewCourseResponse.mNozero>(NewClassListtwoActivity.this, R.layout.item_new_course_list, mDataList) {

                            @Override
                            protected void convert(ViewHolderZhy holder, final NewCourseResponse.mNozero mNozero, int position) {
                                holder.setText(R.id.class_name, mNozero.getGroup_name());
                                TextView mMarkPrice = (TextView) holder.getConvertView().findViewById(R.id.market_price);
//                                mMarkPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
//                                mMarkPrice.setText("￥"+mNozero.getMarket_price());
                                mMarkPrice.setVisibility(View.GONE);
                                TextView price = holder.getConvertView().findViewById(R.id.price);
                                holder.setBackgroundRes(R.id.course_type, R.drawable.purple_bottom_shape5);
                                if (mNozero.getPrice().equals("0.00")) {
                                    price.setText("免费");
                                    price.setTextSize(20);
                                    price.setTextColor(getResources().getColor(R.color.green1));
                                    holder.setVisible(R.id.fuhao, false);
                                }

                                holder.setText(R.id.learn_num, mNozero.getSale_num());
                                if (!mNozero.getTeacher_name().equals("")) {
                                    holder.setText(R.id.teacher_name, mNozero.getTeacher_name());
                                } else {
                                    holder.setVisible(R.id.teacher_name, false);
                                }
                                if (!mNozero.getAssistant_name().equals("")) {
                                    holder.setText(R.id.assistant_name, mNozero.getAssistant_name());
                                } else {
                                    holder.setVisible(R.id.assistant_name, false);
                                }

                                holder.setText(R.id.course_type, mNozero.getBq());
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.image);
                                Glide.with(mContext).load(mNozero.getBq_img())
                                        .apply(GloableConstant.getInstance().getOptions())
                                        .into(imageView);
                                holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        //1班级2课程
                                        intent.setClass(NewClassListtwoActivity.this, CoursesDetialActivity.class);
                                        intent.putExtra("course_id", mNozero.getType_id());
//                                        intent.putExtra("position",position);
                                        intent.putExtra("type", mNozero.getType());   //1班级2课程
                                        intent.putExtra("is_buy", mNozero.getIs_buy());
                                        startActivity(intent);
                                        Log.e("********type", mNozero.getType() + "/////////" + mNozero.getType_id());
                                    }
                                });
                            }
                        });

                    } else {
                        if (mPageNum > 1) {
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    if (mPageNum > 1) {
                        ToastUtil.showShortToast("没有更多数据了");
                    } else if (result.getStatus_code().equals("401")) {
                        GloableConstant.getInstance().clearAll();
                        ToastUtil.showShortToast("请重新登录");
                        startActivity(new Intent(NewClassListtwoActivity.this, LoginActivity.class));
                    } else {
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                Log.d("**********ex", ex.getMessage() + "//////");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
