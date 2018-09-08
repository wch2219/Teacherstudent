package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.yy.activity.MyCouponActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/1.
 * 外链的activity
 */

public class LinkWebActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;
    private String mUrl = "";
    private String title;

    @Override
    protected int getLayoutId() {
        return R.layout.luck_draw_layout;
    }

    @Override
    protected void initView() {
        if (getIntent().getStringExtra("title") != null) {
            title = getIntent().getStringExtra("title");
        } else {
            title = "教师资格证";
        }
        setToolbarTitle().setText(title);

        setToolbarTitle().setText("");
        setSubTitle().setText("");
        if (getIntent().getExtras().get("url") != null) {
            mUrl = (String) getIntent().getExtras().get("url");
        }
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
//        webSettings.setBuiltInZoomControls(false);
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new webViewClient());
        GloableConstant.getInstance().showmeidialog(this);
    }

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // 步骤2：根据协议的参数，判断是否是所需要的url
            // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
            //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

            Uri uri = Uri.parse(url);
            // 如果url的协议 = 预先约定的 js 协议
            // 就解析往下解析参数
            if (uri.getScheme().equals("js")) {
                Log.e("UIUI", "8989898" + uri.getQueryParameter("type"));
                String type = uri.getQueryParameter("type");
                String typeid = uri.getQueryParameter("goods_id");
                adsIntentTo(type, typeid, title);

                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            GloableConstant.getInstance().stopProgressDialog();
//            view.loadUrl("javascript:function setHidd(){document.querySelector('.blue-header').style.display=\"none\";}setHidd();");
            //setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }


    private void adsIntentTo(String type, String typeid, String title) {
        switch (Integer.valueOf(type)) {
            case 1:
//                                    ToastUtil.showShortToast("班级");
                Intent classIntent = new Intent();
                classIntent.setClass(LinkWebActivity.this, CoursesDetialActivity.class);
                classIntent.putExtra("type", type);
                classIntent.putExtra("course_id", typeid);//type_id为课程id
                startActivity(classIntent);
                finish();
                break;
            case 2:
//                                    ToastUtil.showShortToast("课程");
                Intent courseIntent = new Intent();
                courseIntent.setClass(LinkWebActivity.this, CoursesDetialActivity.class);
                courseIntent.putExtra("type", type);
                courseIntent.putExtra("course_id", typeid);//type_id为课s程id
                startActivity(courseIntent);
                finish();
                break;
            case 3:
//                                    ToastUtil.showShortToast("图书");
                Intent bookIntent = new Intent();
                bookIntent.setClass(LinkWebActivity.this, BookDetailActivity.class);
                bookIntent.putExtra("book_id", typeid);
                startActivity(bookIntent);
                finish();
                break;
            case 4:
//                                    ToastUtil.showShortToast("图文");
                Intent ItIntent = new Intent();
                ItIntent.setClass(LinkWebActivity.this, InformationDetialActivity.class);
                ItIntent.putExtra("info_id", typeid);
                startActivity(ItIntent);
                finish();
                break;
            case 5:
//                                    ToastUtil.showShortToast("外链");
                Intent linkIntent = new Intent();
                linkIntent.setClass(LinkWebActivity.this, LinkWebActivity.class);
                linkIntent.putExtra("url", typeid);
                linkIntent.putExtra("title", title);
                startActivity(linkIntent);
                finish();
                break;
            case 6:
//                                    ToastUtil.showShortToast("课程列表");
                GloableConstant.getInstance().setFlag("1");
                startActivity(new Intent(LinkWebActivity.this, MainActivity.class));
                finish();
                //sendBroadcast("classSchool");
                break;
            case 7:
//                                    ToastUtil.showShortToast("图书列表");
                GloableConstant.getInstance().setFlag("2");
                startActivity(new Intent(LinkWebActivity.this, MainActivity.class));
                finish();
                //sendBroadcast("classSchool");
                break;
            case 8:
//                                    ToastUtil.showShortToast("公开课列表");
                GloableConstant.getInstance().setFlag("3");
                startActivity(new Intent(LinkWebActivity.this, MainActivity.class));
                finish();
                //sendBroadcast("");
                break;
        }
    }
}
