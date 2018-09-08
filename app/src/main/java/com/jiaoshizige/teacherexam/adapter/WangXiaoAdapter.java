package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.MessageDetailActivity;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.utils.SwipeListLayout;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class WangXiaoAdapter extends MBaseAdapter<MessageCenterResponse.mData> {
    private Set<SwipeListLayout> sets = new HashSet();
    private String id;
    private String user_id;

    public WangXiaoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_activity_layout, null);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mContent = (TextView) convertView.findViewById(R.id.content);
            holder.mImage = (WebView) convertView.findViewById(R.id.image);
            holder.mTime = (TextView) convertView.findViewById(R.id.time);
            holder.mItem = (LinearLayout) convertView.findViewById(R.id.message_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mList.size() > 0) {
            holder.mTitle.setText(mList.get(position).getTitle());
            holder.mContent.setText(mList.get(position).getContent());
            holder.mTime.setText(mList.get(position).getCreated_at());
//            URLImageParser imageGetter = new URLImageParser(holder.mImage);
//            holder.mImage.setText(Html.fromHtml(mList.get(position).getDetail(), imageGetter, null));

            WebSettings webSettings = holder.mImage.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setJavaScriptEnabled(true);
            String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>"+mList.get(position).getDetail();
            holder.mImage.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);

            final SwipeListLayout sll_main = (SwipeListLayout) convertView
                    .findViewById(R.id.sll_main);
            TextView tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            LinearLayout mLinearLayout = (LinearLayout) convertView.findViewById(R.id.message_item);
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    MessageCenterResponse.mData str = mList.get(position);
                    mList.remove(position);
                    mList.add(0, str);
                    id = mList.get(position).getId();
                    deleteMessage();
                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(mContext,MessageDetailActivity.class);
                    intent.putExtra("_id",mList.get(position).getId());
                   mContext.startActivity(intent);
                }
            });

        }

        return convertView;
    }

    class ViewHolder {
        TextView mTitle;
        TextView mContent;
        WebView mImage;
        TextView mTime;
        LinearLayout mItem;
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
        map.put("user_id", user_id);
        map.put("id", id);
        map.put("type", 2);
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
}
