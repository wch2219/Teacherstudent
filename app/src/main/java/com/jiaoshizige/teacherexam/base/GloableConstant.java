package com.jiaoshizige.teacherexam.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.OpenClassResponse;
import com.jiaoshizige.teacherexam.model.PracticeRecordsResponse;
import com.jiaoshizige.teacherexam.model.RecordDetailResponse;
import com.jiaoshizige.teacherexam.model.TestResultDetailResponse;
import com.jiaoshizige.teacherexam.utils.CustomProgressDialog;
import com.jiaoshizige.teacherexam.utils.GlideCircleTransform;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.widgets.CustomProgressDialog1;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;

import java.util.List;

/**
 * 全局的单列
 */


public class GloableConstant {
    private static GloableConstant instance;
    private Context mAppContext;
    private String type;
    private String sort;
    private String price;
    private String booktype;
    private String booksort;
    private String bookprice;
    private String mQuestionTyope;
    private String mNoteChapterId;
    private String mNoteChapterTitle;
    private String mAddNoteChapterTitle;
    private String mAddNoteChapterId;
    private String searchCourseTag;
    private String searchBookTag;
    private String mHomeWorkChapterId;
    private String mHomeWorkChapterTitle;
    private String mListNoteChapterId;
    private String mListNoteChapterTitle;
    private String catId, typeName;
    private CustomProgressDialog dialog;
    private CustomProgressDialog1 progressDialog;
    private int position;
    private int status;
    private boolean correct;
    private String done;
    private String flag;
    private String order;//订单类型：1.教师币订单 2.图书，班级，课程订单
    private String check;//选中类型：0.未全选 1.全选
    private int mSignFlag;
    private String mCourseName;
    private String mBookName;
    private String collectType;
    private String result;//是否测评 0未测评 1已测评
    private String mLuckBook ;//抽奖抽中图书 进入图书列表
    private int site;
    private int location;
    private int practice_pagerSize;
    private List<OpenClassResponse.mNow> mNows;
    private List<OpenClassResponse.mRecent> mRecents;
    private List<PracticeRecordsResponse.mData> mDataList;
    private List<RecordDetailResponse.mData> mRecordList;
    private String bookPosition;
    private String coursePosition;
    private String courseTypeName;
    private String coursrTypeId;
    private String message;
    private String correctNum;
    private String tiku_id;
    private String momi_position;
    private List<TestResultDetailResponse.mData> mTestResultList;

    private List<ZhenTi> mZhenTiList;//错题

    public List<ZhenTi> getZhenTiList() {
        return mZhenTiList;
    }

    public void setZhenTiList(List<ZhenTi> zhenTiList) {
        mZhenTiList = zhenTiList;
    }

    public List<TestResultDetailResponse.mData> getmTestResultList() {
        return mTestResultList;
    }

    public void setmTestResultList(List<TestResultDetailResponse.mData> mTestResultList) {
        this.mTestResultList = mTestResultList;
    }

    public String getMomi_position() {
        return momi_position;
    }

    public void setMomi_position(String momi_position) {
        this.momi_position = momi_position;
    }

    public List<RecordDetailResponse.mData> getmRecordList() {
        return mRecordList;
    }

    public void setmRecordList(List<RecordDetailResponse.mData> mRecordList) {
        this.mRecordList = mRecordList;
    }

    public String getTiku_id() {
        return tiku_id;
    }

    public void setTiku_id(String tiku_id) {
        this.tiku_id = tiku_id;
    }

