package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * maxiao
 * popupwindow adapter.
 */

public class PopupwindowNewAdapter extends BaseAdapter {
    private List<NewCourseCatalogResponse.mCatalog> mDatas = new ArrayList<>();
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    private int flag;
    private LayoutInflater mInflater;
    private CallBack mCallBack;
    private AllPopupwindow allPopupwindow;
    private String mFlag;
    public PopupwindowNewAdapter(Context context, List<NewCourseCatalogResponse.mCatalog> mDatas, String flag) {
        this.mFlag = flag;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    public interface CallBack {
        public void click(View view);
    }

    public void onClick(View v) {
        mCallBack.click(v);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_all_popupwindow, null);
            viewHolder.mRadioButton = (RadioButton) convertView.findViewById(R.id.pop_radiobutton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mRadioButton.setText(mDatas.get(position).getTitle());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    GloableConstant.getInstance().setmDepartmentText(mDatas.get(position).getDepname());
                GloableConstant.getInstance().setmDepartmentId(mDatas.get(position).getId());*/
                if(mFlag.equals("couesetype")){
                    GloableConstant.getInstance().setType(mDatas.get(position).getTitle());
                }else if(mFlag.equals("couesesort")){
                    GloableConstant.getInstance().setSort(mDatas.get(position).getTitle());
                }else if(mFlag.equals("booksort")){
                    GloableConstant.getInstance().setBooksort(mDatas.get(position).getTitle());
                }else if(mFlag.equals("question")){
                    GloableConstant.getInstance().setmQuestionTyope(mDatas.get(position).getTitle());
                }else if(mFlag.equals("mynote")){
                    GloableConstant.getInstance().setmNoteChapterTitle(mDatas.get(position).getTitle());
                    GloableConstant.getInstance().setMuNoteChapter(mDatas.get(position).getId());
                }else if(mFlag.equals("addnote")){
                    GloableConstant.getInstance().setmAddNoteChapterId(mDatas.get(position).getId());
                    GloableConstant.getInstance().setmAddNoteChapterTitle(mDatas.get(position).getTitle());
                    Log.e("WIIWI",mDatas.get(position).getTitle());
                }
                if( mFlag.equals("homework")){
                    GloableConstant.getInstance().setmHomeWorkChapterId(mDatas.get(position).getId());
                    GloableConstant.getInstance().setmHomeWorkChapterTitle(mDatas.get(position).getTitle());
                }else if(mFlag.equals("note")){
                    GloableConstant.getInstance().setmListNoteChapterId(mDatas.get(position).getId());
                    GloableConstant.getInstance().setmListNoteChapterTitle(mDatas.get(position).getTitle());
                }
                flag = position;
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), finalViewHolder.mRadioButton.isChecked());
                notifyDataSetChanged();
                allPopupwindow.dismiss();
            }
        });
        boolean res = false;
        if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else {
            res = true;
        }
        viewHolder.mRadioButton.setChecked(res);
        return convertView;
    }

    public class ViewHolder {
        RadioButton mRadioButton;
    }
}
