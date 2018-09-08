package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.yy.activity.MyCouponActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/31.
 */

public class LuckDrawActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;

    private String user_id;
    private String url = ApiUrl.BASEIMAGE + "api/v1/jiang_rand?user_id=";

    @Override
    protected int getLayoutId() {
        return R.layout.luck_draw_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        setToolbarTitle().setText("");
        setSubTitle().setText("");
        if (getIntent().getExtras().get("user_id") != null) {
            user_id = (String) getIntent().getExtras().get("user_id");
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
        mWebView.loadUrl(url + user_id);
//        mWebView.loadUrl("file:///android_asset/choujiang.html");
        // 复写WebViewClient类的shouldOverrideUrlLoading方法
        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                          // 步骤2：根据协议的参数，判断是否是所需要的url
                                          // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                                          //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                                          Uri uri = Uri.parse(url);
                                          // 如果url的协议 = 预先约定的 js 协议
                                          // 就解析往下解析参数
                                          if (uri.getScheme().equals("js")) {
                                              Log.e("UIUI", "8989898" + uri.getQueryParameter("type"));

                                              if (uri.getQueryParameter("type").equals("1") || uri.getQueryParameter("type").equals("2") || uri.getQueryParameter("type").equals("4")) {
                                                  ToastUtil.showShortToast("得到卡券");
                                                  GloableConstant.getInstance().setFlag("1");
                                                  startActivity(new Intent(LuckDrawActivity.this, MainActivity.class));
                                                  finish();
                                              } else if (uri.getQueryParameter("type").equals("3")) {

                                                  ToastUtil.showShortToast("图书");
                                                  GloableConstant.getInstance().setmLuckBook("1");
                                                  GloableConstant.getInstance().setFlag("1");
                                                  startActivity(new Intent(LuckDrawActivity.this, MainActivity.class));
                                                  finish();
                                              } else if (uri.getQueryParameter("type").equals("5")) {
                                                  GloableConstant.getInstance().setFlag("1");
                                                  startActivity(new Intent(LuckDrawActivity.this, MainActivity.class));
                                                  finish();
                                                  ToastUtil.showShortToast("课程");
                                              } else if (uri.getQueryParameter("type").equals("6")) {
                                                  GloableConstant.getInstance().setFlag("1");
                                                  startActivity(new Intent(LuckDrawActivity.this, MainActivity.class));
                                                  finish();
                                                  ToastUtil.showShortToast("班级");
                                              } else if (uri.getQueryParameter("type").equals("kq")) {
                                                  ToastUtil.showShortToast("得到卡券");
                                                  startActivity(new Intent(LuckDrawActivity.this, MyCouponActivity.class));
                                                  finish();
                                                  ToastUtil.showShortToast("卡券");
                                              } else if (uri.getQueryParameter("type").equals("close")) {
                                                  finish();
                                              }
                                           /*   if (uri.getAuthority().equals("type")) {

                                                  //  步骤3：
                                                  // 执行JS所需要调用的逻辑
                                                  System.out.println("js调用了Android的方法");
                                                  Log.e("UIUI","66666"+uri.getQueryParameter("arg1"));
                                                  // 可以在协议上带有参数并传递到Android上
//                                                  HashMap<String, String> params = new HashMap<>();
//                                                  Set<String> collection = uri.getQueryParameterNames();

                                              }*/

                                              return true;
                                          }
                                          return super.shouldOverrideUrlLoading(view, url);
                                      }
                                  }
        );
    }

    /* mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
         @Override
         public void onReceiveValue(String value) {
             //此处为 js 返回的结果
             Log.e("UIUI","8989898"+value);
         }
     });*/
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        mWebView.loadUrl("javascript:rnd(3,4)");
                    }
                });
            }
        });*/
     /*   mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(LuckDrawActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });*/
/*
        WebChromeClient webchromeclient = new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LuckDrawActivity.this);
                builder.setMessage(message)
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                finish();
                            }
                        }).show();
                result.cancel();
                Log.e("TYYYY",message+"[[[[["+url+"]]]]"+result);
                ToastUtil.showShortToast(message);
                result.confirm();
                return true;
            }
        };
  /*  private class webViewClient extends WebViewClient {
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {

            result.confirm();
            return true;
        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            Uri uri = Uri.parse(url);
            String appid= uri.getQueryParameter("user_id");
            Log.e("TYYYY","333333");
            Log.e("TYYYY",appid);
            Log.e("TYYYY222",uri.getQueryParameter("center_text"));
            Log.e("TYYYY1",uri.toString());
            return true;
        }
        public void onPageFinished(WebView view,String url)
        {
            Log.e("TYYYY3",url.toString());
//            stopProgressDialog();
//            view.loadUrl("javascript:function setHidd(){document.querySelector('.blue-header').style.display=\"none\";}setHidd();");
            //setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();
        }
    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }

    @JavascriptInterface
    public void rnd(String msg) {
        System.out.println("js调用了android的hello方法");
    }
}
