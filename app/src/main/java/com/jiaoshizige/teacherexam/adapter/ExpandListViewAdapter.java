package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.model.RecordModel;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;


public class ExpandListViewAdapter extends BaseExpandableListAdapter {
    //    private List<CourseCatalogResponse.mCatalog> mListData;
    private List<List<PolyvDownloadInfo>> childArray;
    private List<RecordModel> mListData;
    //    private List<List<CourseCatalogResponse.mSon>> childArray;
    private LayoutInflater mInflate;
    private Context context;
    private boolean isShow = true;//是否显示编辑/完成

    private static PolyvDownloadSQLiteHelper downloadSQLiteHelper;

    private String vid;
    private List<String> list;

//    public ExpandListViewAdapter(List<CourseCatalogResponse.mData> mListData, Context context) {
//        this.mListData = mListData;
//        this.context = context;
//        this.mInflate = LayoutInflater.from(context);
//    }

//    public ExpandListViewAdapter(Context context,  List<List<CourseCatalogResponse.mSon>> childArray, List<CourseCatalogResponse.mCatalog> mListData) {
//        this.context = context;
//        this.childArray = childArray;
//        this.mListData = mListData;
//        this.mInflate = LayoutInflater.from(context);
//        downloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(this.context);
//    }


    public ExpandListViewAdapter(List<RecordModel> mListData, List<List<PolyvDownloadInfo>> childArray, Context context) {
        this.mListData = mListData;
        this.childArray = childArray;
        this.context = context;
        this.mInflate = LayoutInflater.from(context);
        downloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(this.context);

        list = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return mListData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 删除任务
     */
//    public void deleteTask(int position) {
//        PolyvDownloadInfo downloadInfo = lists.remove(position);
//        //移除任务
//        PolyvDownloader downloader = PolyvDownloaderManager.clearPolyvDownload(downloadInfo.getVid(), downloadInfo.getBitrate());
//        //删除文件
//        downloader.deleteVideo();
//        //移除数据库的下载信息
//        downloadSQLiteHelper.delete(downloadInfo);
//        notifyDataSetChanged();
//    }


    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TitleHolder holder = null;
        if (convertView == null) {
            holder = new TitleHolder();
            convertView = mInflate.inflate(R.layout.item_down_load, parent, false);
            holder.tvTitle = ((TextView) convertView.findViewById(R.id.tv));
            holder.cbTitle = ((CheckBox) convertView.findViewById(R.id.cb));
            holder.imTitle = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (TitleHolder) convertView.getTag();
        }
        Log.e("sdsds", mListData.get(groupPosition).getChapter());
        holder.tvTitle.setText(mListData.get(groupPosition).getChapter());
        //判断是否已经打开列表
        if (isExpanded) {
            holder.imTitle.setImageResource(R.mipmap.cehua_arrow_small_up);
        } else {
            holder.imTitle.setImageResource(R.mipmap.cehua_arrow_small_down);
        }

        final TitleHolder finalHolder = holder;
        finalHolder.cbTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = finalHolder.cbTitle.isChecked();
                Log.d("bigname", "onCheckedChanged: second:" + groupPosition + "," + isChecked);
                mListData.get(groupPosition).setCheck(isChecked);
                for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
//                    CourseCatalogResponse.mSon mSon = mListData.get(groupPosition).getSon().get(i);
//                    PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
//                    info.setCheck(isChecked);
                    childArray.get(groupPosition).get(i).setCheck(isChecked);
                }

                if (mListData.get(groupPosition).isCheck()) {
                    for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
                        PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
                        info.setCheck(true);
//                vid = info.getVid();
//                list.add(vid);
                        if (childArray.get(groupPosition).get(i).isCheck()) {
                            list.add(childArray.get(groupPosition).get(i).getVid());
                        }
                    }

