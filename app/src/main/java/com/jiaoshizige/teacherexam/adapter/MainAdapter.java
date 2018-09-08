package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.util.Log;
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
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MainAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<NewCourseCatalogResponse.mCourseList> mData;
//    NestedScrollView mNesteSCrollView;
    ViewHolder holder = null;
    ChildAdapter adapter;
    private String mGroudId;//班级id
    public MainAdapter(Context context,List<NewCourseCatalogResponse.mCourseList> data,String courid){
        this.mContext = context;
        this.mData = data;
        this.mGroudId = courid;
//        this.mNesteSCrollView = mNesteSCrollView;
    }
    @Override
    public int getGroupCount() {
        Log.e("TAG",mData.size()+"wwww");
        return mData != null?mData.size():0;
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return mData.get(parentPosition).getCatalog().size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mData.get(parentPosition);
    }

    @Override
    public NewCourseCatalogResponse.mCatalog getChild(int parentPosition, int childPosition) {
        return mData.get(parentPosition).getCatalog().get(childPosition);
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
     *  第一级菜单适配器布局
     * @param parentPosition
     * @param isExpanded
     *
     * @param convertView
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(int parentPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.parent_group_item, null);
            holder = new ViewHolder();
            holder.upImg = (ImageView) convertView.findViewById(R.id.back_img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //区分箭头往上还是
        if(isExpanded){
            holder.upImg.setImageResource(R.mipmap.cehua_arrow_small_up);
        }else{
            holder.upImg.setImageResource(R.mipmap.cehua_arrow_small_down);
        }
        holder.title.setText(mData.get(parentPosition).getName());
        return convertView;
    }
    class ViewHolder{
        private TextView title;
        private ImageView upImg;
    }
    public ExpandableListView getExpandableListView() {
        ExpandableListView mExpandableListView = new ExpandableListView(
                mContext);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
//                .getResources().getDimension(
//                        R.dimen.parent_list_height));
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ( ViewGroup.LayoutParams.MATCH_PARENT));
        mExpandableListView.setLayoutParams(lp);
        mExpandableListView.setDividerHeight(0);// 取消group项的分割线
        mExpandableListView.setChildDivider(null);// 取消child项的分割线
        mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
        return mExpandableListView;
    }
    /**
     * 第二级菜单式配
     * @param parentPosition
     * @param childPosition
     * @param isExpanded
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        final ExpandableListView childListView = getExpandableListView();
        //获取子菜单的数据
        final ArrayList<NewCourseCatalogResponse.mCatalog> childData = new ArrayList<NewCourseCatalogResponse.mCatalog>();
        final NewCourseCatalogResponse.mCatalog bean = getChild(parentPosition,childPosition);
        childData.add(bean);
        adapter= new ChildAdapter(mContext,mData,childData,parentPosition,mGroudId);
        childListView.setAdapter(adapter);

        /**
         * 点击最小级菜单，调用该方法
         * */
        childListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupIndex, int childIndex, long arg4) {
                if(mListener != null){
                    mListener.onclick(parentPosition,childPosition, childIndex);
                    //点击三级菜单，跳转到编辑菜单界面
                /*    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra("content","你点的位置是:  "+"parentPosition>>"+parentPosition+
                    "childPosition>>"+childPosition+"childIndex>>"+childIndex);
                    mContext.startActivity(intent);*/
                    ToastUtil.showShortToast("sanji");
                }
                return false;
            }
        });
        /**
         *子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度=
         * （子ExpandableListView的child数量 + 1 ）* 每一项的高度
         * */
        childListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.e("xxx",groupPosition+"onGroupExpand>>");
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (bean.getSon().size() + 1)* (int) mContext
                        .getResources().getDimension(R.dimen.parent_list_height));
                Log.e("SSSD",bean.getSon().size()+"ss");
//                ViewGroup.LayoutParams layoutParams1 = mNesteSCrollView.getLayoutParams();
//                Log.e("LLs",layoutParams1.height+"");
//                layoutParams1.height += (bean.getSon().size() + 1)* (int) mContext
//                        .getResources().getDimension(R.dimen.parent_list_height);
//                Log.e("LL",layoutParams1.height+"");
                childListView.setLayoutParams(lp);
                adapter.notifyDataSetChanged();
//                mNesteSCrollView.setLayoutParams(layoutParams1);
            }
        });
        /**
         *子ExpandableListView关闭时，此时只剩下group这一项，
         * 所以子ExpandableListView的总高度即为一项的高度
         * */
        childListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.e("xxx",groupPosition+">>onGroupCollapse");
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                        .getResources().getDimension(R.dimen.parent_list_height));
                Log.e("SSS222D",bean.getSon().size()+"ss");
                childListView.setLayoutParams(lp);
                holder.upImg.setImageResource(R.mipmap.cehua_arrow_small_up);
                adapter.notifyDataSetChanged();
            }
        });
        /**
         * 在这里对二级菜单的点击事件进行操作
         */
        childListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int Position, long id) {
//                if(isClick){
//                    holder.mUpImg.setImageResource(R.drawable.dowm);
//                    isClick = false;
//                }else{
//                    holder.mUpImg.setImageResource(R.drawable.up);
//                    isClick = true;
//                }
                Log.e("Xxx","恭喜你,点击了"+parentPosition+"childpos>>>"+childPosition);
                return false;
            }
        });
        return childListView;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /*接口回调*/
    public interface OnExpandClickListener{
        void onclick(int parentPosition, int childPosition, int childIndex);
    }
    OnExpandClickListener mListener;
    public void setOnChildListener(OnExpandClickListener listener){
        this.mListener = listener;
    }
}
