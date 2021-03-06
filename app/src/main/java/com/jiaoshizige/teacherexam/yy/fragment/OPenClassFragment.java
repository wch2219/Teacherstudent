package com.jiaoshizige.teacherexam.yy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.activity.LiveActivity;
import com.jiaoshizige.teacherexam.widgets.CustomDigitalClock;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.OpenClassDetailActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.model.OpenClassResponse;
import com.jiaoshizige.teacherexam.utils.GifView;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/27.
 * 公开课
 */

public class OPenClassFragment extends MBaseFragment {
    @BindView(R.id.now_recycle_view)
    RecyclerView mNow;
    @BindView(R.id.now_text)
    TextView mNowText;
    @BindView(R.id.recent_recycle_view)
    RecyclerView mRecent;
    @BindView(R.id.recent_text)
    TextView mRecentText;
    @BindView(R.id.history_recycle_view)
    RecyclerView mHistory;
    @BindView(R.id.history_text)
    TextView mHistoryText;
    @BindView(R.id.now_live_ly)
    LinearLayout mNowLiveLy;
    @BindView(R.id.near_live_ly)
    LinearLayout mNearLiveLy;
    @BindView(R.id.history_live_ly)
    LinearLayout mHistoryLiveLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayout mPullToRefresh;
    @BindView(R.id.course_data)
    TextView mCourseData;
    /*   @BindView(R.id.subscribe_ly)
       LinearLayout mSubscribeLy;
       @BindView(R.id.subscribe_time)
       CustomDigitalClock mSubscribeTime;
       @BindView(R.id.live_subscribe)
       TextView mLiveSubscribe;
       @BindView(R.id.is_liveing)
       RelativeLayout mIsLiveing;*/
    private LinearLayout mLive;
    private GifView gif;
    private String _id;
    private Intent intent;
    private long time1;
    private long time2;
    private long time;
    private Long mNoewTime = System.currentTimeMillis();
    private int page = 1;
    private List<OpenClassResponse.mNow> mNows = new ArrayList<>();
    private List<OpenClassResponse.mRecent> mRecents = new ArrayList<>();
    private List<OpenClassResponse.mHistory> mHistorys = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.open_class_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("公开课");
        openClass();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公开课");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        mPullToRefresh.setNoLoadMore(1);
//        mPullToRefresh.setPullRefreshListener(new PullRefreshListener() {
//            @Override
//            public void refresh() {
//                openClass();
//            }
//
//
//        });
//        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
//            @Override
//            public void onCallBackListener(View v) {
//                openClass();
//            }
//        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                page = 1;
                openClass();
            }

            @Override
            public void loadMore() {
                page++;
                openClass();

            }
        });
        openClass();
        time = System.currentTimeMillis();
    /*    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    time=TimeUtils.stringToLong(TimeUtils.getNetTime(),"yyyy-MM-dd HH:mm:ss");
                    Log.e("**************time", TimeUtils.getNetTime()+"//////"+TimeUtils.stringToLong(TimeUtils.getNetTime(),"yyyy-MM-dd HH:mm:ss")+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBus(String name) {
        if (name.equals("倒计时结束")) {
            ToastUtil.showShortToast(name);
//    openClass();

        }
    }

    private long timeNow = 0;

    private void openClass() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("page", page + "");
        Log.e("**********open", map.toString());
        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.GET(ApiUrl.OPEN, map, new MyCallBack<OpenClassResponse>() {
            @Override
            public void onSuccess(final OpenClassResponse result) {
                super.onSuccess(result);
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********openresult", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        if (result.getData().getNow() != null && result.getData().getNow().size() > 0) {
                            Log.e("*********nowsize", result.getData().getNow().size() + "");
                            if (page == 1) {
                                mNows.clear();
                                mNows = result.getData().getNow();
                            } else {
                                if (null == mNows) {
                                    mNows = result.getData().getNow();
                                } else {
                                    mNows.addAll(result.getData().getNow());
                                }
                            }
                            GloableConstant.getInstance().setmNows(mNows);
                            mNowLiveLy.setVisibility(View.VISIBLE);
                            mNow.setVisibility(View.VISIBLE);
                            mNowText.setVisibility(View.VISIBLE);
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            mNow.setLayoutManager(manager);
                            mNow.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
                            mNow.setAdapter(new CommonAdapter<OpenClassResponse.mNow>(getActivity(), R.layout.item_open_class_layout, mNows) {
                                @Override
                                protected void convert(final ViewHolder holder, final OpenClassResponse.mNow mNow, final int position) {
                                    Log.e("*********mNow.getName()", mNow.getName());

                                    CustomDigitalClock mSubscribeTime = (CustomDigitalClock) holder.getConvertView().findViewById(R.id.subscribe_time);
                                    TextView live_subscribe = (TextView) holder.getConvertView().findViewById(R.id.live_subscribe);
                                    //当前时间 大于 开播时间
                                    mCourseData.setText(mNow.getDate_y2() + "  " + mNow.getDate_d());
                                    mLive = (LinearLayout) holder.getConvertView().findViewById(R.id.live);
                                    gif = (GifView) holder.getConvertView().findViewById(R.id.gif);
                                    // 设置背景gif图片资源
                                    gif.setMovieResource(R.raw.picture);
                                    final TextView textView = (TextView) holder.getConvertView().findViewById(R.id.class_price);
                                    ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.class_cover);
                                    Glide.with(getActivity()).load(mNow.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                    holder.setText(R.id.class_title, mNow.getName());

                                    time1 = mNow.getStart_times();
                                    time2 = mNow.getEnd_times();
                                    time = System.currentTimeMillis();
                                    Log.e("**********time", ToolUtils.getTime(time1, time) + "///////time1" + time1);


                                    if (ToolUtils.getTime(time1, time).equals("1")) {
                                        if (ToolUtils.getTime(time, time2).equals("1")) {
                                            gif.setVisibility(View.VISIBLE);
                                            if (mNow.getIs_buy().equals("1")) {
                                                holder.setVisible(R.id.class_num, true);
                                                holder.setVisible(R.id.class_price, false);
                                                holder.setText(R.id.class_num, mNow.getSale_num() + "人在观看");
                                            } else {
                                                holder.setVisible(R.id.class_num, false);
                                                holder.setVisible(R.id.class_price, true);
                                                holder.setText(R.id.class_price, "立即预约");
                                                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                                holder.setBackgroundRes(R.id.class_price, R.drawable.yellowall);
                                            }

                                            holder.setText(R.id.text, "直播中");
                                            holder.setTextColor(R.id.text, R.color.purple4);
                                            holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    _id = mNow.getId();
                                                    intent = new Intent();
                                                    if (System.currentTimeMillis() - timeNow > 2000) {
                                                        if (result.getData().getNow().get(position).getIs_buy().equals("0")) {
                                                            intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                            intent.putExtra("_id", _id);
                                                            intent.putExtra("recent", "now");
                                                            intent.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                            intent.putExtra("token", mNow.getLive_info().getToken());
                                                            intent.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                            startActivity(intent);
                                                        } else {
                                                            onLine(mNow.getLive_info().getApp_name(), mNow);
                                                        }
                                                        timeNow = System.currentTimeMillis();
                                                    }


                                                }
                                            });

                                        } else {
                                            gif.setVisibility(View.GONE);
                                            holder.setText(R.id.text, "直播已结束");
                                            holder.setTextColor(R.id.text, R.color.text_color9);
                                        }
                                    } else {
                                        holder.setVisible(R.id.subscribe_ly, true);
                                        holder.setVisible(R.id.is_liveing, false);
                                        if (mNow.getIs_buy().equals("1")) {
                                            holder.setText(R.id.live_subscribe, "已预约");
                                            live_subscribe.setBackgroundResource(R.drawable.gay_shape15);
                                            live_subscribe.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color6));
                                        } else {
                                            holder.setText(R.id.class_price, "立即预约");
                                            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                            holder.setBackgroundRes(R.id.class_price, R.drawable.yellowall);
                                        }

                                        Log.e("***getStart_times", mNow.getStart_times() + "******");
                                        mSubscribeTime.setEndTime(Long.valueOf(mNow.getStart_times()));
                                        mSubscribeTime.setClockListener(new CustomDigitalClock.ClockListener() {

                                            @Override
                                            public void timeEnd() {
                                                EventBus.getDefault().post("倒计时结束");
//                                                notifyDataSetChanged();
                                                holder.setVisible(R.id.subscribe_ly, false);
                                                holder.setVisible(R.id.is_liveing, true);
                                                if (mNow.getIs_buy().equals("1")) {
                                                    holder.setVisible(R.id.class_num, true);
                                                    holder.setVisible(R.id.class_price, false);
                                                    holder.setText(R.id.class_num, mNow.getSale_num() + "人在观看");
                                                } else {
                                                    holder.setVisible(R.id.class_num, false);
                                                    holder.setVisible(R.id.class_price, true);
                                                    holder.setText(R.id.class_price, "立即预约");
                                                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                                    holder.setBackgroundRes(R.id.class_price, R.drawable.yellowall);
                                                }

                                                holder.setText(R.id.text, "直播中");
                                                holder.setTextColor(R.id.text, R.color.purple4);
                                                holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        _id = mNow.getId();
                                                        intent = new Intent();
                                                        if (System.currentTimeMillis() - timeNow > 2000) {
                                                            if (result.getData().getNow().get(position).getIs_buy().equals("0")) {
                                                                intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                                intent.putExtra("_id", _id);
                                                                intent.putExtra("recent", "now");
                                                                intent.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                                intent.putExtra("token", mNow.getLive_info().getToken());
                                                                intent.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                                startActivity(intent);
                                                            } else {
                                                                onLine(mNow.getLive_info().getApp_name(), mNow);
                                                            }
                                                            timeNow = System.currentTimeMillis();
                                                        }


                                                    }
                                                });
                                            }

                                            @Override
                                            public void remainFiveMinutes() {

                                            }
                                        });

                                        holder.setOnClickListener(R.id.live_subscribe, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent1 = new Intent();
                                                _id = mNow.getId();
                                                if (mNow.getIs_buy().equals("1")) {
                                                    intent1.setClass(getActivity(), OpenClassDetailActivity.class);
                                                    intent1.putExtra("_id", _id);
                                                    intent1.putExtra("recent", "now");
                                                    intent1.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                    intent1.putExtra("token", mNow.getLive_info().getToken());
                                                    intent1.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                    intent1.putExtra("name", mNow.getLive_info().getName());
                                                    startActivity(intent1);
                                                } else {
                                                    intent1.setClass(getActivity(), OpenClassDetailActivity.class);
                                                    intent1.putExtra("_id", _id);
                                                    intent1.putExtra("recent", "now");
                                                    intent1.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                    intent1.putExtra("token", mNow.getLive_info().getToken());
                                                    intent1.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                    startActivity(intent1);
                                                }
                                            }
                                        });
                                    }


                                }
                            });

                        } else {
                            if (page == 1) {
                                mNowLiveLy.setVisibility(View.GONE);
                            } else {
                                if (GloableConstant.getInstance().getmNows() != null && GloableConstant.getInstance().getmNows().size() > 0) {
                                    mNows = GloableConstant.getInstance().getmNows();
                                    mNow.setVisibility(View.VISIBLE);
                                    mNowText.setVisibility(View.VISIBLE);
                                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    mNow.setLayoutManager(manager);
                                    mNow.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
                                    mNow.setAdapter(new CommonAdapter<OpenClassResponse.mNow>(getActivity(), R.layout.item_open_class_layout, mNows) {
                                        @Override
                                        protected void convert(final ViewHolder holder, final OpenClassResponse.mNow mNow, final int position) {
                                            Log.e("*********mNow.getName()", mNow.getName());
                                            CustomDigitalClock mSubscribeTime = (CustomDigitalClock) holder.getConvertView().findViewById(R.id.subscribe_time);
                                            TextView live_subscribe = (TextView) holder.getConvertView().findViewById(R.id.live_subscribe);
                                            //当前时间 大于 开播时间
                                 /*   if(ToolUtils.getTime(mNow.getStart_times(),mNoewTime).equals(1)){

                                    }else{

                                    }*/
                                            mCourseData.setText(mNow.getDate_y2() + "  " + mNow.getDate_d());
                                            mLive = (LinearLayout) holder.getConvertView().findViewById(R.id.live);
                                            mLive.setVisibility(View.VISIBLE);
                                            gif = (GifView) holder.getConvertView().findViewById(R.id.gif);
                                            // 设置背景gif图片资源
                                            gif.setMovieResource(R.raw.picture);
                                            TextView textView = (TextView) holder.getConvertView().findViewById(R.id.class_price);


