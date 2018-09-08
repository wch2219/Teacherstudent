package com.jiaoshizige.teacherexam.yy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.BookDetailActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MPBaseFragment;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BookListResponse;
import com.jiaoshizige.teacherexam.utils.AppLog;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.SelfDialog;
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
 * Created by Administrator on 2017/a1/7 0007.
 * 图书收藏
 */

public class BooksFragment extends MPBaseFragment {
    @BindView(R.id.class_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String user_id;
    private String pagesize = "10";
    private int mPage = 1;
    private SelfDialog selfDialog;
    private String book_id;
    private Intent intent;
    View view;

    private CommRecyclerViewAdapter<BookListResponse.mData> mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.class_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        BookCollect();
        MobclickAgent.onPageStart("图书收藏");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图书收藏");
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
        lp.setMargins(0, ToolUtils.dip2px(getActivity(), 10), 0, 0);

        EventBus.getDefault().register(this);
        mRecyclerView.setLayoutParams(lp);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                BookCollect();
            }

            @Override
            public void loadMore() {
                mPage++;
                BookCollect();

            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                BookCollect();
            }
        });
        BookCollect();//访问网络请求数据
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBus(String num) {
        if (num.equals("图书取消收藏")){
            BookCollect();
        }
    }

    private void BookCollect() {
        Map<String, Object> map = new HashMap<>();
//    map.put("user_id",1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "2");
        map.put("page", mPage);
        map.put("page_size", pagesize);
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.COLLECT, map, new MyCallBack<BookListResponse>() {
            @Override
            public void onSuccess(final BookListResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        if (mPage == 1) {
                            getAdapter().updateData(result.getData());
                        } else {
                            getAdapter().appendData(result.getData());
                        }
                    } else {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    if (mPage > 1) {
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });


    }

    /**
     * 取消收藏
     */
    private void delCollection(final BookListResponse.mData item) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "2");
        map.put("goods_id", item.getBook_id());
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.DELCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    getAdapter().removeData(item);
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private CommRecyclerViewAdapter<BookListResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<BookListResponse.mData>(getActivity(), R.layout.item_book_fragment_layout, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final BookListResponse.mData s, final int position) {
                    holder.setVisible(R.id.view, false);
                    holder.setText(R.id.course_name, s.getBook_name());
                    holder.setText(R.id.course_price, "￥" + s.getPrice());
                    holder.setText(R.id.sell_num, s.getSale_num() + "人已购买");
                    ImageView mBookCover = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                    Glide.with(getActivity()).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mBookCover);
                    holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            book_id = s.getBook_id();
                            Log.e("**********bookid", book_id);
                            intent = new Intent(getActivity(), BookDetailActivity.class);
                            intent.putExtra("book_id", book_id);
                            startActivity(intent);
                        }
                    });
                    holder.setOnLongClickListener(R.id.item_book, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AppLog.instance().d("position:" + position);
                            selfDialog = new SelfDialog(getActivity());
                            selfDialog.setTitle("是否取消收藏");
                            selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    delCollection(s);
                                    selfDialog.dismiss();
                                }
                            });
                            selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                                @Override
                                public void onNoClick() {
                                    ToastUtil.showShortToast("取消");
                                    selfDialog.dismiss();
                                }
                            });
                            selfDialog.show();
                            return false;
                        }
                    });
                }
            };
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}