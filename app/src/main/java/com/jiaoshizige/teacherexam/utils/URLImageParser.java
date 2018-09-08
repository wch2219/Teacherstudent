package com.jiaoshizige.teacherexam.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;

    public URLImageParser(TextView textView) {
        this.mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.e("图片地址source:",source);
        final URLDrawable urlDrawable = new URLDrawable();
        ImageLoader.getInstance().loadImage(source,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        urlDrawable.bitmap = loadedImage;
                        urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                        mTextView.invalidate();
                        mTextView.setText(mTextView.getText());
                    }
                });
        return urlDrawable;
    }
}
