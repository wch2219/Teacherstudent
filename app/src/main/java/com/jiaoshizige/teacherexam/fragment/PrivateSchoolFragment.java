package com.jiaoshizige.teacherexam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guideview.GuideView;
import com.guideview.GuideViewHelper;
import com.guideview.LightType;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.NewGiftPackActivity;
import com.jiaoshizige.teacherexam.activity.SearchActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.widgets.CenterCenterStyle;
import com.jiaoshizige.teacherexam.widgets.NoviceParkDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/27.
 * 选课   班级改名为课程  原课程去掉
 */

public class PrivateSchoolFragment extends MBaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.private_school_tab)
    RadioGroup mPrivateSchoolTab;
    @BindView(R.id.search_img)
    ImageView mSearch;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.container_ly)
    LinearLayout mContainer;
    FragmentHolder holder = null;
    @BindView(R.id.class_radio)
    RadioButton mClassRadio;
    @BindView(R.id.book_radio)
    RadioButton mBookRadio;
    Unbinder unbinder;
    private GuideViewHelper helper;
    private int tabFlag = 0;
    private boolean isFirst = false;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.private_school_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        initFragments();
        mPrivateSchoolTab.setOnCheckedChangeListener(this);
        isFirst = true;
       /* if(!Sphelper.getBoolean(getActivity(),"hadexam","isexam") && Sphelper.getBoolean(getActivity(),"hadexam","isexam") != null){
            showDialog();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Sphelper.getString(getActivity(), "ISFIRSTSCHOOL", "isfirstschool").equals("1")) {
            stop();
        }
        if (isFirst) {
            mPrivateSchoolTab.check(R.id.class_radio);
        } else {
            if (!TextUtils.isEmpty(GloableConstant.getInstance().getmLuckBook())) {
                if (GloableConstant.getInstance().getmLuckBook().equals("1")) {
                    changeBook();
                } else if (GloableConstant.getInstance().getmLuckBook().equals("0")) {
                    changeClass();
                }
            }

        }

        MobclickAgent.onPageStart("选课");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!TextUtils.isEmpty(GloableConstant.getInstance().getmLuckBook())) {
                if (GloableConstant.getInstance().getmLuckBook().equals("1")) {
                    changeBook();
                } else if (GloableConstant.getInstance().getmLuckBook().equals("0")) {
                    changeClass();
                }
            }
//        stop();
            MobclickAgent.onPageStart("选课");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选课");
        isFirst = false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        String oldTag = (String) group.getTag();
        switch (checkedId) {
            case R.id.class_radio:
                holder = mFragments[0];
                mSearch.setVisibility(View.VISIBLE);
                tabFlag = 1;
                break;
    /*        case R.id.course_radio:
                holder = mFragments[1];
                mSearch.setVisibility(View.VISIBLE);

                break;*/
            case R.id.book_radio:
                holder = mFragments[1];
                mSearch.setVisibility(View.VISIBLE);
                tabFlag = 2;
                break;
            default:
                holder = mFragments[0];
                break;
        }
        if (holder.tag.equals(oldTag))
            return;
        try {
            group.setTag(holder.tag);
            if (holder.fragment == null)
                holder.fragment = holder.cls.newInstance();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment from = fm.findFragmentByTag(oldTag);
            Fragment to = holder.fragment;
            if (from == null) {
                fm.beginTransaction().add(R.id.container_ly, to, holder.tag)
                        .commitAllowingStateLoss();
                return;
            }
            if (!to.isAdded())
                fm.beginTransaction().hide(from)
                        .add(R.id.container_ly, to, holder.tag)
                        .commitAllowingStateLoss();
            else
                fm.beginTransaction().hide(from).show(to)
                        .commitAllowingStateLoss();

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        isFirst = false;
    }

    private static class FragmentHolder {
        public Class<? extends Fragment> cls;
        public String tag;
        public Fragment fragment;
    }

    private FragmentHolder[] mFragments = {new FragmentHolder(),
            new FragmentHolder(), new FragmentHolder()};

    private void initFragments() {
//        mFragments[0].cls = PrivateSchoolClassFragment.class;
//        mFragments[0].tag = "PrivateSchoolClassFragment";
        mFragments[0].cls = NewPrivateCourseFragmentTwo.class;
        mFragments[0].tag = "NewPrivateCourseFragmentTwo";
   /*     mFragments[1].cls = PrivateSchoolCourseFragment.class;
        mFragments[1].tag = "PrivateSchoolCourseFragment";*/
        mFragments[1].cls = PrivateSchoolBookFragment.class;
        mFragments[1].tag = "PrivateSchoolBookFragment";
    }

    @OnClick(R.id.search_img)//1.是課程 2.是圖書　
    public void onSearchClick() {
        if (tabFlag == 1) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SearchActivity.class);
            intent.putExtra("type", "1");
            startActivityForResult(intent, 1);
        } else if (tabFlag == 2) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SearchActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("book_name", GloableConstant.getInstance().getSearchBookTag());
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().get("result") != null) {
                    holder = mFragments[1];
                    onCheckedChanged(mPrivateSchoolTab, R.id.course_radio);
//                    GloableConstant.getInstance().setmCourseName((String) data.getExtras().get("result"));
                    GloableConstant.getInstance().setSearchCourseTag((String) data.getExtras().get("result"));

                    Log.d("******course", (String) data.getExtras().get("result"));
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().get("result") != null) {
                    holder = mFragments[2];
                    onCheckedChanged(mPrivateSchoolTab, R.id.book_radio);
//                    GloableConstant.getInstance().setmBookName((String) data.getExtras().get("result"));
                    GloableConstant.getInstance().setSearchBookTag((String) data.getExtras().get("result"));
                    Log.d("******book", (String) data.getExtras().get("result"));

                }
            }
        }
    }

    public void showDialog() {
        NoviceParkDialog.Builder builder = new NoviceParkDialog.Builder(getActivity());
        builder.setLookMoreButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("tag", "0");
                intent.setClass(getActivity(), NewGiftPackActivity.class);
                startActivity(intent);
                Sphelper.save(getActivity(), "hasexam", "isexam", "1");
                dialog.dismiss();
            }
        });
        builder.setCloseButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showOnCreate() {
        View deco_view2 = LayoutInflater.from(getActivity()).inflate(R.layout.newpersonguide_layout_three, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        View deco_view3 = LayoutInflater.from(getActivity()).inflate(R.layout.newpersonguide_layout_four, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        helper = new GuideViewHelper(getActivity())
                .addView(mSearch, new CenterCenterStyle(deco_view2, 20))
                .type(LightType.Rectangle)
                .autoNext()
                .addView(mView, new CenterCenterStyle(deco_view3, 10))
                .onDismiss(new GuideView.OnDismissListener() {
                    @Override
                    public void dismiss() {
//                        MainActivity activity = (MainActivity) getActivity();
//                        activity.changeMainTab();
                    }
                });
        helper.show();
        Sphelper.save(getActivity(), "ISFIRSTSCHOOL", "isfirstschool", "1");
    }

    public void stop() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 1) {
                showOnCreate();
            }
        }
    };

    public void changeBook() {
        mBookRadio.setChecked(true);
        GloableConstant.getInstance().setmLuckBook("");
    }


    public void changeClass() {
        mClassRadio.setChecked(true);
    }
}
