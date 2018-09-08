package com.jiaoshizige.teacherexam.activity.aaa.xx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.google.gson.Gson;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.VideoDownAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.NewCourseListBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.interfa.DownSuccessbackListener;
import com.jiaoshizige.teacherexam.activity.aaa.xx.service.DownService;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import org.json.JSONException;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程下载
 */
public class CourseDownFileActivity extends AppCompatActivity implements DownSuccessbackListener {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.tv_downType)
    TextView tvDownType;
    @BindView(R.id.ll_seleteType)
    LinearLayout llSeleteType;
    @BindView(R.id.nodata)
    ImageView nodata;
    @BindView(R.id.course_no_data)
    LinearLayout courseNoData;
    @BindView(R.id.course_error)
    LinearLayout courseError;
    @BindView(R.id.single_buy)
    RecyclerView singleBuy;
    @BindView(R.id.tv_alldown)
    TextView tvAlldown;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    private Context ctx;
    private boolean isshow;
    private String mType;
    private String mCourseId;
    private String isBuy;
    private int bitrate = 2;//清晰度 1标清 2 高清 3 超清
    private VideoDownAdapter videoDownAdapter;
    private String covermap;
    private String mName;
    private List<NewCourseListBean.DataBean.CourseListBean> course_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_down_file);
        ButterKnife.bind(this);
        ctx = this;
        if (getIntent().getExtras().get("type") != null) {
            mType = (String) getIntent().getExtras().get("type");
            Log.e("*******type", mType);

        } else {
            mType = "";
        }
        if (getIntent().getExtras().get("course_id") != null) {
            mCourseId = (String) getIntent().getExtras().get("course_id");
        } else {
            mCourseId = "";
        }
        if (getIntent().getStringExtra("is_buy") != null) {
            isBuy = getIntent().getStringExtra("is_buy");
        } else {
            isBuy = "";
        }
        if (getIntent().getStringExtra("name") != null) {
            mName = getIntent().getStringExtra("name");
        } else {
            mName = "";
        }
        covermap = getIntent().getStringExtra("covermap");
        requestCatalog();
        DownService.setSuccessbackListener(this);
    }

    @OnClick({R.id.close, R.id.ll_seleteType, R.id.tv_alldown, R.id.tv_manager, R.id.course_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.ll_seleteType:
                showPopu();

                break;
            case R.id.tv_alldown://全部下载
                downAll();
                break;
            case R.id.tv_manager://下载管理
                Intent intent = new Intent(ctx, DownVideosMangerActivity.class);
                startActivity(intent);
                break;
            case R.id.course_error://低级重新获取
                requestCatalog();
                break;
        }
    }



    /**
     * 下载成功回调 更新界面
     */
    @Override
    public void success() {
        videoDownAdapter.notifyDataSetChanged();
    }

    private void showPopu() {
        View view = LayoutInflater.from(ctx).inflate(R.layout.popu_selete_videotype, null);
        final PopuViewHolder popuViewHolder = new PopuViewHolder(view);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = popuViewHolder.radioGroup.getY();
                float y1 = event.getY();
                if (y1 < y) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        popupWindow.showAtLocation(tvDownType, Gravity.CENTER, 0, 0);
        popuViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //清晰度 超清  高清  标清
                switch (checkedId) {

                    case R.id.rb_chao:
                        bitrate = 3;
                        popupWindow.dismiss();
                        tvDownType.setText("超清");
                        videoDownAdapter.setBitrate(bitrate);
                        break;
                    case R.id.rb_gao:
                        bitrate = 2;
                        popupWindow.dismiss();
                        tvDownType.setText("高清");
                        videoDownAdapter.setBitrate(bitrate);
                        break;
                    case R.id.rb_biao:
                        bitrate = 1;
                        tvDownType.setText("标清");
                        popupWindow.dismiss();
                        videoDownAdapter.setBitrate(bitrate);
                        break;

                }
            }
        });

    }

    public void requestCatalog() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("type_id", mType);
      /*  map.put("course_id","16");
        map.put("type_id","1");*/
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
//        map.put("user_id", "52458");
        GloableConstant.getInstance().startProgressDialog(ctx);
        Callback.Cancelable wch = Xutil.GET(ApiUrl.COURSEDETAILCATALOGS, map, new MyCallBack<NewCourseListBean>() {
            @Override
            public void onSuccess(NewCourseListBean result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                String s = new Gson().toJson(result);
                Log.i("wch", s);
                if (result.getStatus_code() == 200) {
                    if (result.getData() != null) {
                        if (result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0) {
                            if (result.getData().getCourse_list().get(0).getCatalog() != null && result.getData().getCourse_list().get(0).getCatalog().size() > 0) {
//                                if (result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size() > 0) {
                                courseNoData.setVisibility(View.GONE);
                                courseError.setVisibility(View.GONE);
                                LinearLayoutManager manager = new LinearLayoutManager(ctx);
                                singleBuy.setLayoutManager(manager);
                                course_list = result.getData().getCourse_list();
//                                singleBuy.setAdapter(new SanJiDeLeiBiaoAdapter(ctx, course_list, isshow, true, mType, mCourseId, Integer.valueOf(isBuy)));
                                videoDownAdapter = new VideoDownAdapter(ctx, course_list, bitrate, mType, mCourseId, covermap, mName);
                                singleBuy.setAdapter(videoDownAdapter);
//
                            } else {
                                courseNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            courseNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                courseError.setVisibility(View.VISIBLE);
                courseNoData.setVisibility(View.GONE);
               /* mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);*/
            }
        });
    }

    private class PopuViewHolder {
        public RadioGroup radioGroup;
        public RadioButton rb_chao;
        public RadioButton rb_gao;
        public RadioButton rb_biao;

        public PopuViewHolder(View itemView) {
            this.radioGroup = itemView.findViewById(R.id.rg_videoType);
            this.rb_chao = itemView.findViewById(R.id.rb_chao);
            this.rb_gao = itemView.findViewById(R.id.rb_gao);
            this.rb_biao = itemView.findViewById(R.id.rb_biao);
        }
    }
    private int downParentPosition;
    private int downChildPosition;
    private NewCourseListBean.DataBean.CourseListBean.CatalogBean catalogBean;
    private void downAll() {
        if (downParentPosition == course_list.size()) {
            Toast.makeText(ctx, "全部加入加载队列", Toast.LENGTH_SHORT).show();
            videoDownAdapter.notifyDataSetChanged();
            return;
        }

        NewCourseListBean.DataBean.CourseListBean courseListBean = course_list.get(downParentPosition);
        if (courseListBean.getCatalog().size() == downChildPosition) {
            downChildPosition = 0;
            downParentPosition++;
        }
        catalogBean = courseListBean.getCatalog().get(downChildPosition);

        String vid = catalogBean.getVideo();

        new DownAsyncTast().execute(vid);
    }
    /**
     * 异步获取视频信息
     */
    private class DownAsyncTast extends AsyncTask<String, Void, PolyvVideoVO> {
        @Override
        protected PolyvVideoVO doInBackground(String... strings) {

            try {
                return PolyvSDKUtil.loadVideoJSON2Video(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PolyvVideoVO video) {
            super.onPostExecute(video);
            if (video == null) {
                Toast.makeText(ctx, "操作失败,重新下载", Toast.LENGTH_SHORT).show();
                return;
            }
            if (video.getDuration() == null) {
                Toast.makeText(ctx, "操作失败,重新下载", Toast.LENGTH_SHORT).show();
                return;
            }
            PolyvDownloadSQLiteHelper mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(ctx);
            PolyvDownloadInfo downloadInfo = new PolyvDownloadInfo(video.getVid(), video.getDuration(),
                    video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), 0, catalogBean.getTitle());
            if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
                downloadInfo.setClassname(course_list.get(downParentPosition).getName());
                if (mType.equals("1")) {
                    downloadInfo.setClass_id(Integer.valueOf(course_list.get(downParentPosition).getId()));
                } else {
                    downloadInfo.setClass_id(Integer.valueOf(mCourseId));
                }
                downloadInfo.setChapter(covermap);
                downloadInfo.setChapter_id(String.valueOf(catalogBean.getId()));
                Log.e("TA", catalogBean.getTitle());
                downloadInfo.setSection_name(catalogBean.getTitle());
                downloadInfo.setSection_id(String.valueOf(catalogBean.getId()));
                downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
                downloadInfo.setLearn_time(String.valueOf(catalogBean.getLearn_time()));
                downloadInfo.setType(mType);
                mDownloadSQLiteHelper.insert(downloadInfo);
                DownManagerBean managerBean = new DownManagerBean();
                managerBean.setCovermap(covermap);
                managerBean.setCourse_id(Integer.valueOf(mCourseId));
                managerBean.setTitle(mName);
                mDownloadSQLiteHelper.insertMannger(managerBean);
                downChildPosition++;
                if (VideoDownAdapter.backInterface != null) {
                    VideoDownAdapter.backInterface.backDown(video.getVid(),downloadInfo,bitrate);
                }
                downAll();
            }
        }
    }
}
