package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.nex3z.flowlayout.FlowLayout;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.SelectPicGridViewAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CommentLableResponse;
import com.jiaoshizige.teacherexam.utils.MainContent;
import com.jiaoshizige.teacherexam.utils.PictureSelectorConfig;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.RatingBar;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/a1 0011.
 * 订单评价
 */

public class MyOrderCommentActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.flow_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.add_photo)
    GridView mGridView;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.content)
    EditText mContent;
    @BindView(R.id.btn_commit)
    Button mCommit;
    @BindView(R.id.course_name)
    TextView mName;
    private Context mContext;
    private float grade = (float) 5;
    private String user_id, order_id;
    private String[] lable_id;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private SelectPicGridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private List<String> mList = new ArrayList<>();
    private List<String> content = new ArrayList<>();
    List<String> lable_list = new ArrayList();
    private Intent intent;
    private String flag;
    private String mType;//1课程2图书3班级

    protected int getLayoutId() {
        return R.layout.my_order_comment_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("订单评价");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单评价");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的订单");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mContext = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        Log.e("***********user_id", user_id);
        intent = getIntent();
        if (intent.getStringExtra("order_id") != null) {
            order_id = intent.getStringExtra("order_id");
            Log.e("opop", order_id + getIntent().getExtras().get("order_id"));
        }
        if (getIntent().getStringExtra("name") != null) {
            mName.setText(getIntent().getStringExtra("name"));
        }
//        grade = mRatingBar.getRating();
//        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                grade = ratingBar.getRating();
//            }
//        });

        mRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                grade = ratingCount;
                Log.e("****111******", grade + "");
            }
        });
        Log.e("**********", grade + "");
        if (intent.getStringExtra("flag") != null) {
            flag = intent.getStringExtra("flag");
            mType = flag;
        }
        Log.e("**********flag", flag);
       /* //课程
        if (flag.equals("1")) {

            commentLable("2");
            mType = "2";
        }
        //套餐
        if (flag.equals("3")) {
            commentLable("1");
            mType = "1";
        }
        //图书
        if (flag.equals("2")) {
            commentLable("3");
            mType = "3";
        }*/

        initGridview();
        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit_lable();
                break;
        }
    }


    /**
     * 评价标签
     */
    private void commentLable(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        Log.e("******map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.LABLE, map, new MyCallBack<CommentLableResponse>() {
            @Override
            public void onSuccess(CommentLableResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        content.add(result.getData().get(i).getLabel_name());
                        mList.add(result.getData().get(i).getId());
                    }
                    Log.e("************cotent", content.toString());
                    for (int i = 0; i < content.size(); i++) {
                        //创建一个Textview
                        final TextView textView = new TextView(mContext);
                        textView.setText(content.get(i));
                        textView.setTag(i);
                        textView.setTextSize(12);
                        textView.setBackgroundResource(R.drawable.button_gray);
                        textView.setTextColor(getResources().getColor(R.color.text_color6));
                        //设置padding
                        int pxlr = textView.getResources().getDimensionPixelOffset(R.dimen.margin_5);
                        int pxlb = textView.getResources().getDimensionPixelOffset(R.dimen.margin_5);
                        textView.setPadding(pxlr, pxlb, pxlr, pxlr);
//                        textView.setTextSize(14);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //标签点击事
                                mList.add(v.getTag() + "");
                                Log.e("size++++++++", mList.get((Integer) v.getTag()).toString());
                                lable_list.add(mList.get((Integer) v.getTag()));
                                for (int i = 0; i < lable_list.size(); i++) {
                                    Log.e("********lable_id", lable_list.get(i).toString());
                                }

//                                final int size = lable_list.size();
//                                lable_id = lable_list.toArray(new String[size]);
//                                Log.e("length++++++++", lable_id.length + "");

                                textView.setBackgroundResource(R.drawable.yellow);
                                textView.setTextColor(getResources().getColor(R.color.yellow));
                            }
                        });
                        mFlowLayout.addView(textView);
                    }
                }

            }
        });

    }

    /**
     * 提交评论
     */

    private void commit_lable() {
        String content = mContent.getText().toString().trim();
        RequestParams params = new RequestParams(ApiUrl.COMMINT);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        for (int i = 0; i < mPicList.size(); i++) {
            File file = new File(mPicList.get(i));
            list.add(new KeyValue("images[]", file));
        }

//        for (int i = 0; i < lable_list.size(); i++) {
////            map.put("tag_id", );
//            list.add(new KeyValue("tag_id", lable_list.get(Integer.parseInt(lable_list.)));
//        }
        for (int i = 0; i < lable_list.size(); i++) {
            Log.e("********lable_id", lable_list.get(i).toString());
            list.add(new KeyValue("tag_id", lable_list.get(i).toString()));
        }

//        list.add(new KeyValue("user_id", 1));
        list.add(new KeyValue("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING)));
        list.add(new KeyValue("order_id", order_id));
//        list.add(new KeyValue("order_id", 36));
        list.add(new KeyValue("type", mType));
        list.add(new KeyValue("rank", grade));
        Log.e("****2222******", grade + "");
        list.add(new KeyValue("content", content));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        Log.e("***********order_id", order_id);
        Log.e("***************params", params.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.PostImageFiles(params, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    finish();
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("********e", ex.getMessage() + "*****");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }


    private void initGridview() {
        mGridViewAddImgAdapter = new SelectPicGridViewAdapter(this, mPicList);
        mGridView.setAdapter(mGridViewAddImgAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过4张，才能点击
                    if (mPicList.size() == MainContent.MAX_SELECT_PIC_NUM) {
                        //最多添加4张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(MainContent.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(this, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainContent.IMG_LIST, mPicList);
        intent.putExtra(MainContent.POSITION, position);
        startActivityForResult(intent, MainContent.REQUEST_CODE_MAIN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainContent.REQUEST_CODE_MAIN && resultCode == MainContent.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainContent.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }


}