                    Log.e("******listvid", list.size() + "********" + mListData.get(groupPosition).getmPolyvDownloadInfo().size());
                } else {
                    for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
                        PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
                        if (info.isCheck() == true) {
                            info.setCheck(true);
                        } else {
                            info.setCheck(false);
                        }
                        list.clear();
                    }
                }
                notifyDataSetChanged();
            }
        });

        if (mListData.get(groupPosition).isCheck()) {
            for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
                PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
                info.setCheck(true);
//                vid = info.getVid();
//                list.add(vid);
                if (childArray.get(groupPosition).get(i).isCheck()) {
                    list.add(childArray.get(groupPosition).get(i).getVid());
                }
            }

            Log.e("******listvid", list.size() + "********" + mListData.get(groupPosition).getmPolyvDownloadInfo().size());
        } else {
            if (GloableConstant.getInstance().getAll().equals("1")) {
                for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
                    PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
//                    if (info.isCheck() == true) {
//                        info.setCheck(true);
//                    } else {
//                        info.setCheck(false);
//                    }
//                    list.clear();
                    info.setCheck(false);
                }
            }else {
                for (int i = 0; i < mListData.get(groupPosition).getmPolyvDownloadInfo().size(); i++) {
                    PolyvDownloadInfo info = mListData.get(groupPosition).getmPolyvDownloadInfo().get(i);
                    if (info.isCheck() == true) {
                        info.setCheck(true);
                    } else {
                        info.setCheck(false);
                    }
                    list.clear();
                }
            }

        }
        finalHolder.cbTitle.setChecked(mListData.get(groupPosition).isCheck());
        if (isShow) {
            finalHolder.cbTitle.setVisibility(View.GONE);
        } else {
            finalHolder.cbTitle.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = mInflate.inflate(R.layout.item_down_load_child, parent, false);
            holder.tvChild = ((TextView) convertView.findViewById(R.id.tv));
            holder.cbChild = ((CheckBox) convertView.findViewById(R.id.cb));
            holder.ll = ((LinearLayout) convertView.findViewById(R.id.ll));
            holder.textChild = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.tvChild.setText(childArray.get(groupPosition).get(childPosition).getSection_name());
        holder.cbChild.setChecked(childArray.get(groupPosition).get(childPosition).isCheck());
        String size = ToolUtils.FormatTwoPoint(String.valueOf(((childArray.get(groupPosition).get(childPosition).getFilesize() / 1024) / 1024)));
        holder.textChild.setText(size + "M");
        Log.e("***********check", childArray.get(groupPosition).get(childPosition).isCheck() + "");
        final ChildHolder childHolder = holder;
        childHolder.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = childHolder.cbChild.isChecked();
                Log.d("bigname", "onCheckedChanged: third:" + groupPosition + "," + isChecked);
                vid = childArray.get(groupPosition).get(childPosition).getVid();
                list.add(vid);
                Log.e("******listvid", list.size() + "********" + vid + "*********childArray" + childArray.get(groupPosition).size());
                mListData.get(groupPosition).getmPolyvDownloadInfo().get(childPosition).setCheck(isChecked);
                boolean isOneParentAllChildIsChecked = dealOneParentAllChildIsChecked(groupPosition);
                Log.e("*******ChildIsChecked", isOneParentAllChildIsChecked + "");
                if (isOneParentAllChildIsChecked) {
                    mListData.get(groupPosition).setCheck(true);
                    notifyDataSetChanged();
                }
                if (isChecked == false) {
                    mListData.get(groupPosition).setCheck(false);
                    notifyDataSetChanged();
                }

            }
        });
        if (isShow) {
            childHolder.cbChild.setVisibility(View.GONE);
        } else {
            childHolder.cbChild.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    public boolean dealOneParentAllChildIsChecked(int groupPosition) {
        List<PolyvDownloadInfo> infos = childArray.get(groupPosition);
        for (int j = 0; j < infos.size(); j++) {
            PolyvDownloadInfo info = infos.get(j);
            if (!info.isCheck()) {
                return false;
            }
        }
        return true;
    }

    public void removeAll() {
        for (int i = mListData.size() - 1; i >= 0; i--) {
            RecordModel recordModel = mListData.get(i);
            if (recordModel.isCheck()) {
                mListData.remove(i);
                childArray.remove(i);
                notifyDataSetChanged();
                for (int j = 0; j < list.size(); j++) {
                    Log.e("**********vididid", list.get(j));
                    downloadSQLiteHelper.deletecontent(list.get(j));
                }

            } else {
                List<PolyvDownloadInfo> list = childArray.get(i);
                for (int j = list.size() - 1; j >= 0; j--) {
                    PolyvDownloadInfo info = list.get(j);
                    if (info.isCheck()) {
                        list.remove(j);
                        Log.e("************vid", info.getVid());
                        downloadSQLiteHelper.deletecontent(info.getVid());
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


    class TitleHolder {
        CheckBox cbTitle;
        TextView tvTitle;
        ImageView imTitle;
    }


    class ChildHolder {
        CheckBox cbChild;
        TextView tvChild;
        TextView titleChild;
        TextView textChild;
        LinearLayout ll;
    }
}
