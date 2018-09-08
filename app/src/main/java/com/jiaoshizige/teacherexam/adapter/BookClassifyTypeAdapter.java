package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.CatBookResponse;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class BookClassifyTypeAdapter extends MBaseAdapter<CatBookResponse.mChild> {

    public BookClassifyTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookClassifyAdapter.ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new BookClassifyAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_book_classfity, null);
            mHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(mHolder);
        } else {
            mHolder = (BookClassifyAdapter.ViewHolder) convertView.getTag();
        }
        mHolder.content.setText(mList.get(position).getCat_name());
        Log.e("***********//", clickTemp + "*********" + position);
        if (clickTemp == position) {
            mHolder.content.setBackgroundResource(R.drawable.purple);
            mHolder.content.setTextColor(mContext.getResources().getColor(R.color.purple4));
            mList.get(position).setStatus("1");
//

        } else {
            mHolder.content.setBackgroundResource(R.drawable.gray_shap_white_5);
            mHolder.content.setTextColor(mContext.getResources().getColor(R.color.text_color6));
//            mHolder.content.setChecked(false);
            mList.get(position).setStatus("0");

        }


        return convertView;

    }

    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }


    public static class ViewHolder {
        TextView content;
    }
}
