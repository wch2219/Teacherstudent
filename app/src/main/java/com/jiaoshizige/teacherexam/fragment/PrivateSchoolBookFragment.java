package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.BookDetailActivity;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.adapter.BookTypeAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BookListResponse;
import com.jiaoshizige.teacherexam.model.CatBookResponse;
import com.jiaoshizige.teacherexam.model.NewCatBookResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/17.
 * 私塾图书
 */

public class PrivateSchoolBookFragment extends MBaseFragment {
    @BindView(R.id.course_recycler)
    RecyclerView mBookRecycler;
    @BindView(R.id.type)
    RadioButton mType;
    @BindView(R.id.sort)
    RadioButton mSort;
    @BindView(R.id.price)
    RadioButton mPrice;
    @BindView(R.id.screen_ly)
    LinearLayout mScreenLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    //    BookClassifyPopupWindow mBookTypePopupWindow;
    AllPopupwindow mBookSortPopupWindow;
    private int priceFlag = 1;
    private String mPageNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    private String type;
    private String order;
    private String sort;
    private String nameTag;
    private String mCatId;
    private List<NoteaWorkChapterResponse.mData> mSortList;
    private List<CatBookResponse.mChild> mList;
    private Context mContext;
    private int mHideFlag = 0;
    private String onResumeNameType;
    private List<NewCatBookResponse.mData> mBookallFlag = new ArrayList<>();

    private CommRecyclerViewAdapter<BookListResponse.mData> mRCVBookAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GloableConstant.getInstance().getInstance().getSearchBookTag() != null) {
            nameTag = GloableConstant.getInstance().getSearchBookTag();
            if (onResumeNameType == null) {
                onResumeNameType = "新";//随便赋个值  对比两个树不一样时候请求列表  减少列表刷新次数
            } else {
                Log.e("opop", "jkjkj2222");
            }
            if (mHideFlag == 0 && !onResumeNameType.equals(nameTag)) {
                mPage = 1;
                mPageNum = "1";
                requestBookList(type, sort, order, nameTag, mCatId);
            }
            Log.e("book", nameTag);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.e("OPOP", "hide");
            mHideFlag = 1;
        } else {
            Log.e("OPOP", "show");
            mHideFlag = 0;
        }
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mContext = getActivity();
        NoteaWorkChapterResponse.mData sdata2 = new NoteaWorkChapterResponse.mData();
        sdata2.setTitle("最新");
        NoteaWorkChapterResponse.mData sdata3 = new NoteaWorkChapterResponse.mData();
        sdata3.setTitle("销量升序");
        NoteaWorkChapterResponse.mData sdata4 = new NoteaWorkChapterResponse.mData();
        sdata4.setTitle("销量降序");
        mSortList = new ArrayList<>();
        mSortList.add(sdata2);
        mSortList.add(sdata3);
        mSortList.add(sdata4);
//        mBookTypePopupWindow = new BookClassifyPopupWindow(getActivity(),mList);
        mBookSortPopupWindow = new AllPopupwindow(getActivity(), mSortList, "booksort");
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                mPage = 1;
                requestBookList(type, sort, order, nameTag, mCatId);
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestBookList(type, sort, order, nameTag, mCatId);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPageNum = "1";
                requestBookList(type, sort, order, nameTag, mCatId);
            }
        });
        checkBook();
        requestBookList(type, sort, order, nameTag, mCatId);

    }

    public void setmTypeChecked() {
        mType.setChecked(false);
        mPrice.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPrice.setCompoundDrawables(null, null, drawable, null);
    }

    public void setmSortChecked() {
        mSort.setChecked(false);
        mPrice.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPrice.setCompoundDrawables(null, null, drawable, null);
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
//            mBookTypePopupWindow.setOnDismissListener(new poponDismissListenermDepartment());
//            mBookTypePopupWindow.showFilterPopup(mScreenLy);
            showPopWindow(mBookallFlag);
//            ToastUtil.showShortToast("1111111111");
        } else if (v.getId() == R.id.sort) {
//            mBookSortPopupWindow.setOnDismissListener(new poponDismissListenermDepartment());
//            mBookSortPopupWindow.showFilterPopup(mScreenLy);
            sort = "desc";
            requestBookList(type, sort, order, nameTag, mCatId);
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mPrice.setCompoundDrawables(null, null, drawable, null);
            mPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color3));
        } else if (v.getId() == R.id.price) {
            mPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.purple4));
            mSort.setText("销量");
