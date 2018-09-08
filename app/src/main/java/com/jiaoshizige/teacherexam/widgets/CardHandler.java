package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/23.
 */

public interface CardHandler<T> extends Serializable {

    View onBind(Context context, T data, int position, @CardViewPager.TransformerMode int mode);
}
