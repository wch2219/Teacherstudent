package com.jiaoshizige.teacherexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jiaoshizige.teacherexam.activity.LiveActivity;
import com.jiaoshizige.teacherexam.widgets.BaseMsgView;
import com.jiaoshizige.teacherexam.widgets.GiftMessage;
import com.jiaoshizige.teacherexam.widgets.LiveKit;
import com.jiaoshizige.teacherexam.widgets.UnknownMsgView;

import java.util.ArrayList;

import io.rong.imlib.model.MessageContent;

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<MessageContent> msgList;
    private Activity mActivity;
    public ChatListAdapter(Activity activity) {
        msgList = new ArrayList<>();
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseMsgView baseMsgView = (BaseMsgView) convertView;
        MessageContent msgContent = msgList.get(position);
//        Log.e("wocao",msgContent.getMentionedInfo().getMentionedContent());
        Class<? extends BaseMsgView> msgViewClass = LiveKit.getRegisterMessageView(msgContent.getClass());
        if (msgViewClass == null) {
            baseMsgView = new UnknownMsgView(parent.getContext());
        } else if (baseMsgView == null || baseMsgView.getClass() != msgViewClass) {
            try {
                baseMsgView = msgViewClass.getConstructor(Context.class).newInstance(parent.getContext());
            } catch (Exception e) {
                throw new RuntimeException("baseMsgView newInstance failed.");
            }
        }
        LiveActivity activity = (LiveActivity) mActivity;
        Log.e("sd",activity.getmGiftIdList().size()+"");
              GiftMessage giftMessage = null;
                if(msgContent instanceof GiftMessage){
                    giftMessage = (GiftMessage) msgContent;
                    for (int i = 0; i <  activity.getmGiftIdList().size();i++){
                        if(giftMessage.getType().equals( activity.getmGiftIdList().get(i))){
                            giftMessage.setContent("赠送了礼物"+"“"+activity.getmGiftNameList().get(i)+"”");
                        }
                    }
                    Log.e("sd",giftMessage.getContent()+giftMessage.getType());
                    baseMsgView.setContent(giftMessage);
                }else{
                    baseMsgView.setContent(msgContent);
                }
       /* GiftMessage giftMessage = null;
        if(msgContent instanceof GiftMessage){
             giftMessage = (GiftMessage) msgContent;
             if(giftMessage.getType().equals("6")){
                 giftMessage.setContent("赠送了礼物“小花”");
             }else if(giftMessage.getType().equals("4")){

             }else if(giftMessage.getType().equals("11")){

             }
            Log.e("sd",giftMessage.getContent()+giftMessage.getType());
            baseMsgView.setContent(giftMessage);
        }else{
            baseMsgView.setContent(msgContent);
        }*/

//        baseMsgView.setContent(msgContent);
        return baseMsgView;
    }

    public void addMessage(MessageContent msg) {
        msgList.add(msg);
    }
}
