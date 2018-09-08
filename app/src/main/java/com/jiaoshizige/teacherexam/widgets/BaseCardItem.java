package com.jiaoshizige.teacherexam.widgets;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2018/7/23.
 */

public class BaseCardItem <T> extends Fragment {

    protected static final String ARGUMENTS_HANDLER = "cards_handler";
    protected static final String ARGUMENTS_DATA = "cards_data";
    protected static final String ARGUMENTS_POSITION = "cards_position";


    @CardViewPager.TransformerMode
  public  int currentMode;

    public void bindHandler(CardHandler<T> handler) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            setArguments(bundle);
        }
        bundle.putSerializable(ARGUMENTS_HANDLER, handler);
    }
}