//            Drawable drawable1= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down);
//            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//            mSort.setCompoundDrawables(null,null,drawable1,null);
            if (priceFlag == 1) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 0;
                setmPriceChecked();
                mPage = 1;
                mPageNum = "1";
                sort = "asc";
                order = "price";
                requestBookList(type, sort, order, nameTag, mCatId);
            } else {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 1;
                setmPriceChecked();
                mPage = 1;
                mPageNum = "1";
                sort = "desc";
                order = "price";
                requestBookList(type, sort, order, nameTag, mCatId);
            }
        }
    }


    class dialogDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if (GloableConstant.getInstance().getCatId() != null) {
                mType.setChecked(true);
                mType.setText(GloableConstant.getInstance().getTypeName());
                mCatId = GloableConstant.getInstance().getCatId();
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_down_pre);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null, null, drawable, null);
                Log.e("*******mCatId", mCatId + "*********" + GloableConstant.getInstance().getTypeName());
                mPage = 1;
                mPageNum = "1";
                GloableConstant.getInstance().setSearchBookTag("");
                nameTag = "";
                requestBookList(mCatId, sort, order, nameTag, mCatId);
            } else {
                mType.setChecked(false);
                mType.setText("类型");
                mCatId = GloableConstant.getInstance().getCatId();//? if 判断为null 这里又赋值 null 是怎么个意思呢
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null, null, drawable, null);
            }
            setBackgroundAlpha(1f);
        }
    }

    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if (GloableConstant.getInstance().getBooksort() != null) {
                mSort.setChecked(true);
                mSort.setText(GloableConstant.getInstance().getBooksort());
//                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down_pre);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mSort.setCompoundDrawables(null,null,drawable,null);
            } else {
                mSort.setChecked(false);
                mSort.setText("销量");
//                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mSort.setCompoundDrawables(null,null,drawable,null);
            }
            if (mSort.getText().toString().equals("最新")) {
                mPage = 1;
                mPageNum = "1";
                order = "created_at";
                sort = "";
                requestBookList(type, sort, order, nameTag, mCatId);
            } else if (mSort.getText().toString().equals("销量升序")) {
                mPage = 1;
                mPageNum = "1";
                order = "sale_num";
                sort = "asc";
                requestBookList(type, sort, order, nameTag, mCatId);
            } else if (mSort.getText().toString().equals("销量降序")) {
                mPage = 1;
                mPageNum = "1";
                order = "sale_num";
                sort = "desc";
                requestBookList(type, sort, order, nameTag, mCatId);
            }
            setBackgroundAlpha(1f);
        }
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private PopupWindow mPop;
    private View mContentView;
    BookTypeAdapter mAdapter;
    private String catName;
    ListView mListView;

    private void showPopWindow(List<NewCatBookResponse.mData> data) {
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_small_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mType.setCompoundDrawables(null, null, drawable, null);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        mAdapter = new BookTypeAdapter(mContext);
        mAdapter.setmList(data);
        mListView.setAdapter(mAdapter);
        Log.d("*******position", GloableConstant.getInstance().getBookPosition() + "++++++++++");
        if (GloableConstant.getInstance().getBookPosition() != null) {
            mAdapter.setSelection(Integer.valueOf(GloableConstant.getInstance().getBookPosition()));
            mAdapter.notifyDataSetChanged();
        }
        mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = mScreenLy.getWidth() / 2 - mContentView.getMeasuredWidth() / 2;
        mPop.showAsDropDown(mScreenLy, xOffset, 0);
        mPop.setOnDismissListener(new dialogDismissListenermDepartment());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelection(position);
                mAdapter.notifyDataSetChanged();
                GloableConstant.getInstance().setBookPosition(position + "");
                NewCatBookResponse.mData mData = (NewCatBookResponse.mData) parent.getAdapter().getItem(position);
                mCatId = mData.getCat_id();
                catName = mData.getCat_name();
                Log.e("********** ", mCatId + "*******catName" + catName);
                GloableConstant.getInstance().setCatId(mCatId);
                GloableConstant.getInstance().setTypeName(catName);
//                requestBookList(type,sort,order,nameTag,mCatId);
                if (mPop.isShowing()) {
                    mPop.dismiss();
                }
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
//        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.Post(ApiUrl.CHECKBOOK, map, new MyCallBack<NewCatBookResponse>() {
            @Override
            public void onSuccess(final NewCatBookResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().size() > 0) {
                       /* mBookallFlag = 1;
                        Log.e("********size", result.getData().size() + "");
                        mAdapter =new BookTypeAdapter(mContext);
                        mAdapter.setmList(result.getData());
                        mListView.setAdapter(mAdapter);*/
                        mBookallFlag = result.getData();
                    }
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
            }
        });
    }

    private void requestBookList(String type, String sort, String order, String book_name, String catid) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        map.put("book_name", book_name);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order", order);
        map.put("sort", sort);
        map.put("cat_id", catid);
        Log.e("**********map", map.toString());
        onResumeNameType = book_name;
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.BOOKLIST, map, new MyCallBack<BookListResponse>() {
            @Override
            public void onSuccess(BookListResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    Log.e("TAGs", mPage + "");
                    if (mPage == 1) {
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }

                    if (result.getData() != null && result.getData().size() > 0) {
//                        mBookRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        if(mPageNum.equals("1")){
//                            mDataList.clear();
//                            mDataList = result.getData();
//                        }else{
//                            if (null == mDataList){
//                                mDataList = result.getData();
//                            }
//                            else{
//                                mDataList.addAll(result.getData());
//                            }
//                        }
//                        mBookRecycler.setAdapter(new CommonAdapter<BookListResponse.mData>(getActivity(),R.layout.item_book_fragment_layout,mDataList) {
//                            @Override
//                            protected void convert(ViewHolder holder, final BookListResponse.mData s, int position) {
//                                holder.setText(R.id.course_name,s.getBook_name());
//                                holder.setText(R.id.course_price,s.getPrice());
//                                holder.setText(R.id.sell_num,s.getSale_num()+"人已购买");
//                                ImageView mBookCover = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
//                                Glide.with(getActivity()).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mBookCover);
//                                holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent();
//                                        intent.setClass(getActivity(), BookDetailActivity.class);
//                                        intent.putExtra("book_id",s.getId());
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        });
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
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });


    }


    private CommRecyclerViewAdapter<BookListResponse.mData> getAdapter() {
        if (null == mRCVBookAdapter) {
            mRCVBookAdapter = new CommRecyclerViewAdapter<BookListResponse.mData>(getActivity(), R.layout.item_book_fragment_layout, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final BookListResponse.mData s, final int position) {
                    holder.setText(R.id.course_name, s.getBook_name());
                    holder.setText(R.id.course_price, s.getPrice());
                    holder.setText(R.id.sell_num, s.getSale_num() + "人已购买");
                    ImageView mBookCover = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                    Glide.with(getActivity()).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mBookCover);
                    holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), BookDetailActivity.class);
                            intent.putExtra("book_id", s.getId());
                            startActivity(intent);
                        }
                    });
                }
            };
            mBookRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBookRecycler.setAdapter(mRCVBookAdapter);
        }
        return mRCVBookAdapter;
    }


}
