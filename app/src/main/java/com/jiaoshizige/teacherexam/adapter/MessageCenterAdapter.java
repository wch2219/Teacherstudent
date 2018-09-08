package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LogisticsDetailActivity;
import com.jiaoshizige.teacherexam.activity.ReplayDetialActivity;
import com.jiaoshizige.teacherexam.yy.activity.YYHomeWorkDetailActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.model.PushDatailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.SwipeListLayout;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class MessageCenterAdapter extends MBaseAdapter<MessageCenterResponse.mData> {

    private Set<SwipeListLayout> sets = new HashSet();
    private String id;
    private String user_id;
    private Intent intent;
    private String message_id;

    public MessageCenterAdapter(Context context) {
        super(context);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = new Intent();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message_center_layout, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.mName = (TextView) convertView.findViewById(R.id.message_name);
            holder.mContent = (TextView) convertView.findViewById(R.id.message_content);
            holder.mRed = (TextView) convertView.findViewById(R.id.red_dot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mList.size() > 0) {
            Log.e("*************sie", mList.size() + "");
//            Glide.with(mContext).load(mList.get(position).getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(holder.image);
            holder.mName.setText(mList.get(position).getTitle());
            holder.mContent.setText(mList.get(position).getContent());
            switch (mList.get(position).getType()) {
                case 7:
                    holder.image.setImageResource(R.mipmap.logistics);
                    break;
                case 6:
                    holder.image.setImageResource(R.mipmap.answer);
                    break;
                case 5:
                    holder.image.setImageResource(R.mipmap.operation);
                    break;
            }

            if (mList.get(position).getIs_read().equals("0")) {
                holder.mRed.setVisibility(View.VISIBLE);
            } else {
                holder.mRed.setVisibility(View.GONE);
            }

            final SwipeListLayout sll_main = (SwipeListLayout) convertView
                    .findViewById(R.id.sll_main);
            TextView tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            LinearLayout mItem = (LinearLayout) convertView.findViewById(R.id.item_message);
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                    sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    MessageCenterResponse.mData str = mList.get(position);
                    mList.remove(position);
                    mList.add(0, str);


                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    mList.remove(position);
                    id = mList.get(position).getId();
                    deleteMessage();
                    notifyDataSetChanged();
                }
            });
//未完成
            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("*********getType*", mList.get(position).getType() + "");
                    message_id=mList.get(position).getId();
                    switch (mList.get(position).getType()) {
                        case 7:
                            intent.setClass(mContext, LogisticsDetailActivity.class);
                            intent.putExtra("courier",mList.get(position).getType_id());
                            mContext.startActivity(intent);
                            pushDetail(message_id);
                            break;
                        case 6:
                            intent.setClass(mContext, ReplayDetialActivity.class);
                            intent.putExtra("answer_id", mList.get(position).getType_id());
                            mContext.startActivity(intent);
                            pushDetail(message_id);
                            break;
                        case 5:
                            intent.setClass(mContext, YYHomeWorkDetailActivity.class);
                            intent.putExtra("work_id", mList.get(position).getType_id());
                            intent.putExtra("is_done", "1");
                            mContext.startActivity(intent);
                            pushDetail(message_id);
                            break;
                    }
                }
            });


        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView mName;
        TextView mContent;
        TextView mRed;

    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }

    /**
     * 删除推送信息
     */

    private void deleteMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", 1);
        map.put("id", id);
        map.put("type", 2);
        Log.e("***********demap", map.toString());
        Xutil.Post(ApiUrl.PUSHDELETE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }
        });
    }
    /**
     * 推送详情
     */
    private void pushDetail(String message_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message_id);
        map.put("user_id", user_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.Post(ApiUrl.PUSHDETAIL, map, new MyCallBack<PushDatailResponse>() {
            @Override
            public void onSuccess(PushDatailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
}
