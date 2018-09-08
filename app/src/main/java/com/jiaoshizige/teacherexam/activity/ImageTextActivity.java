package com.jiaoshizige.teacherexam.activity;

import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/27.
 * 图文 课程文档页面
 */

public class ImageTextActivity extends BaseActivity{
    @BindView(R.id.image_text_web)
    WebView mImageTextWebView;
    private String mContent;
    private String mId;
    private String mTitle;
    private int isLearnTime = 0;
    private int isAdd = 0;
//    Handler handler=new Handler();
    private AddLearnRecordModel mModel = new AddLearnRecordModel();
    @Override
    protected int getLayoutId() {
        return R.layout.image_text_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
//        setSubTitle().setText("学过了");
        if(getIntent().getExtras().get("content") != null){
            mContent = (String) getIntent().getExtras().get("content");
        }else{
            mContent = "";
        }
        if(getIntent().getExtras().get("title") != null){
            mTitle = (String) getIntent().getExtras().get("title");
        }else {
            mTitle = "";
        }
        if(getIntent().getExtras().get("id") != null){
            mId = (String) getIntent().getExtras().get("id");
        }
        if(getIntent().getExtras().get("model") !=null){

            mModel = (AddLearnRecordModel) getIntent().getExtras().get("model");
            isLearnTime = Integer.valueOf(mModel.getLearn_time()) * 60 * 1000;
            Log.e("TTT",mModel.getCourse_name());
        }
        setToolbarTitle().setText(mTitle);
        WebSettings webSettings = mImageTextWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mImageTextWebView.loadDataWithBaseURL(null, mContent, "text/html", "utf-8", null);
        addLearnRecord();
//        handler.postDelayed(runnable, isLearnTime);
    }
//    Runnable runnable=new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            //要做的事情
//            try{
//                addLearnRecord();
//                    handler.postDelayed(this, 2000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };
    public void addLearnRecord(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type","3");
        map.put("group_id",mModel.getGroup_id());
        map.put("course_id",mModel.getCourse_id());
        map.put("course_name",mModel.getCourse_name());
        map.put("chapter_id",mModel.getChapter_id());//章id
        map.put("chapter_name",mModel.getChapter_name());//章名称
        map.put("section_id",mModel.getSection_id());//节id
        map.put("section_name",mModel.getSection_name());//节名称
        map.put("type_id",mModel.getType());
        map.put("ltime", "1");
        Log.e("TAg",map.toString());
//        map.put("learn_time",mModel.getLearn_time());////学习完成时间：分钟
        Xutil.Post(ApiUrl.AddLEARNRECORD,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
//                handler.removeCallbacks(runnable);
                if(result.getStatus_code().equals("204")){
                    //成功
                    isAdd = 0;
                }else if(result.getStatus_code().equals("205")){
                    //重复提交
                    isAdd = 0;
                }else if(result.getStatus_code().equals("206")){
                    //提交失败
                    isAdd = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                handler.removeCallbacks(runnable);
            }
        });
    }
}
