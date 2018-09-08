package com.jiaoshizige.teacherexam.mycourse.fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.mycourse.adapter.CeilingAdapter;
import com.jiaoshizige.teacherexam.mycourse.bean.CeilingBean;
import com.jiaoshizige.teacherexam.mycourse.bean.DataUtil;
import com.jiaoshizige.teacherexam.mycourse.listener.PowerGroupListener;
import com.jiaoshizige.teacherexam.mycourse.utils.Util;
import com.jiaoshizige.teacherexam.mycourse.weiget.SectionDecoration;
import com.jiaoshizige.teacherexam.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.jiaoshizige.teacherexam.utils.SDViewUtil.inflate;

/**
 * Created by Administrator on 2018/8/21.
 */

public class CourseCatalogFragment extends MBaseFragment implements CeilingAdapter.OnItemClickListener  {
    RecyclerView mRecyclerView;
    CeilingAdapter mAdapter;
    /**
     * 该集合用来盛放所有item对应悬浮栏的内容
     */
    private List<DataUtil> dataList;
    /**
     * 用来盛放接口返回的数据
     **/
    private List<CeilingBean.BaseBean> beanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_catalog, container, false);
        mRecyclerView =  view.findViewById(R.id.rv_list);
        initView();
        return view;
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {

    }
    /**
     * 制造虚拟数据
     */
    private void initData() {
        beanList=new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            CeilingBean.ResultBean.FirstBean bean = new CeilingBean.ResultBean.FirstBean();
            bean.setgName("类别一");
            bean.setUname("u1");
            beanList.add(bean);
        }
        for (int i = 0; i < 19; i++) {
            CeilingBean.ResultBean.SecondBean bean = new CeilingBean.ResultBean.SecondBean();
            bean.setgName("类别二");
            bean.setUname("u2");
            beanList.add(bean);
        }
        for (int i = 0; i < 17; i++) {
            CeilingBean.ResultBean.ThirdBean bean = new CeilingBean.ResultBean.ThirdBean();
            bean.setgName("类别三");
            bean.setUname("u3");
            beanList.add(bean);
        }

        setPullAction();
    }
    /**
     * 给子item添加父布局
     */
    private void setPullAction() {
        dataList = new ArrayList<>();
        if(beanList!=null && beanList.size()>0){
            for (int i=0;i<beanList.size();i++)
            {
                DataUtil data = new DataUtil();
                String gName = beanList.get(i).getgName();
                if("类别一".equals(gName))
                {
                    data.setName("最新类别1");
                    data.setIcon(R.mipmap.again_icon);
                }else if("类别二".equals(gName)){
                    data.setName("最新类别2");
                    data.setIcon(R.mipmap.ban);
                }else if("类别三".equals(gName)){
                    data.setName("最新类别3");
                    data.setIcon(R.mipmap.again_icon);
                }
                dataList.add(data);
            }
        }
    }
    /**
     * 初始化RecyclerView
     */
    private void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        mAdapter = new CeilingAdapter(getActivity(), beanList);
        mRecyclerView.setAdapter(mAdapter);
        //添加悬浮布局
        initDecoration();
        mAdapter.setOnItemClickListener(this);
    }
    /**
     * 添加悬浮布局
     */
    private void initDecoration() {
        SectionDecoration decoration = SectionDecoration.Builder
                .init(new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (dataList.size() > position) {
                            return dataList.get(position).getName();
                        }
                        return null;
                    }
                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (dataList.size() > position) {
                            View view = inflate(R.layout.test_item_group, null, false);
                            ((TextView) view.findViewById(R.id.tv)).setText(dataList.get(position).getName());
                            ((ImageView) view.findViewById(R.id.iv)).setImageResource(dataList.get(position).getIcon());
                            return view;
                        } else {
                            return null;
                        }
                    }
                })
                //设置高度
                .setGroupHeight(Util.dip2px(getActivity(), 40))
                .build();
        mRecyclerView.addItemDecoration(decoration);
    }
    /**
     * item的单击事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "你点击的位置是"+position, Toast.LENGTH_SHORT).show();
    }
}
