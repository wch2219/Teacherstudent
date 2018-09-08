package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.citywheel.CityPickerView;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/27 0027.
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.receiving_name)
    EditText mReceivingName;
    @BindView(R.id.receiving_phone)
    EditText mReceivingPhone;
    @BindView(R.id.receiving_address)
    TextView mReceivingAddress;
    @BindView(R.id.receiving_detail)
    EditText mReceivingDetail;
    @BindView(R.id.address_switch)
    ToggleButton mSwitch;
    @BindView(R.id.btn_commit)
    Button mCommit;
    private String mProvince, mCity, mDistrict;
    private String user_id;
    private String mName, mPhone, mDetail;
    private Intent intent;
    private String tag, address_id;//tag: 1 代表新建 0 代表编辑

    @Override
    protected int getLayoutId() {
        return R.layout.add_address_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");

        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = getIntent();
        if (getIntent().getStringExtra("tag") != null) {
            tag = intent.getStringExtra("tag");
            if (tag.equals("1")) {
                setToolbarTitle().setText("新建收货地址");
            } else {
                setToolbarTitle().setText("编辑收货地址");
                mReceivingName.setText(getIntent().getStringExtra("address_name"));
                mReceivingPhone.setText(getIntent().getStringExtra("address_phone"));
                mReceivingAddress.setText(getIntent().getStringExtra("address_province"));
                mReceivingDetail.setText(getIntent().getStringExtra("address_county"));
            }
        }
        mReceivingAddress.setOnClickListener(this);
        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receiving_address:
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddAddressActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                addressSelect();
                break;
            case R.id.btn_commit:
                if (tag.equals("1")) {
                    //tianjiapanduan
                    if (mSwitch.isChecked()) {
                        addAddress("1");
                    } else {
                        addAddress("0");
                    }
                } else {
                    if (mSwitch.isChecked()) {
                        editAddress("1");
                    } else {
                        editAddress("0");
                    }
                }

                break;
            case R.id.address_switch:
                if (mSwitch.isChecked()) {
                    editAddress("1");
                } else {
                    editAddress("0");
                }
                break;
        }

    }

    /**
     * 编辑收货地址
     */
    private void editAddress(String status) {
        if (intent.getStringExtra("address_id") != null) {
            address_id = intent.getStringExtra("address_id");
        }
        Log.e("********address_id", address_id);
        mName = mReceivingName.getText().toString().trim();
        mPhone = mReceivingPhone.getText().toString().trim();
        mDetail = mReceivingDetail.getText().toString().trim();
        if (TextUtils.isEmpty(mName)) {
            ToastUtil.showShortToast("请输入收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtil.showShortToast("请输入收货人电话");
            return;
        }
        if(!ToolUtils.isCellphone(mPhone)){
            ToastUtil.showShortToast("请输入正确的收货人电话");
            return;
        }
        if (TextUtils.isEmpty(mDetail)) {
            ToastUtil.showShortToast("请输入详细的收货地址");
            return;
        }
        if (TextUtils.isEmpty(mProvince)) {
            ToastUtil.showShortToast("请选择省份");
            return;
        }
        if (TextUtils.isEmpty(mCity)) {
            ToastUtil.showShortToast("请选择城市");
            return;
        }
        if (TextUtils.isEmpty(mDistrict)) {
            ToastUtil.showShortToast("请选择地区");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("name", mName);
        map.put("mobile", mPhone);
        map.put("province", mProvince);
        map.put("city", mCity);
        map.put("county", mDistrict);
        map.put("address", mDetail);
        map.put("status", status);
        map.put("id", address_id);
        Log.e("**********mapedit", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.EDIT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    /**
     * 添加地址
     */
    private void addAddress(String status) {
        mName = mReceivingName.getText().toString().trim();
        mPhone = mReceivingPhone.getText().toString().trim();
        mDetail = mReceivingDetail.getText().toString().trim();
        if (TextUtils.isEmpty(mName)) {
            ToastUtil.showShortToast("请输入收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtil.showShortToast("请输入收货人电话");
            return;
        }
        if(!ToolUtils.isCellphone(mPhone)){
            ToastUtil.showShortToast("请输入正确的收货人电话");
            return;
        }
        if (TextUtils.isEmpty(mDetail)) {
            ToastUtil.showShortToast("请输入详细的收货地址");
            return;
        }
        if (TextUtils.isEmpty(mProvince)) {
            ToastUtil.showShortToast("请选择省份");
            return;
        }
        if (TextUtils.isEmpty(mCity)) {
            ToastUtil.showShortToast("请选择城市");
            return;
        }
        if (TextUtils.isEmpty(mDistrict)) {
            ToastUtil.showShortToast("请选择地区");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("name", mName);
        map.put("mobile", mPhone);
        map.put("province", mProvince);
        map.put("city", mCity);
        map.put("county", mDistrict);
        map.put("address", mDetail);
        map.put("status", status);
        Log.e("**********mapaddd", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.ADD, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("***********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    EventBus.getDefault().post(new BusRefreshClass(AddressListActivity.class.getSimpleName()));
                    ToastUtil.showShortToast(result.getMessage());
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    /**
     * 地址选择
     */
    private void addressSelect() {
        CityConfig cityConfig = new CityConfig.Builder(AddAddressActivity.this)
                .title("选择地区")
                .titleBackgroundColor("#E9E9E9")
                .textSize(18)
                .titleTextColor("#585858")
                .textColor("#FF585858")
                .confirTextColor("#0000FF")
                .cancelTextColor("#000000")
                .province("北京")
                .city("北京")
                .district("朝阳区")
                .visibleItemsCount(5)
                .provinceCyclic(true)
                .cityCyclic(true)
                .showBackground(true)
                .districtCyclic(true)
                .itemPadding(5)
                .setCityInfoType(CityConfig.CityInfoType.BASE)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                .build();

        CityPickerView cityPicker = new CityPickerView(cityConfig);

        cityPicker.show();

        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                mProvince = province.getName();
                mCity = city.getName();
                mDistrict = district.getName();
                //返回结果
                mReceivingAddress.setText(province.getName() + "" + city.getName() + "" + district.getName());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("增加收货地址");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("增加收货地址");
        MobclickAgent.onPause(this);
    }
}
