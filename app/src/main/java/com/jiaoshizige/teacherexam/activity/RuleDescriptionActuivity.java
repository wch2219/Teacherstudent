package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.RuleResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/19 0019.
 * 教师币使用规则
 */

public class RuleDescriptionActuivity extends BaseActivity {
    /*@BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;*/
    @BindView(R.id.agreement)
    WebView mAgreement;
    @BindView(R.id.nodata)
    TextView mNoData;
    private String _id;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.rule_description_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("教师币使用规则");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("教师币使用规则");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");


        if (getIntent().getStringExtra("flag") != null) {
            if (getIntent().getStringExtra("flag").equals("teacher")) {
                _id = "24";
                setToolbarTitle().setText("使用规则");
            }
            if (getIntent().getStringExtra("flag").equals("service")){
                _id="25";
                setToolbarTitle().setText("服务协议");
            }
            if(getIntent().getExtras().get("flag").equals("sign")){
                _id = "26";
                setToolbarTitle().setText("签到规则");
            }
        }
        WebSettings webSettings = mAgreement.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptEnabled(true);
        postRule();
    }

    private void postRule() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", _id);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.RULE, map, new MyCallBack<RuleResponse>() {
            @Override
            public void onSuccess(RuleResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if(result.getData() != null && result.getData().size() > 0){
                        mNoData.setVisibility(View.GONE);
                        mAgreement.loadDataWithBaseURL(null, result.getData().get(0).getValue(), "text/html", "utf-8", null);
                    }else{
                        mNoData.setVisibility(View.VISIBLE);
                    }

                 /*   mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerView.setAdapter(new CommonAdapter<RuleResponse.mData>(mContext,R.layout.item_rule_desciption_layout,result.getData()) {
                        @Override
                        protected void convert(ViewHolder holder, RuleResponse.mData mData, int position) {
                            Spanned rule= Html.fromHtml(mData.getValue());
                            holder.setText(R.id.rule, String.valueOf(rule));
                        }
                    });*/
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                mNoData.setVisibility(View.VISIBLE);
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }
}
