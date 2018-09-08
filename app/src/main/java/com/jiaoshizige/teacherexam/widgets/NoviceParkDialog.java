package com.jiaoshizige.teacherexam.widgets;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.jiaoshizige.teacherexam.R;

/**
 * Created by Administrator on 2017/a1/8.
 * 新手礼包 Dialog
 */

public class NoviceParkDialog extends Dialog {
    public NoviceParkDialog(@NonNull Context context) {
        super(context);
    }
    public NoviceParkDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected NoviceParkDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public static class Builder {
        private Context context;
        private DialogInterface.OnClickListener ButtonClickLookListener;
        private DialogInterface.OnClickListener ButtonClickCloseListener;
        public Builder(Context context) {
            this.context = context;
        }
        public Builder setLookMoreButton(DialogInterface.OnClickListener listener) {
            this.ButtonClickLookListener = listener;
            return this;
        }

        public Builder setCloseButton(DialogInterface.OnClickListener listener) {
            this.ButtonClickCloseListener = listener;
            return this;
        }
        public NoviceParkDialog create(){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final NoviceParkDialog dialog = new NoviceParkDialog(context, R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.novice_pack_dialog, null);
            dialog.addContentView(layout, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            if (ButtonClickLookListener != null) {
                layout.findViewById(R.id.look_more).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ButtonClickLookListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (ButtonClickCloseListener != null) {
                layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ButtonClickCloseListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
