package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.NewClassListActivity;
import com.jiaoshizige.teacherexam.activity.NewClassListtwoActivity;
import com.jiaoshizige.teacherexam.adapter.BookTypeAdapter;
import com.jiaoshizige.teacherexam.adapter.NewPrivateCourseFragmentTwoAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CatBookResponse;
import com.jiaoshizige.teacherexam.model.CourseCatResponse;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.NewCatBookResponse;
import com.jiaoshizige.teacherexam.model.NewCourseResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ScreenUtil;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/10.
 */

public class NewPrivateCourseFragmentTwo extends MBaseFragment {
    @BindView(R.id.course_recycler)
    ListView mCourseRecycler;
    @BindView(R.id.type)
    RadioButton mType;
    @BindView(R.id.sort)
    RadioButton mSort;
    @BindView(R.id.price)
    RadioButton mMainPrice;
    @BindView(R.id.screen_ly)
    LinearLayout mScreenLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String mPageSize = "10";
    private int mPage = 1;
    private String type = "11,12";
    private String order;
    private String sort;
    private String nameTag;
    private String mCatId;
    private Context mContext;
    private int mHideFlag = 0;
    private String onResumeNameType;
    private NewPrivateCourseFragmentTwoAdapter mAdapter;
    private View mHeadView;
    private TextView mTitle;
    private TextView mContent;
    private ImageView mImage;
    private TextView mPrice;
    private TextView mSaleNum;
    private TextView mCourseType;
    private TextView mClass;
    private TextView mNum;
    private List<NewCourseResponse.mZero> mZero;

    List<CourseCatResponse.mData> mTypeList;
    List<NoteaWorkChapterResponse.mData> mSortList;
    AllPopupwindow mCourseTypePopupWindow;
    private int priceFlag = 1;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private String flag = "0";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_fragment_two, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GloableConstant.getInstance().getSearchCourseTag() != null) {
            nameTag = GloableConstant.getInstance().getSearchCourseTag();
            Log.d("********nameTag", nameTag + "/////");
            if (onResumeNameType == null) {
                onResumeNameType = "课程";
            } else {
                Log.e("opop", "jkjkj2222");
            }
            if (mHideFlag == 0 && !onResumeNameType.equals(nameTag)) {
                mPage = 1;
                getCourse(type, nameTag, order, sort);
            }


        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            Log.e("OPOP", "hide");
            mHideFlag = 1;
        } else {
            Log.e("OPOP", "show");
            mHideFlag = 0;
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mTypeList = new ArrayList<>();
        mContext = getActivity();
        getCourseType();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mZero = new ArrayList<>();
        mHeadView = View.inflate(getActivity(), R.layout.course_header_layout, null);
        mTitle = (TextView) mHeadView.findViewById(R.id.class_name);
        mContent = (TextView) mHeadView.findViewById(R.id.assistant_name);
        mCourseType = (TextView) mHeadView.findViewById(R.id.course_type);
        mImage = (ImageView) mHeadView.findViewById(R.id.image);
        mPrice = (TextView) mHeadView.findViewById(R.id.market_price);
        mClass = (TextView) mHeadView.findViewById(R.id.course);
        mNum = (TextView) mHeadView.findViewById(R.id.learn_num);
        mCourseRecycler.addHeaderView(mHeadView);
        mAdapter = new NewPrivateCourseFragmentTwoAdapter(getActivity());
        mCourseRecycler.setAdapter(mAdapter);

        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                getCourse(type, nameTag, order, sort);
            }

            @Override
            public void loadMore() {
                mPage++;
                getCourse(type, nameTag, order, sort);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                getCourse(type, nameTag, order, sort);
            }
        });
        mPage = 1;
        getCourse(type, nameTag, order, sort);
        mCourseRecycler.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mCourseRecycler.getLastVisiblePosition() == (mCourseRecycler
                                .getCount() - 1)) {
//                            topBtn.setVisibility(View.VISIBLE);
                        }
                        // 判断滚动到顶部
                        if (mCourseRecycler.getFirstVisiblePosition() == 0) {
//                            topBtn.setVisibility(View.GONE);
                        }

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(mCourseRecycler) >= ScreenUtil
                        .getScreenHeight(getActivity())) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
