package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassDetailResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 *
 * 班级详情   已拥有的班级详情(feiqi)
 */

public class MyClassDetialActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back_im)
    TextView mBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbar_title;
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbar_subtitle;
    @BindView(R.id.study_prodress)
    ZzHorizontalProgressBar mProgress_bar;
    @BindView(R.id.completion_degree)
    TextView mCompletionDegree;
    @BindView(R.id.headmaster_photo)
    ImageView mHeadMaster;
    @BindView(R.id.assistant_photo)
    RecyclerView mAssistant;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_group)
    TextView mAddGroup;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String user_id,qq;
    private String mProgress;
    private Context mContext;
    private String mClassId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_class_datail_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mContext = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getExtras().get("progress") != null) {
            mProgress = (String) getIntent().getExtras().get("progress");
        } else {
            mProgress = "0";
        }
        mCompletionDegree.setText(mProgress);
        mProgress_bar.setProgress(Integer.parseInt(mProgress));
        if (getIntent().getStringExtra("id") != null) {
            mClassId = getIntent().getStringExtra("id");
        }else{
            mClassId = "";
        }
        if (getIntent().getExtras().get("name") != null) {
            mToolbar_title.setText((String) getIntent().getExtras().get("name"));
        } else {

            mToolbar_title.setText("");
        }
        mToolbar_subtitle.setText("学习日历");
        mBack.setOnClickListener(this);
        mToolbar_subtitle.setOnClickListener(this);
        mAddGroup.setOnClickListener(this);
        classDatail();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                classDatail();
            }

            @Override
            public void loadMore() {
                classDatail();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_im:
                finish();
                break;
            case R.id.toolbar_subtitle:
                Intent intent = new Intent();
                intent.putExtra("class_id", mClassId);
                intent.setClass(MyClassDetialActivity.this, StudyCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.add_group:
                joinQQGroup(qq);
                break;
        }
    }

    private void classDatail() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", mClassId);
//        map.put("user_id", 1);
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*********",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.CLASSSDETAIL, map, new MyCallBack<ClassDetailResponse>() {
            @Override
            public void onSuccess(ClassDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getQq() != "") {
                        qq = result.getData().getQq();
                    } else {
                        ToastUtil.showShortToast("该班级尚未建群");
                    }
                    mToolbar_title.setText(result.getData().getGroup_name());
                    Glide.with(mContext).load(result.getData().getTeacher().getHeadImg())
                            .apply(GloableConstant.getInstance().getOptions()).into(mHeadMaster);
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mAssistant.setLayoutManager(manager);

                    mAssistant.setAdapter(new CommonAdapter<ClassDetailResponse.mAssistant>(mContext, R.layout.item_small_phpto, result.getData().getAssistant()) {
                        @Override
                        protected void convert(ViewHolder holder, ClassDetailResponse.mAssistant mAssistant, int position) {
                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.small_phpto);
                            Glide.with(mContext).load(mAssistant.getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(imageView);
                        }
                    });
                       if (result.getData().getCourse().size()>0){
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommonAdapter<ClassDetailResponse.mCoures>(mContext, R.layout.item_my_class_layout, result.getData().getCourse()) {
                            @Override
                            protected void convert(ViewHolder holder, final ClassDetailResponse.mCoures mCoures, int position) {
                                ImageView image = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                                Glide.with(mContext).load(mCoures.getCover_image()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(image);
                                holder.setText(R.id.course_name, mCoures.getName());
                                holder.setText(R.id.sell_num, mCoures.getSale_num() + "人在学习");
                                holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(mContext,ClassCoursesDetialActivity.class);
                                        intent.putExtra("flag","1");
                                        intent.putExtra("type","1");
                                        intent.putExtra("group_id",mClassId);
                                        intent.putExtra("course_id",mCoures.getId());
                                        intent.putExtra("course_name",mCoures.getName());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else {
                           mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
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
    /****************
     * 发起添加群流程。群号：(121683601) 的 key 为： 5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD
     * 调用 joinQQGroup(5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD) 即可发起手Q客户端申请加群 2013级梦翔会员交流群(121683601)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtil.showShortToast("未安装手Q或安装的版本不支持");
            return false;
        }
    }
}
