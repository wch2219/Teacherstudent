package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.SearchHotAdapter;
import com.jiaoshizige.teacherexam.adapter.SearchLatelyAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.SearchResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.CancelOrOkDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/21.
 * 搜索课程 图书
 * type 1 课程 2图书
 */

public class SearchActivity extends BaseActivity{
    @BindView(R.id.search_content)
    EditText mSearchContent;
    @BindView(R.id.cancel)
    TextView mCansel;
    @BindView(R.id.hot_search)
    GridView mHotSearch;
    @BindView(R.id.lately_search)
    GridView mLatelySearch;
    @BindView(R.id.dele_lately_search)
    TextView mDeleLatelySearch;
    private String mType;//1課程 2圖書
    private SearchHotAdapter mHotAdapter;
    private SearchLatelyAdapter mMyAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.search_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索课程");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索课程");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        if(getIntent().getExtras().get("type") != null){
            mType = (String) getIntent().getExtras().get("type");
        }else {
            mType = "";
        }
        if(getIntent().getExtras().get("book_name") != null){
            GloableConstant.getInstance().setSearchBookTag((String) getIntent().getExtras().get("book_name"));

        }else{
            GloableConstant.getInstance().setSearchBookTag("");
        }
        if (mType.equals("1")){
            mSearchContent.setText( GloableConstant.getInstance().getSearchCourseTag());
        }else {
            mSearchContent.setText( GloableConstant.getInstance().getSearchBookTag());
        }

        mHotAdapter = new SearchHotAdapter(this);
        mMyAdapter = new SearchLatelyAdapter(this);
        mHotSearch.setAdapter(mHotAdapter);
        mLatelySearch.setAdapter(mMyAdapter);
        mHotSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResponse.mHotTag mHot = (SearchResponse.mHotTag) parent.getAdapter().getItem(position);
                if(mType.equals("1")){
                    GloableConstant.getInstance().setSearchCourseTag(mHot.getKeyword());
                    GloableConstant.getInstance().setMessage("1");
                }else{
                    GloableConstant.getInstance().setSearchBookTag(mHot.getKeyword());
                }

                Intent intent = new Intent();
                intent.putExtra("result",mHot.getKeyword());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mLatelySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResponse.mMyTag mTag = (SearchResponse.mMyTag) parent.getAdapter().getItem(position);
                if(mType.equals("1")){
                    GloableConstant.getInstance().setSearchCourseTag(mTag.getKeyword());
                    GloableConstant.getInstance().setMessage("1");
                }else{
                    GloableConstant.getInstance().setSearchBookTag(mTag.getKeyword());
                }
                Intent intent = new Intent();
                intent.putExtra("result",mTag.getKeyword());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    Log.e("caoso","ss"+actionId+"ee"+event);

                    if(mType.equals("1")){
                        GloableConstant.getInstance().setMessage("1");
                        GloableConstant.getInstance().setSearchCourseTag(mSearchContent.getText().toString());
                    }else{
                        GloableConstant.getInstance().setSearchBookTag(mSearchContent.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("result",mSearchContent.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                    requestSearch(mType);
                    return false;
                }
                return false;
            }
        });
        requestSearch(mType);
    }
    @OnClick({R.id.cancel,R.id.dele_lately_search})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.cancel:
                GloableConstant.getInstance().setSearchBookTag("");
                finish();
                break;
            case R.id.dele_lately_search:
                CancelOrOkDialog dialog = new CancelOrOkDialog(SearchActivity.this, "确定要删除搜索记录吗?") {
                    @Override
                    public void ok() {
                        super.ok();
                        requestClearSearch();
                        dismiss();
                    }
                };
                dialog.show();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        GloableConstant.getInstance().setSearchBookTag("");
    }

    private void requestSearch(String type){
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.d("**********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.SEARCH,map,new MyCallBack<SearchResponse>(){
            @Override
            public void onSuccess(SearchResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("200")){
                    if(result.getData().getHot_tag() != null && result.getData().getHot_tag().size() > 0){
                            mHotAdapter.setmList(result.getData().getHot_tag());
                    }else{

                    }
                    if(result.getData().getMy_tag() != null && result.getData().getMy_tag().size() > 0){

                        mMyAdapter.setmList(result.getData().getMy_tag());
                        mDeleLatelySearch.setVisibility(View.VISIBLE);
                    }else{
                        mDeleLatelySearch.setVisibility(View.GONE);
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

                Log.e("******ex", ex.getMessage()+"*******" );
            }
        });
    }
    private void requestClearSearch(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type",mType);
        Log.e("*******map",map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CLEARSEARCH,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                Log.e("*******resylt",result.getStatus_code());
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("204")){
                    mMyAdapter.clear();
                    ToastUtil.showShortToast(result.getMessage());
                    mMyAdapter.notifyDataSetChanged();
                    ToastUtil.showShortToast("最近记录已清除");
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

}
