package com.jiaoshizige.teacherexam.yy.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.GetZanReplyResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.model.QuestionAnswerDetailResponse;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/16.
 * 问题详情
 */
public class QuestionDetialActivity extends BaseActivity {
    @BindView(R.id.question_subject)
    TextView mQuestionSubject;
    @BindView(R.id.question_photo)
    ImageView mQuestionPhoto;
    @BindView(R.id.nickname)
    TextView mNickName;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.answer_num)
    TextView mAnswerNum;
    @BindView(R.id.sort_type)
    TextView mSortType;
    @BindView(R.id.sort_switch)
    LinearLayout mSortSwitch;
    @BindView(R.id.answer_list)
    RecyclerView mAnswerList;
    @BindView(R.id.reply_content)
    EditText mReplyContent;
    @BindView(R.id.reply)
    Button mReply;
    @BindView(R.id.sort_ly)
    LinearLayout mSortLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.sort_tgb)
    ToggleButton mSortTgb;
    private AllPopupwindow mSortPopwpWindow;
    private String mQuetionId;
    private String mPageSize = "15";
    private int mPage = 1;
    private String mOrderZan = "asc";
    private CommRecyclerViewAdapter<QuestionAnswerDetailResponse.mReply> mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.question_detial_activity;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("问题详情");
        if(getIntent().getExtras().get("q_id") != null){
            mQuetionId = (String) getIntent().getExtras().get("q_id");
        }else{
            mQuetionId = "";
        }
        if (getIntent().getStringExtra("message_id")!=null){
            deleteMessage(getIntent().getStringExtra("message_id"));
        }
        NoteaWorkChapterResponse.mData ll = new NoteaWorkChapterResponse.mData();
        ll.setTitle("升序");
        NoteaWorkChapterResponse.mData ll2 = new NoteaWorkChapterResponse.mData();
        ll2.setTitle("降序");
        List<NoteaWorkChapterResponse.mData> list = new ArrayList<>();
        list.add(ll);
        list.add(ll2);
        mSortPopwpWindow = new AllPopupwindow(this,list,"question");


        requestQuestionAnswerDetail();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                requestQuestionAnswerDetail();
            }

            @Override
            public void loadMore() {
                mPage++;
                requestQuestionAnswerDetail();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                requestQuestionAnswerDetail();
            }
        });
    }
    @OnClick({R.id.sort_switch,R.id.sort_tgb})
    public void onSortSwitchClick(){
        mSortPopwpWindow.setOnDismissListener(new QuestionDetialActivity.poponDismissListenermDepartment());
        mSortPopwpWindow.showFilterPopup(mSortLy);
        mSortTgb.setChecked(true);
    }
    @OnClick(R.id.reply)
    public void onClick(){
        if(!mReplyContent.getText().toString().trim().equals("")){
            requestReply();
        }else{
            ToastUtil.showShortToast("请先输入回复内容");
        }
    }
    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            if (GloableConstant.getInstance().getmQuestionTyope()!=null){
                if(GloableConstant.getInstance().getmQuestionTyope().equals("升序")){
                    mOrderZan = "asc";
                    requestQuestionAnswerDetail();
                }else{
                    mOrderZan = "desc";
                    requestQuestionAnswerDetail();
                }
            }
            mSortTgb.setChecked(false);
        }
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 问答详情
     */
    private void requestQuestionAnswerDetail(){
        Map<String,String> map = new HashMap<>();
        map.put("quiz_id",mQuetionId);
//        map.put("quiz_id","3");
        map.put("page",mPage+"");
        map.put("page_size",mPageSize);
//        map.put("user_id","65");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));

        map.put("order_zan",mOrderZan);
        Log.e("TTG",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.QADETAIL,map,new MyCallBack<QuestionAnswerDetailResponse>(){
            @Override
            public void onSuccess(QuestionAnswerDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        if(result.getData().getQuiz() != null){
                            mQuestionSubject.setText(result.getData().getQuiz().getContent());
                            mNickName.setText(result.getData().getQuiz().getName());
                            mTime.setText(result.getData().getQuiz().getCreated_at());
                            Glide.with(QuestionDetialActivity.this).load(result.getData().getQuiz().getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mQuestionPhoto);
                            if(mPage == 1){
                                getAdapter().updateData(result.getData().getReply());
                            } else {
                                getAdapter().appendData(result.getData().getReply());
                            }
                            if(result.getData().getReply() != null && result.getData().getReply().size() > 0){
                                mAnswerNum.setText("共"+result.getData().getReply().size()+"人回答");
//                                mAnswerList.setLayoutManager(new LinearLayoutManager(QuestionDetialActivity.this));
//                                mAnswerList.setAdapter(new CommonAdapter<QuestionAnswerDetailResponse.mReply>(QuestionDetialActivity.this,R.layout.item_question_detial_answer,result.getData().getReply()) {
//                                    @Override
//                                    protected void convert(final ViewHolder holder, final QuestionAnswerDetailResponse.mReply mReply, int position) {
//                                        holder.setText(R.id.answer_nickname,mReply.getName());
//                                        holder.setText(R.id.answer_time,mReply.getCreated_at());
//                                        holder.setText(R.id.answer_content,mReply.getContent());
//                                        holder.setText(R.id.zan_num,mReply.getCount_zan());
//                                        ImageView mAnswerPhoto = (ImageView) holder.getConvertView().findViewById(R.id.answer_photo);
//                                        Glide.with(mContext).load(mReply.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mAnswerPhoto);
//                                        if(mReply.getIs_like().equals("1")){
//                                            holder.setBackgroundRes(R.id.zan,R.mipmap.common_like_pre);
//                                        }else{
//                                            holder.setBackgroundRes(R.id.zan,R.mipmap.common_like);
//                                        }
//                                        holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                requestZan(mReply.getId(),holder);
////                                                notifyDataSetChanged();
//                                            }
//                                        });
//                                    }
//                                });
                            }else{
                                if(mPage > 1){
                                    ToastUtil.showShortToast("没有更多数据了");
                                }else{
                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                                    mAnswerNum.setText("共"+"0"+"人回答");
                                }

                            }
                        }

                    }
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

    /**
     * 点赞
     * @param mReplyId
     */
    private void requestZan(final QuestionAnswerDetailResponse.mReply mReplyId){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("be_like_id",mReplyId.getId());
        map.put("type","3");
        Xutil.Post(ApiUrl.GETZAN,map,new MyCallBack<GetZanReplyResponse>(){
            @Override
            public void onSuccess(GetZanReplyResponse result) {
                super.onSuccess(result);
                if(result!= null && result.getData() != null){
                    mReplyId.setCount_zan(result.getData().getCount());
                }
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("点赞成功");
                    mReplyId.setIs_like("1");
                }else if(result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast("取消点赞成功");
                    mReplyId.setIs_like("0");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
    /**
     * 回复
     */
    private void requestReply(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("quiz_id",mQuetionId);
        map.put("content",mReplyContent.getText().toString().trim());
        Xutil.Post(ApiUrl.REPLYADD,map,new MyCallBack<GetZanReplyResponse>(){
            @Override
            public void onSuccess(GetZanReplyResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("回复成功");
                    mReplyContent.setText("");
                    requestQuestionAnswerDetail();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 查看推送消息
     */
    private void deleteMessage(String message_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message_id);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING));
        map.put("type",1);
        Xutil.Post(ApiUrl.DELETE,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")){
//                ToastUtil.showShortToast("删除成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private CommRecyclerViewAdapter<QuestionAnswerDetailResponse.mReply> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<QuestionAnswerDetailResponse.mReply>(this, R.layout.item_question_detial_answer, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final QuestionAnswerDetailResponse.mReply mReply, final int position) {
                    holder.setText(R.id.answer_nickname,mReply.getName());
                    holder.setText(R.id.answer_time,mReply.getCreated_at());
                    holder.setText(R.id.answer_content,mReply.getContent());
                    holder.setText(R.id.zan_num,mReply.getCount_zan());
                    ImageView mAnswerPhoto = (ImageView) holder.getConvertView().findViewById(R.id.answer_photo);
                    Glide.with(mContext).load(mReply.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mAnswerPhoto);
                    if(mReply.getIs_like().equals("1")){
                        holder.setBackgroundRes(R.id.zan,R.mipmap.common_like_pre);
                    }else{
                        holder.setBackgroundRes(R.id.zan,R.mipmap.common_like);
                    }
                    holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestZan(mReply);
//                                                notifyDataSetChanged();
                        }
                    });
                }
            };
            mAnswerList.setLayoutManager(new LinearLayoutManager(this));
            mAnswerList.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}