    public List<PracticeRecordsResponse.mData> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<PracticeRecordsResponse.mData> mDataList) {
        this.mDataList = mDataList;
    }

    public String getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(String correctNum) {
        this.correctNum = correctNum;
    }

    private String ceshi;

    public String getCeshi() {
        return ceshi;
    }

    public void setCeshi(String ceshi) {
        this.ceshi = ceshi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getCoursrTypeId() {
        return coursrTypeId;
    }

    public void setCoursrTypeId(String coursrTypeId) {
        this.coursrTypeId = coursrTypeId;
    }

    public String getCoursePosition() {
        return coursePosition;
    }

    public void setCoursePosition(String coursePosition) {
        this.coursePosition = coursePosition;
    }

    public String getBookPosition() {
        return bookPosition;
    }

    public void setBookPosition(String bookPosition) {
        this.bookPosition = bookPosition;
    }

    public List<OpenClassResponse.mRecent> getmRecents() {
        return mRecents;
    }

    public void setmRecents(List<OpenClassResponse.mRecent> mRecents) {
        this.mRecents = mRecents;
    }

    public List<OpenClassResponse.mNow> getmNows() {
        return mNows;
    }

    public void setmNows(List<OpenClassResponse.mNow> mNows) {
        this.mNows = mNows;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    private String all;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }


    public GloableConstant() {
    }

    public static GloableConstant getInstance() {
        if (instance == null)
            instance = new GloableConstant();
        return instance;

    }

    public void init(Context context) {
        if (context == null)
            throw new IllegalArgumentException("传入context");
        mAppContext = context;
    }

    public void clearAll() {
        SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, "");
        SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_IS_LOGIN, "0");
        SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, "");
        Sphelper.removeFile(mAppContext, "WIFISWITCH");
    }


    /**
     * 判断当前网络是否是wifi。
     */

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public void showmeidialog(Context context) {
        if (!(context instanceof Activity)) {
            throw new ClassCastException("显示dialog context 必须为activity");
        }
//        dialog = new CustomProgressDialog(context, "正在加载中......", R.drawable.progress);
//        dialog.setCanceledOnTouchOutside(true);//设置是否可以点击外部消失
//        dialog.setCancelable(true);//设置是否可以按退回键取消
//        dialog.show();

        if (progressDialog == null) {
            progressDialog = CustomProgressDialog1.createDialog(context);
            progressDialog.setMessage("正在加载中...");
        }
        if (((Activity) context).isFinishing()) {
            return;
        }
        if (progressDialog.isShowing()) {
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgressDialog() {
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog = null;
        }
    }

    public void startProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog1.createDialog(context);
            progressDialog.setMessage("正在加载中...");
        }
        if (progressDialog.isShowing()) {
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgressDialog1() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog = null;
        }
    }

    RequestOptions defaultOption = new RequestOptions()
            .fitCenter()
            .error(R.mipmap.home_png_placeholder_default)
            .placeholder(R.mipmap.home_png_placeholder_default)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    public RequestOptions getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(RequestOptions defaultOption) {
        this.defaultOption = defaultOption;
    }

    RequestOptions defaultOptionssmall = new RequestOptions()
            .fitCenter()
            .error(R.mipmap.home_png_activity_default)
            .placeholder(R.mipmap.home_png_activity_default)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    public RequestOptions getDefaultOptionssmall() {
        return defaultOptionssmall;
    }

    public void setDefaultOptionssmall(RequestOptions defaultOptionssmall) {
        this.defaultOptionssmall = defaultOptionssmall;
    }

    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.login_icon_headportraitr_default)
            .error(R.mipmap.login_icon_headportraitr_default)
            .priority(Priority.HIGH)
            .transform(new GlideCircleTransform(mAppContext))
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    public RequestOptions getOptions() {
        return options;
    }

    public void setOptions(RequestOptions options) {
        this.options = options;
    }


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    public String getBooksort() {
        return booksort;
    }

    public void setBooksort(String booksort) {
        this.booksort = booksort;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getSearchCourseTag() {
        return searchCourseTag;
    }

    public void setSearchCourseTag(String searchCourseTag) {
        this.searchCourseTag = searchCourseTag;
    }

    public String getSearchBookTag() {
        return searchBookTag;
    }

    public void setSearchBookTag(String searchBookTag) {
        this.searchBookTag = searchBookTag;
    }

    public String getmHomeWorkChapterId() {
        return mHomeWorkChapterId;
    }

    public void setmHomeWorkChapterId(String mHomeWorkChapterId) {
        this.mHomeWorkChapterId = mHomeWorkChapterId;
    }

    public String getmQuestionTyope() {
        return mQuestionTyope;
    }

    public void setmQuestionTyope(String mQuestionTyope) {
        this.mQuestionTyope = mQuestionTyope;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMuNoteChapter() {
        return mNoteChapterId;
    }

    public void setMuNoteChapter(String muNoteChapter) {
        this.mNoteChapterId = muNoteChapter;
    }

    public String getmAddNoteChapterId() {
        return mAddNoteChapterId;
    }

    public void setmAddNoteChapterId(String mAddNoteChapterId) {
        this.mAddNoteChapterId = mAddNoteChapterId;
    }

    public String getmNoteChapterTitle() {
        return mNoteChapterTitle;
    }

    public void setmNoteChapterTitle(String mNoteChapterTitle) {
        this.mNoteChapterTitle = mNoteChapterTitle;
    }

    public String getmAddNoteChapterTitle() {
        return mAddNoteChapterTitle;
    }

    public void setmAddNoteChapterTitle(String mAddNoteChapterTitle) {
        this.mAddNoteChapterTitle = mAddNoteChapterTitle;
    }

    public String getmNoteChapterId() {
        return mNoteChapterId;
    }

    public void setmNoteChapterId(String mNoteChapterId) {
        this.mNoteChapterId = mNoteChapterId;
    }

    public String getmHomeWorkChapterTitle() {
        return mHomeWorkChapterTitle;
    }

    public void setmHomeWorkChapterTitle(String mHomeWorkChapterTitle) {
        this.mHomeWorkChapterTitle = mHomeWorkChapterTitle;
    }

    public String getmListNoteChapterId() {
        return mListNoteChapterId;
    }

    public void setmListNoteChapterId(String mListNoteChapterId) {
        this.mListNoteChapterId = mListNoteChapterId;
    }

    public String getmListNoteChapterTitle() {
        return mListNoteChapterTitle;
    }

    public void setmListNoteChapterTitle(String mListNoteChapterTitle) {
        this.mListNoteChapterTitle = mListNoteChapterTitle;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getmSignFlag() {
        return mSignFlag;
    }

    public void setmSignFlag(int mSignFlag) {
        this.mSignFlag = mSignFlag;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public void setmCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getmLuckBook() {
        return mLuckBook;
    }

    public void setmLuckBook(String mLuckBook) {
        this.mLuckBook = mLuckBook;
    }

    public int getPractice_pagerSize() {
        return practice_pagerSize;
    }

    public void setPractice_pagerSize(int practice_pagerSize) {
        this.practice_pagerSize = practice_pagerSize;
    }
}