//                        topBtn.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
//                        topBtn.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
        mCourseRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag.equals("0")) {
                    if (position == 0) {
                        NewCourseResponse.mZero mZero = (NewCourseResponse.mZero) parent.getAdapter().getItem(position);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), NewClassListtwoActivity.class);
                        intent.putExtra("data", mZero);
                        startActivity(intent);
                    } else {
                        NewCourseResponse.mNozero mNozero = (NewCourseResponse.mNozero) parent.getAdapter().getItem(position);
                        Intent intent = new Intent();
                        //1班级2课程
                        intent.setClass(getActivity(), CoursesDetialActivity.class);
                        intent.putExtra("course_id", mNozero.getType_id());
                        intent.putExtra("type", mNozero.getType());   //1班级2课程
                        intent.putExtra("is_buy", mNozero.getIs_buy());
                        Log.d("///////2222222isBuy", mNozero.getIs_buy() + "**********");
                        startActivity(intent);
                    }
                } else {
                    NewCourseResponse.mNozero mNozero = (NewCourseResponse.mNozero) parent.getAdapter().getItem(position);
                    Intent intent = new Intent();
                    //1班级2课程
                    intent.setClass(getActivity(), CoursesDetialActivity.class);
                    intent.putExtra("course_id", mNozero.getType_id());
                    intent.putExtra("type", mNozero.getType());   //1班级2课程
                    intent.putExtra("is_buy", mNozero.getIs_buy());
                    Log.d("///////2222222isBuy", mNozero.getIs_buy() + "**********");
                    startActivity(intent);
                }
            }
        });

    }


    @Subscribe()
    public void onMessageEvent(MessageEvent event) {
        Log.e("+++++++++++++++", "zhixiong" + event.getMessage());
        if (event.getMessage().equals("课程")) {
            mCourseRecycler.removeHeaderView(mHeadView);
            mAdapter.notifyDataSetChanged();
            mHeadView = View.inflate(getActivity(), R.layout.course_header_layout, null);
            mTitle = (TextView) mHeadView.findViewById(R.id.class_name);
            mContent = (TextView) mHeadView.findViewById(R.id.assistant_name);
            mCourseType = (TextView) mHeadView.findViewById(R.id.course_type);
            mImage = (ImageView) mHeadView.findViewById(R.id.image);
            mPrice = (TextView) mHeadView.findViewById(R.id.market_price);
            mClass = (TextView) mHeadView.findViewById(R.id.course);
            mNum = (TextView) mHeadView.findViewById(R.id.learn_num);
            mCourseRecycler.addHeaderView(mHeadView);
            mAdapter = new NewPrivateCourseFragmentTwoAdapter(getActivity());
            mCourseRecycler.setAdapter(mAdapter);
            mPage = 1;
            nameTag = "";
            GloableConstant.getInstance().setSearchCourseTag(null);
            GloableConstant.getInstance().setMessage("0");
            getCourse(type, nameTag, order, sort);
        } else if (event.getMessage().equals("更换考试类型")) {

            mPage = 1;
            getCourse(type, nameTag, order, sort);
        }
    }


    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            mCourseRecycler.smoothScrollToPosition(pos);
        } else {
            mCourseRecycler.setSelection(pos);
        }
    }

    public void setmSortChecked() {
//        mSort.setChecked(false);
        mMainPrice.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mMainPrice.setCompoundDrawables(null, null, drawable, null);
    }

    public void setmPriceChecked() {
//        mType.setChecked(false);
        GloableConstant.getInstance().setSort(null);
        mSort.setText("销量");
        mSort.setChecked(false);
    }

    @OnClick({R.id.type, R.id.sort, R.id.price})
    public void onClick(View v) {
        if (v.getId() == R.id.type) {
//            mCourseTypePopupWindow.setOnDismissListener(new NewPrivateCourseFragmentTwo.poponDismissListenermDepartment());
//            mCourseTypePopupWindow.showFilterPopup(mScreenLy);
            showPopWindow(mBookallFlag);
        } else if (v.getId() == R.id.sort) {
            mPage = 1;
            getCourse(type, nameTag, "sale_num", "desc");
            setmSortChecked();
        } else if (v.getId() == R.id.price) {

            Log.d("*******priceFlag", priceFlag + "////////");
            if (priceFlag == 1) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mMainPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 0;
                setmPriceChecked();
                mPage = 1;
                getCourse(type, nameTag, "price", "desc");

            } else {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mMainPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 1;
                setmPriceChecked();
                mPage = 1;
                getCourse(type, nameTag, "price", "asc");
            }
        }

    }

    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if (GloableConstant.getInstance().getSort() != null) {
                mSort.setChecked(true);
                mSort.setText(GloableConstant.getInstance().getSort());
//                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down_pre);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mSort.setCompoundDrawables(null,null,drawable,null);
                setmSortChecked();

            } else {
                mSort.setChecked(false);
                mSort.setText("销量");
//                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mSort.setCompoundDrawables(null,null,drawable,null);
                setmSortChecked();
            }

            if (GloableConstant.getInstance().getCourseTypeName() != null) {
                mType.setChecked(true);
                mType.setText(GloableConstant.getInstance().getCourseTypeName());
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_down_pre);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null, null, drawable, null);
                type = GloableConstant.getInstance().getCoursrTypeId();
                if (GloableConstant.getInstance().getSearchCourseTag() != null) {
                    nameTag = "";
                    GloableConstant.getInstance().setSearchCourseTag(null);
                    mCourseRecycler.addHeaderView(mHeadView);
                    mAdapter = new NewPrivateCourseFragmentTwoAdapter(getActivity());
                    mCourseRecycler.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                mPage = 1;
                getCourse(type, nameTag, order, sort);
//                if(GloableConstant.getInstance().getType().equals("全部")){
//                    type = "11,12";
//                    getCourse(type,"",order,sort);
//                }else if(GloableConstant.getInstance().getType().equals("热门推荐课")){
//                    type = "11";
//                    getCourse(type,"",order,sort);
////                    requestCourseList(type,sortField,sortOrder,nameTag);
//                }else if(GloableConstant.getInstance().getType().equals("VIP通关班")){
//                    type = "12";
//                    getCourse(type,"",order,sort);
////                    requestCourseList(type,sortField,sortOrder,nameTag);
//                }
            } else {
                mType.setChecked(false);
                mType.setText("全部");
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null, null, drawable, null);
            }
            for (int i = 0; i < mTypeList.size(); i++) {
                if (mType.getText().toString().equals(mTypeList.get(i).getCat_name())) {
                    type = mTypeList.get(i).getCat_id();
                }
            }


            setBackgroundAlpha(1f);
        }
    }
    private PopupWindow mPop;
    private View mContentView;
    BookTypeAdapter mtypeAdapter;
    private String catName;
    ListView mListView;
    private List<NewCatBookResponse.mData> mBookallFlag = new ArrayList<>();

    private void showPopWindow(List<NewCatBookResponse.mData> data) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mType.setCompoundDrawables(null, null, drawable, null);
        mContentView = inflater.inflate(R.layout.all_popupwindow_layout, null);
        mListView = (ListView) mContentView.findViewById(R.id.pop_list);
        mPop = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView mPopDismess = (TextView) mContentView.findViewById(R.id.pop_dismiss);
        mPopDismess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
        mPop.setFocusable(true);// 取得焦点
        mPop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPop.setOutsideTouchable(true);
        //设置可以点击
        mPop.setTouchable(true);
        mtypeAdapter = new BookTypeAdapter(mContext);
        mtypeAdapter.setmList(data);
        mListView.setAdapter(mtypeAdapter);
        Log.d("*******position", GloableConstant.getInstance().getBookPosition() + "++++++++++");
        if (GloableConstant.getInstance().getCoursePosition() != null) {
            mtypeAdapter.setSelection(Integer.valueOf(GloableConstant.getInstance().getCoursePosition()));
            mtypeAdapter.notifyDataSetChanged();
        }
        mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = mScreenLy.getWidth() / 2 - mContentView.getMeasuredWidth() / 2;
        mPop.showAsDropDown(mScreenLy, xOffset, 0);
        mPop.setOnDismissListener(new NewPrivateCourseFragmentTwo.poponDismissListenermDepartment());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mtypeAdapter.setSelection(position);
                mtypeAdapter.notifyDataSetChanged();
                GloableConstant.getInstance().setCoursePosition(String.valueOf(position));
                NewCatBookResponse.mData mData = (NewCatBookResponse.mData) parent.getAdapter().getItem(position);
                mCatId = mData.getCat_id();
                catName = mData.getCat_name();
                mType.setText(catName);
                Log.e("********** ", mCatId + "*******catName" + catName);
                GloableConstant.getInstance().setCoursrTypeId(mCatId);
                GloableConstant.getInstance().setCourseTypeName(catName);
