package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.CityAdapter;
import com.jiaoshizige.teacherexam.adapter.ProvinceAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.model.provincesResponse;
import com.jiaoshizige.teacherexam.utils.AllUtils;
import com.jiaoshizige.teacherexam.utils.CharacterParser;
import com.jiaoshizige.teacherexam.utils.PinyinComparator;
import com.jiaoshizige.teacherexam.utils.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/31.
 * 选择考试地区
 */

public class ProvinceActivity extends BaseActivity {
    @BindView(R.id.province)
    ListView mProvinceSortListView;
    @BindView(R.id.sidrbar)
    SideBar mSideBar;
    @BindView(R.id.dialog)
    TextView mDialog;
    @BindView(R.id.city_list)
    ListView mCityList;
    private ProvinceAdapter mAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    private List<provincesResponse.mProvinces> mSourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator mPinyinComparator;

    private CityAdapter mCityAdapter;
    private String mProvince ="安徽省";
    private String mProvinceText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarTitle().setText("选择城市");
        setSubTitle().setText("");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.evaluation_item1;
    }


    protected void initView() {
     mCityAdapter = new CityAdapter(this);
        mCityList.setAdapter(mCityAdapter);
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
//        mProvinceSortListView = (ListView) findViewById(R.id.province);
//        mSideBar = (SideBar) findViewById(R.id.sidrbar);
//        mDialog = (TextView) findViewById(R.id.dialog);
        mSideBar.setTextView(mDialog);
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mProvinceSortListView.setSelection(position);
                    Log.e("Tag", position + "" + s);
                }
            }
        });
        mProvinceSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provincesResponse.mProvinces provinces = (provincesResponse.mProvinces) parent.getAdapter().getItem(position);
                mProvince = provinces.getProvinceName();
                mCityAdapter.setmList(((provincesResponse.mProvinces) parent.getAdapter().getItem(position)).getCitys());
                mCityAdapter.notifyDataSetChanged();
            }
        });
        mCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provincesResponse.mCitys citys = (provincesResponse.mCitys) parent.getAdapter().getItem(position);
                mProvinceText = (mProvince + citys.getCitysName());
//                mProvinceText = mProvince;
                Intent mIntent = new Intent();
                mIntent.putExtra("provinceText", mProvinceText);
                mIntent.putExtra("province", mProvince);
                setResult(RESULT_OK,mIntent);
                finish();
            }
        });
        getData();
    }

    private void getData() {
        String fileName = "json.json";
        String foodJson = AllUtils.getJson(this, fileName);
        provincesResponse provincesResponse = AllUtils.JsonToObject(foodJson, provincesResponse.class);
        mSourceDateList = filledData(provincesResponse.getProvinces());
        //根据a-z进行排序源数据
        Collections.sort(mSourceDateList, (Comparator<? super provincesResponse.mProvinces>) mPinyinComparator);
        //初始化适配器
        mAdapter = new ProvinceAdapter(this, mSourceDateList);
        //绑定适配器
        mProvinceSortListView.setAdapter(mAdapter);
      mCityAdapter.setmList(mSourceDateList.get(0).getCitys());
    }


    private List<provincesResponse.mProvinces> filledData(List<provincesResponse.mProvinces> list) {
        List<provincesResponse.mProvinces> mSortList = new ArrayList<provincesResponse.mProvinces>();
        for (int i = 0; i < list.size(); i++) {
            provincesResponse.mProvinces province = new provincesResponse.mProvinces();
            province.setProvinceName(list.get(i).getProvinceName());
            province.setCitys(list.get(i).getCitys());
            //汉字转换成拼音
            String pinyin = mCharacterParser.getSelling(list.get(i).getProvinceName());
            String sortString = pinyin.substring(0, 1).toUpperCase();//获取拼音首字母
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                province.setSortLetters(sortString.toUpperCase());
            } else {
                province.setSortLetters("#");
            }
            mSortList.add(province);
        }
        return mSortList;

    }
}
