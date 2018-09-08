package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class TextMsgView extends BaseMsgView {

    private TextView username;
    private TextView msgText;
    private ImageView mPhoto;

    public TextMsgView(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_text_view, this);
        username = (TextView) view.findViewById(R.id.username);
        msgText = (TextView) view.findViewById(R.id.msg_text);
        mPhoto = (ImageView) view.findViewById(R.id.photo);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        TextMessage msg = (TextMessage) msgContent;
        //http://rongcloud-web.qiniudn.com/b4612816d27ce158661df0c8b320ea64
//        Glide.with(getContext()).load("http://rongcloud-web.qiniudn.com/b4612816d27ce158661df0c8b320ea64").apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
        if (msgContent.getUserInfo().getPortraitUri()!=null&&!msgContent.getUserInfo().getPortraitUri().equals("")){
            Glide.with(getContext()).load(msgContent.getUserInfo().getPortraitUri()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
            username.setText(msgContent.getUserInfo().getName()+"：");
            msgText.setText(msg.getContent());
            ToolUtils.setTVColor((""+msgContent.getUserInfo().getName()+"："+msg.getContent()),"","：", ContextCompat.getColor(getContext(),R.color.text_color6),msgText);
//        username.setText(msg.getUserInfo().getName() + ": ");
//        msgText.setText(EmojiManager.parse(msg.getContent(), msgText.getTextSize()));
        }else {
            ToastUtil.showShortToast("网络连接异常");
        }

    }
}
