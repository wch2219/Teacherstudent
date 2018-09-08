package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.EvaluateResponse;
import com.nex3z.flowlayout.FlowLayout;
import butterknife.BindView;

public class HeadViewCourseEvaluateList extends SDAppView {

    @BindView(R.id.toatal_ratingbar)
    RatingBar toatal_ratingbar;
    @BindView(R.id.total_evaluate)
    TextView total_evaluate;
    @BindView(R.id.flow_layout)
    FlowLayout flow_layout;

    public HeadViewCourseEvaluateList(Context context) {
        super(context);
        init();
    }

    public HeadViewCourseEvaluateList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.evaluate_header_layout);

    }

    public HeadViewCourseEvaluateList bindData(EvaluateResponse result){
        if(result == null || result.getData() == null ||result.getData().getLabel()==null){
            return this;
        }
        toatal_ratingbar.setRating(Float.valueOf(result.getData().getAvg_rank()));
        total_evaluate.setText(result.getData().getAvg_rank() + "分");
        flow_layout.removeAllViews();
        if (result.getData().getLabel() != null && result.getData().getLabel().size() > 0) {
            Log.e("TAG`1", "55555");
            int i = 0;
            for (final EvaluateResponse.mLabel mLabel : result.getData().getLabel()) {
                i++;
                TextView textView = new TextView(getActivity());
                textView.setPadding(16, 10, 16, 10);
                textView.setText(mLabel.getLabel_name() + "(" + mLabel.getCount() + ")");
                textView.setTextSize(12);
                if (i == 3) {
                    textView.setBackgroundResource(R.drawable.gray_shap_5);
                } else {
                    textView.setBackgroundResource(R.drawable.buy_orange_shape);
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mOnEvaluateTabClick != null){
                            mOnEvaluateTabClick.onTabClick(v,mLabel.getId());
                        }
//                        mTagId = mLabel.getId();
//                        requestEvaluate(mGoodId, mTyp);
                    }
                });
                flow_layout.addView(textView);
            }
        }
        return this;
    }

    private onEvaluateTabClick mOnEvaluateTabClick;

    public HeadViewCourseEvaluateList setOnEvaluateTabClick(onEvaluateTabClick onEvaluateTabClick) {
        mOnEvaluateTabClick = onEvaluateTabClick;
        return this;
    }

    public interface onEvaluateTabClick{
        void onTabClick(View view,String tagId);
    }
}