//                requestBookList(type,sort,order,nameTag,mCatId);
                if (mPop.isShowing()) {
                    mPop.dismiss();
                }
            }
        });
    }


    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    /**
     * 课程筛选
     */
    private void getCourseType() {
        Map<String, Object> map = new HashMap<>();
        Xutil.Post(ApiUrl.COURSETYPECAT, map, new MyCallBack<NewCatBookResponse>() {
            @Override
            public void onSuccess(NewCatBookResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    mBookallFlag = result.getData();
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }


    private int currIndex = 0;

    private void getCourse(String type_id, String course_name, String order, String sort) {
        Map<String, String> map = new HashMap<>();
        map.put("type_id", type_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order", order);
        map.put("sort", sort);
        map.put("course_name", course_name);
        map.put("page", mPage + "");
        map.put("page_size", mPageSize);
        onResumeNameType = course_name;
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Log.d("********map", map.toString());
        Xutil.GET(ApiUrl.NEWCOURSE, map, new MyCallBack<NewCourseResponse>() {
            @Override
            public void onSuccess(NewCourseResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    setListViewPos(0);
                    Log.d("**********size", GloableConstant.getInstance().getSearchCourseTag() + "/////");
                    if (GloableConstant.getInstance().getSearchCourseTag() == null) {
                        Log.d("**********size", "/zhixingam////");
                        if (result.getData().getZero().size() > 0 && result.getData().getZero() != null) {
                            mZero = result.getData().getZero();
                            mTitle.setText(result.getData().getZero().get(0).getName());
                            mContent.setText(result.getData().getZero().get(0).getDesc());
                            mCourseType.setText(result.getData().getZero().get(0).getBq());
                            mPrice.setText(result.getData().getZero().get(0).getPrice());
                            mClass.setText("(共" + result.getData().getZero().get(0).getKeshi() + "个课)");
                            mNum.setText(result.getData().getZero().get(0).getSale_num());
                            Glide.with(getActivity()).load(result.getData().getZero().get(0).getBq_img())
                                    .apply(GloableConstant.getInstance().getDefaultOptionssmall())
                                    .into(mImage);
                        } else {
                            mCourseRecycler.removeHeaderView(mHeadView);
                            mCourseRecycler.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        Log.d("**********size", result.getData().getNozero().size() + "/////");
                        if (result.getData().getNozero().size() > 0 && result.getData().getNozero() != null) {
                            if (mPage == 1) {
                                mAdapter.clear();
                                currIndex = result.getData().getNozero().size();
                                mAdapter.setmList(result.getData().getNozero());
                            } else {
                                //  mAdapter.clear();
                                mAdapter.addAll(result.getData().getNozero());
                                mCourseRecycler.setSelection(currIndex);
                                currIndex += result.getData().getNozero().size();
                            }

                        } else {
                            if (mPage > 1) {
                                mCourseRecycler.setSelection(currIndex);
                                ToastUtil.showShortToast("没有更多数据了");
                            } else {
                                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                            }
                        }
                        flag = "0";
                    } else {//搜索
                        flag = "1";
                        mCourseRecycler.removeHeaderView(mHeadView);
                        mCourseRecycler.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        if (result.getData().getNozero().size() > 0 && result.getData().getNozero() != null) {
                            if (mPage == 1) {
                                mAdapter.clear();
                                currIndex = result.getData().getNozero().size();
                                mAdapter.setmList(result.getData().getNozero());
                            } else {
                                // mAdapter.clear();
                                mAdapter.addAll(result.getData().getNozero());
                                mCourseRecycler.setSelection(currIndex);
                                currIndex += result.getData().getNozero().size();
                            }

                        } else {
                            if (mPage > 1) {
                                // mPage--;
                                mCourseRecycler.setSelection(currIndex);
                                ToastUtil.showShortToast("没有更多数据了");
                            } else {
                                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                            }
                        }


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
