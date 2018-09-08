package com.jiaoshizige.teacherexam.activity;

import android.content.Context;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ErrorTopicAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ErrorTopicResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.widgets.CustomExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/31.
 */

public class ErrorTopicActivity extends BaseActivity {
    @BindView(R.id.customexpandablelistView)
    CustomExpandableListView mListView;

    private String tiku_id;
    private String user_id;
    private Context mContext;
    private ErrorTopicAdapter mAdapter;

    private List<ErrorTopicResponse.mData> mData;
    private List<ErrorTopicResponse.mSon> mSon;
    @Override
    protected int getLayoutId() {
        return R.layout.error_topic_layput;
    }

    @Override
    protected void initView() {
     setSubTitle().setText("");
     setToolbarTitle().setText("错题练习");
     mContext=this;
     user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
     mData=new ArrayList<>();
     mSon=new ArrayList<>();
        if (GloableConstant.getInstance().getTiku_id()!=null){
         tiku_id=GloableConstant.getInstance().getTiku_id();
     }else {
         tiku_id="";
     }


     if (!tiku_id.equals("")){
         getErrorTopic();
     }
    }
  private void getErrorTopic(){
      Map<String,String> map=new HashMap<>();
      map.put("user_id",user_id);
      map.put("tiku_id",tiku_id);
      GloableConstant.getInstance().startProgressDialog(mContext);
      Xutil.GET(ApiUrl.ERRORTOPIC,map,new MyCallBack<ErrorTopicResponse>(){
          @Override
          public void onSuccess(ErrorTopicResponse result) {
              super.onSuccess(result);
              GloableConstant.getInstance().stopProgressDialog();
              if (result.getStatus_code().equals("200")){
          if (result.getData()!=null&&result.getData().size()>0){
              mData=result.getData();
              for (int i=0;i<mData.size();i++){
                  mSon=mData.get(i).getSon();
              }
              mAdapter=new ErrorTopicAdapter(mData,mSon,mContext);
              mListView.setAdapter(mAdapter);
          }
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
              GloableConstant.getInstance().stopProgressDialog();
          }
      });
  }
}
