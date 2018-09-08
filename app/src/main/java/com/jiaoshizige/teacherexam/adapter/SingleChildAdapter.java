package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.ArrayList;

/**
 * 二级菜单适配器
 * Created by Administrator on 2016/7/18.
 */
public class SingleChildAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<NewCourseCatalogResponse.mCatalog> mDatas;
    private NewCourseCatalogResponse.mCourseList mParentsData;
    int mPosition;
     ExpandableListView mChildListView;
    //////////////////////
    private String state = "";
    private static final String DOWNLOADED = "已下载";
    private static final String DOWNLOADING = "正在下载";
    private static final String PAUSEED = "暂停下载";
    private static final String WAITED = "等待下载";
    private int progress = 0;
    private int bitrate = 1;
    private String vid = "c538856dde2600e0096215c16592d4d3_c";
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    public SingleChildAdapter(Context context, NewCourseCatalogResponse.mCourseList parent_data, ArrayList<NewCourseCatalogResponse.mCatalog> data, int pos,ExpandableListView childListView){
        this.mContext = context;
        this.mDatas = data;
        this.mPosition = pos;
        this.mParentsData = parent_data;
        this.mChildListView = childListView;
    }
    @Override
    public int getGroupCount() {
        return mDatas!= null?mDatas.size():0;
    }

    @Override
    public int getChildrenCount(int childPosition) {
        return mDatas.get(childPosition).getSon()!=null
                ?mDatas.get(childPosition).getSon().size():0;
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mDatas.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return mDatas.get(parentPosition).getSon().get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 主菜单布局
     * @param parentPosition
     * @param isExpandabled  是否展开
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(final int parentPosition, boolean isExpandabled, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.child_adapter,null);
            holder = new ViewHolder();
//            holder.mChildPosition = (TextView)view.findViewById(R.id.child_position);
            holder.mImg = (ImageView)view.findViewById(R.id.back_img);
            holder.mChildTitle = (TextView)view.findViewById(R.id.child_title);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if(isExpandabled){
            holder.mImg.setImageResource(R.mipmap.cehua_arrow_small_up);
        }else{
            holder.mImg.setImageResource(R.mipmap.cehua_arrow_small_down);
        }
        holder.mChildTitle.setText(mDatas.get(parentPosition).getTitle());

        return view;
    }
    class ViewHolder {

        private TextView mChildPosition;
        private ImageView mImg;
        private TextView mChildTitle;
    }
    /**
     * 子菜单布局
     * @param parentPosition
     * @param childPosition
     * @param isExpandabled
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isExpandabled, View view, ViewGroup viewGroup) {


        ChildHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.child_child, null);
            holder = new ChildHolder();
            holder.mContentTitle = (TextView) view.findViewById(R.id.content_title);
            holder.mContentFlag = (TextView) view.findViewById(R.id.content_flag);
            holder.mContentType = (TextView) view.findViewById(R.id.content_type);
                    view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        //1视频2练习3图文4直播
        if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("1")){
            if(mParentsData.getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.video1_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.video1));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
            }
        }else if(mDatas.get(parentPosition).getSon().get(childPosition).equals("2")){
            if(mParentsData.getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_type_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_type));
            }

        }else if(mDatas.get(parentPosition).getSon().get(childPosition).equals("3")){
            if(mParentsData.getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_flag_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_flag));
            }

        }else if(mDatas.get(parentPosition).getSon().get(childPosition).equals("4")){
            if(mParentsData.getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_type_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_type));
            }

        }
        holder.mContentTitle.setText(mDatas.get(parentPosition).getSon().get(childPosition).getTitle());
        holder.mContentFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShortToast(mDatas.get(parentPosition).getTitle()+""+mDatas.get(parentPosition).getSon().get(childPosition).getTitle()+mParentsData.getName());
            }
        });

        return view;
    }
    public ExpandableListView getExpandableListView() {
        ExpandableListView mExpandableListView = new ExpandableListView(
                mContext);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                .getResources().getDimension(
                        R.dimen.parent_list_height));
        mExpandableListView.setLayoutParams(lp);
        mExpandableListView.setDividerHeight(0);// 取消group项的分割线
        mExpandableListView.setChildDivider(null);// 取消child项的分割线
        mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
        return mExpandableListView;
    }
    class ChildHolder {

        private TextView mContentTitle;
        private TextView mContentFlag;
        private TextView mContentType;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    SingleMainAdapter.OnExpandClickListener mListener;
    public void setOnChildListener(SingleMainAdapter.OnExpandClickListener listener){
        this.mListener = listener;
    }
}