//                                    if(mNow.getPrice().equals("0.00")){
//                                        textView.setTextColor(getResources().getColor(R.color.green));
//                                        holder.setText(R.id.class_price, "免费");
////                                        holder.setText(R.id.class_price, mNow.getSale_num()+"人在观看");
////                                        holder.setTextColor(R.id.class_price,R.color.green);
//                                    }else{
////
//                                        holder.setText(R.id.class_price, "￥" + mNow.getPrice());
//                                    }

                                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.class_cover);
                                            Glide.with(getActivity()).load(mNow.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                            holder.setText(R.id.class_title, mNow.getName());

                                            time1 = mNow.getStart_times();
                                            time2 = mNow.getEnd_times();
//                                    time = System.currentTimeMillis();
                                            Log.e("**********time", time + "");


                                            if (ToolUtils.getTime(time1, time).equals("1")) {
                                                if (ToolUtils.getTime(time, time2).equals("1")) {
                                                    gif.setVisibility(View.VISIBLE);
                                                    if (mNow.getIs_buy().equals("1")) {
                                                        holder.setVisible(R.id.class_num, true);
                                                        holder.setVisible(R.id.class_price, false);
                                                        holder.setText(R.id.class_num, mNow.getSale_num() + "人在观看");
                                                    } else {
                                                        holder.setVisible(R.id.class_num, false);
                                                        holder.setVisible(R.id.class_price, true);
                                                        holder.setText(R.id.class_price, "立即预约");
                                                        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                                        holder.setBackgroundRes(R.id.class_price, R.drawable.yellowall);
                                                    }

                                                    holder.setText(R.id.text, "直播中");
                                                    holder.setTextColor(R.id.text, R.color.purple4);
                                                    holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            _id = mNow.getId();
                                                            intent = new Intent();
                                                            if (System.currentTimeMillis() - timeNow > 2000) {
                                                                if (result.getData().getNow().get(position).getIs_buy().equals("0")) {
                                                                    intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                                    intent.putExtra("_id", _id);
                                                                    intent.putExtra("recent", "now");
                                                                    intent.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                                    intent.putExtra("token", mNow.getLive_info().getToken());
                                                                    intent.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                                    startActivity(intent);
                                                                } else {
                                                                    onLine(mNow.getLive_info().getApp_name(), mNow);
                                                       /* intent.setClass(getActivity(), LiveActivity.class);
//                                                        intent.putExtra("url", result.getData().getNow().get(position).getLive_info().getPush_url());
                                                        intent.putExtra("chat_room_id",mNow.getLive_info().getChat_room_id());
                                                        intent.putExtra("token",mNow.getLive_info().getToken());
                                                        intent.putExtra("live_url",mNow.getLive_info().getPush_url());
                                                        intent.putExtra("_id", _id);*/
                                                                }
                                                                timeNow = System.currentTimeMillis();
                                                            }


                                                        }
                                                    });

                                                } else {
                                                    gif.setVisibility(View.GONE);
                                                    holder.setText(R.id.text, "直播已结束");
                                                    holder.setTextColor(R.id.text, R.color.text_color9);
                                                }
                                            } else {
                                      /*  gif.setVisibility(View.GONE);
                                        holder.setText(R.id.text, "直播未开始");
                                        holder.setTextColor(R.id.text, R.color.text_color9);*/
                                                holder.setVisible(R.id.subscribe_ly, true);
                                                holder.setVisible(R.id.is_liveing, false);
                                                if (mNow.getIs_buy().equals("1")) {
                                                    holder.setText(R.id.live_subscribe, "已预约");
                                                    live_subscribe.setBackgroundResource(R.drawable.gay_shape15);
                                                    live_subscribe.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color6));
                                                } else {
                                                    holder.setText(R.id.class_price, "立即预约");
                                                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                                                    holder.setBackgroundRes(R.id.class_price, R.drawable.yellowall);
                                                }

                                                Log.e("***getStart_times", mNow.getStart_times() + "******");
                                                mSubscribeTime.setEndTime(Long.valueOf(mNow.getStart_times()));
                                                mSubscribeTime.setClockListener(new CustomDigitalClock.ClockListener() {

                                                    @Override
                                                    public void timeEnd() {
                                                        EventBus.getDefault().post("倒计时结束");
//                                                openClass();
//                                                notifyDataSetChanged();
//                                                holder.setVisible(R.id.subscribe_ly,false);
//                                                holder.setVisible(R.id.is_liveing,true);
//                                                gif.setVisibility(View.VISIBLE);
//                                                holder.setText(R.id.text, "直播中");
//                                                holder.setVisible(R.id.class_num,true);
//                                                holder.setVisible(R.id.class_price,false);
//                                                holder.setText(R.id.class_num,mNow.getSale_num()+"人在观看");
//                                                holder.setTextColor(R.id.text, R.color.purple4);
//                                                holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        _id = mNow.getId();
//                                                        intent = new Intent();
//                                                        if (System.currentTimeMillis()-timeNow>2000){
//                                                            if(result.getData().getNow().get(position).getIs_buy().equals("0")){
//                                                                intent.setClass(getActivity(), OpenClassDetailActivity.class);
//                                                                intent.putExtra("_id", _id);
//                                                                intent.putExtra("recent","now");
//                                                                intent.putExtra("chat_room_id",mNow.getLive_info().getChat_room_id());
//                                                                intent.putExtra("token",mNow.getLive_info().getToken());
//                                                                intent.putExtra("live_url",mNow.getLive_info().getPush_url());
//                                                                startActivity(intent);
//                                                            }else {
//                                                                onLine(mNow.getLive_info().getApp_name(),mNow);
//                                                       /* intent.setClass(getActivity(), LiveActivity.class);
////                                                        intent.putExtra("url", result.getData().getNow().get(position).getLive_info().getPush_url());
//                                                        intent.putExtra("chat_room_id",mNow.getLive_info().getChat_room_id());
//                                                        intent.putExtra("token",mNow.getLive_info().getToken());
//                                                        intent.putExtra("live_url",mNow.getLive_info().getPush_url());
//                                                        intent.putExtra("_id", _id);*/
//                                                            }
//                                                            timeNow=System.currentTimeMillis();
//                                                        }
//
//
//
//                                                    }
//                                                });
                                                    }

                                                    @Override
                                                    public void remainFiveMinutes() {

                                                    }
                                                });

                                                holder.setOnClickListener(R.id.live_subscribe, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent1 = new Intent();
                                                        _id = mNow.getId();
                                                        if (mNow.getIs_buy().equals("1")) {
                                                            intent1.setClass(getActivity(), OpenClassDetailActivity.class);
                                                            intent1.putExtra("_id", _id);
                                                            intent1.putExtra("recent", "now");
                                                            intent1.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                            intent1.putExtra("token", mNow.getLive_info().getToken());
                                                            intent1.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                            intent1.putExtra("name", mNow.getLive_info().getName());
                                                            startActivity(intent1);
                                                        } else {
                                                            intent1.setClass(getActivity(), OpenClassDetailActivity.class);
                                                            intent1.putExtra("_id", _id);
                                                            intent1.putExtra("recent", "now");
                                                            intent1.putExtra("chat_room_id", mNow.getLive_info().getChat_room_id());
                                                            intent1.putExtra("token", mNow.getLive_info().getToken());
                                                            intent1.putExtra("live_url", mNow.getLive_info().getPush_url());
                                                            startActivity(intent1);
                                                        }
                                                    }
                                                });
                                            }


                                        }
                                    });
                                } else {
                                    mNowLiveLy.setVisibility(View.GONE);
                                }
                            }
                        }


                        if (result.getData().getRecent() != null && result.getData().getRecent().size() > 0) {
                            Log.e("*********getRecent", "2222222222222");
                            if (page == 1) {
                                mRecents.clear();
                                mRecents = result.getData().getRecent();
                            } else {
                                if (null == mRecents) {
                                    mRecents = result.getData().getRecent();
                                } else {
                                    mRecents.addAll(result.getData().getRecent());
                                }
                            }
                            GloableConstant.getInstance().setmRecents(mRecents);
                            mNearLiveLy.setVisibility(View.VISIBLE);
                            mRecent.setVisibility(View.VISIBLE);
                            mRecentText.setVisibility(View.VISIBLE);
                            mRecent.setLayoutManager(new LinearLayoutManager(getActivity()));
                            mRecent.setAdapter(new CommonAdapter<OpenClassResponse.mRecent>(getActivity(), R.layout.item_open_class, mRecents) {

                                @Override
                                protected void convert(ViewHolder holder, final OpenClassResponse.mRecent mRecent, int position) {
                                    ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                                    Glide.with(getActivity()).load(mRecent.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                    TextView mNum = (TextView) holder.getConvertView().findViewById(R.id.course_num);
                                    mNum.setVisibility(View.GONE);
                                    holder.setText(R.id.date, mRecent.getDate_y2());
                                    holder.setText(R.id.course_name, mRecent.getName());
                                    holder.setText(R.id.course_data, mRecent.getDate_d());
                                    TextView textView = (TextView) holder.getConvertView().findViewById(R.id.sell_num);
                                    if (!mRecent.getPrice().equals("0.00")) {
                                        textView.setText("￥" + mRecent.getPrice());
                                        textView.setTextColor(getResources().getColor(R.color.orange_color));
                                    } else {
                                        textView.setText("免费");
                                        textView.setTextColor(getResources().getColor(R.color.green));
                                    }
                                    LinearLayout mStatus = (LinearLayout) holder.getConvertView().findViewById(R.id.status);
                                    TextView mText = (TextView) holder.getConvertView().findViewById(R.id.status_bar);


                                    if (mRecent.getIs_buy().equals("0")) {
                                        holder.setText(R.id.status_bar, "立即预约");
                                        mText.setTextColor(getResources().getColor(R.color.white));
                                        mStatus.setBackgroundResource(R.drawable.yellowall);
                                    } else {
                                        holder.setText(R.id.status_bar, "已预约");
                                        mText.setTextColor(getResources().getColor(R.color.text_color9));
                                        mStatus.setBackgroundResource(R.drawable.gay_shape15);
                                    }
                                    holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            _id = mRecent.getId();
                                            intent = new Intent();
                                            intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                            intent.putExtra("_id", _id);
                                            intent.putExtra("recent", "recent");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });

                        } else {
                            if (page == 1) {
                                mNearLiveLy.setVisibility(View.GONE);
                            } else {
                                if (GloableConstant.getInstance().getmRecents() != null
                                        && GloableConstant.getInstance().getmRecents().size() > 0) {
                                    mRecents = GloableConstant.getInstance().getmRecents();
                                    mNearLiveLy.setVisibility(View.VISIBLE);
                                    mRecent.setVisibility(View.VISIBLE);
                                    mRecentText.setVisibility(View.VISIBLE);
                                    Log.e("*********getRecent11111", result.getData().getRecent().size() + "/////");
                                    mRecent.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    mRecent.setAdapter(new CommonAdapter<OpenClassResponse.mRecent>(getActivity(), R.layout.item_open_class, mRecents) {

                                        @Override
                                        protected void convert(ViewHolder holder, final OpenClassResponse.mRecent mRecent, int position) {
                                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                                            Glide.with(getActivity()).load(mRecent.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                            TextView mNum = (TextView) holder.getConvertView().findViewById(R.id.course_num);
                                            mNum.setVisibility(View.GONE);
                                            holder.setText(R.id.date, mRecent.getDate_y2());
                                            holder.setText(R.id.course_name, mRecent.getName());
                                            holder.setText(R.id.course_data, mRecent.getDate_d());
                                            TextView textView = (TextView) holder.getConvertView().findViewById(R.id.sell_num);
                                            if (!mRecent.getPrice().equals("0.00")) {
                                                textView.setText("￥" + mRecent.getPrice());
                                                textView.setTextColor(getResources().getColor(R.color.orange_color));
                                            } else {
                                                textView.setText("免费");
                                                textView.setTextColor(getResources().getColor(R.color.green));
                                            }
                                            LinearLayout mStatus = (LinearLayout) holder.getConvertView().findViewById(R.id.status);
                                            TextView mText = (TextView) holder.getConvertView().findViewById(R.id.status_bar);


                                            if (mRecent.getIs_buy().equals("0")) {
                                                holder.setText(R.id.status_bar, "立即预约");
                                                mText.setTextColor(getResources().getColor(R.color.white));
                                                mStatus.setBackgroundResource(R.drawable.yellowall);
                                            } else {
                                                holder.setText(R.id.status_bar, "已预约");
                                                mText.setTextColor(getResources().getColor(R.color.text_color9));
                                                mStatus.setBackgroundResource(R.drawable.gay_shape15);
                                            }
                                            holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    _id = mRecent.getId();
                                                    intent = new Intent();
                                                    intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                    intent.putExtra("_id", _id);
                                                    intent.putExtra("recent", "recent");
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    mNearLiveLy.setVisibility(View.GONE);
                                }
                            }

                        }
                        Log.e("*********size", result.getData().getHistory().size() + "");
                        if (result.getData().getHistory() != null && result.getData().getHistory().size() > 0) {
                            mHistoryLiveLy.setVisibility(View.VISIBLE);
                            mHistory.setVisibility(View.VISIBLE);
                            mHistoryText.setVisibility(View.VISIBLE);
                            if (page == 1) {
                                mHistorys.clear();
                                mHistorys = result.getData().getHistory();
                            } else {
                                if (null == mHistorys) {
                                    mHistorys = result.getData().getHistory();
                                } else {
                                    mHistorys.addAll(result.getData().getHistory());
                                }
                            }
//                            mHistory.setHasFixedSize(true);
//                            mHistory.setNestedScrollingEnabled(false);
                            mHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
                            Log.e("*********mHistory", mHistorys.size() + "///////");
                            mHistory.setAdapter(new CommonAdapter<OpenClassResponse.mHistory>(getActivity(), R.layout.item_open_class, mHistorys) {

                                @Override
                                protected void convert(ViewHolder holder, final OpenClassResponse.mHistory mHistory, int position) {
                                    ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                                    Glide.with(getActivity()).load(mHistory.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                    TextView mNum = (TextView) holder.getConvertView().findViewById(R.id.course_num);
                                    mNum.setVisibility(View.VISIBLE);
                                    mNum.setText(mHistory.getSale_num());
                                    TextView mDate = (TextView) holder.getConvertView().findViewById(R.id.date);
                                    mDate.setVisibility(View.GONE);
                                    holder.setText(R.id.course_name, mHistory.getName());
                                    holder.setText(R.id.course_data, "人已观看");
                                    TextView textView = (TextView) holder.getConvertView().findViewById(R.id.sell_num);
                                    if (!mHistory.getPrice().equals("0.00")) {
                                        textView.setText("￥" + mHistory.getPrice());
                                        textView.setTextColor(getResources().getColor(R.color.orange_color));
                                    } else {
                                        textView.setText("免费");
                                        textView.setTextColor(getResources().getColor(R.color.green));
                                    }
                                    LinearLayout mStatus = (LinearLayout) holder.getConvertView().findViewById(R.id.status);
                                    TextView mText = (TextView) holder.getConvertView().findViewById(R.id.status_bar);
                                    if (mHistory.getIs_buy().equals("0")) {
                                        holder.setText(R.id.status_bar, "立即预约");
                                        mText.setTextColor(getResources().getColor(R.color.white));
                                        mStatus.setBackgroundResource(R.drawable.yellowall);
                                    } else {
                                        holder.setText(R.id.status_bar, "已预约");
                                        mText.setTextColor(getResources().getColor(R.color.text_color9));
                                        mStatus.setBackgroundResource(R.drawable.gay_shape15);
                                    }

                                    holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            _id = mHistory.getId();
                                            intent = new Intent();
                                            intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                            intent.putExtra("_id", _id);
                                            intent.putExtra("lb_url", mHistory.getLb_url());
                                            intent.putExtra("recent", "history");
                                            startActivity(intent);

                                        }
                                    });
                                }
                            });
                        } else {
//                            mHistory.setVisibility(View.GONE);
//                            mHistoryText.setVisibility(View.GONE);
                            mHistoryLiveLy.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("********page", page + "////////////");
                        if (page > 1) {
                            ToastUtil.showShortToast("没有更多数据");
                        } else {
//                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }

                    }
                } else if (result.getStatus_code().equals("203")) {
                    GloableConstant.getInstance().stopProgressDialog();
                    mPullToRefresh.finishRefresh();
                    mPullToRefresh.finishLoadMore();
//                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                Log.e("********ex", ex.getMessage() + "////////");
            }
        });
    }

    private void onLine(String appname, final OpenClassResponse.mNow Now) {
        Map<String, String> map = new HashMap<>();
        map.put("app_name", appname);
        Log.e("OP*********OP", appname);
        Xutil.GET(ApiUrl.ONLINE, map, new MyCallBack<OnLineResponse>() {
            @Override
            public void onSuccess(OnLineResponse result) {
                super.onSuccess(result);
                Log.e("OP*********OP", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getIs_online() != null) {
                        // 备注：0直播不在线 1正在直播
                        if (result.getData().getIs_online().equals("0")) {
                            ToastUtil.showShortToast("直播不在线");
                        } else {
                            intent = new Intent();
                            intent.setClass(getActivity(), LiveActivity.class);
//                            ToastUtil.showShortToast("88888888");
//                                                        intent.putExtra("url", result.getData().getNow().get(position).getLive_info().getPush_url());
                            intent.putExtra("chat_room_id", Now.getLive_info().getChat_room_id());
                            intent.putExtra("token", Now.getLive_info().getToken());
                            intent.putExtra("live_url", Now.getLive_info().getPush_url());
                            intent.putExtra("_id", _id);
                            intent.putExtra("name", Now.getLive_info().getName());
                            startActivity(intent);
                        }
                    }
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("*******mapex", ex.getMessage() + "///////");
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }

}
