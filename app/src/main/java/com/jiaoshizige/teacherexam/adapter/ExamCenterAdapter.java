package com.jiaoshizige.teacherexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ExamCenterActivity;
import com.jiaoshizige.teacherexam.activity.ProvinceActivity;
import com.jiaoshizige.teacherexam.model.ExamSubjectModel;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/a1/6.
 */

public class ExamCenterAdapter extends PagerAdapter {
    private ExamCenterActivity mContext;
    private LayoutInflater mInflater;
    private List<ExamSubjectModel> mList;
    private View mView;
    private String[] mOption = new String[13];
    public ExamCenterAdapter(ExamCenterActivity context) {
        this.mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setmList(List<ExamSubjectModel> list)
    {
        if (null != list)
        {
            mList = list;
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
       return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mView = mInflater.inflate(R.layout.item_exam_center, null);
        mView.setTag(position);
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder(mView);
       /* if(mView == null){
            mView = mInflater.inflate(R.layout.item_exam_center,null);
            viewHolder = new ViewHolder(mView);
            mView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) mView.getTag();
        }*/
        if(mList != null && mList.size() > 0){
            viewHolder.mTitle.setText(mList.get(position).getTitle());
            if(!mList.get(position).getSubtitle().equals("")){
                viewHolder.mSelectTitle.setText(mList.get(position).getSubtitle());
            }
            if(position == 0){
            if(mContext.getmProvince() !=null && !mContext.getmProvince().equals("")){
                viewHolder.mSelectContent.setText(mContext.getmProvince());
                mOption[position] = mContext.getmProvinceStr();
                ToastUtil.showShortToast(mContext.getmProvinceStr());
                viewHolder.getmSelectContentRight.setVisibility(View.GONE);
            }else{
                viewHolder.mSelectContent.setText("请选择您的考试地区");
                viewHolder.getmSelectContentRight.setVisibility(View.VISIBLE);
//                mOption[position] = "请选择您的考试地区";
            }
            }else if(position == 9){
                if(mContext.getmGraduationTime() !=null && !mContext.getmGraduationTime().equals("")){
                    viewHolder.mSelectContent.setText(mContext.getmGraduationTime());
                    mOption[position] = mContext.getmGraduationTime();
                    Log.e("TAG",mContext.getmGraduationTime());
                    viewHolder.getmSelectContentRight.setVisibility(View.GONE);
                }else{
                    viewHolder.mSelectContent.setText("请选择您的毕业时间");
                    viewHolder.getmSelectContentRight.setVisibility(View.VISIBLE);
                }
            }
            viewHolder.mAnswerA.setText(mList.get(position).getAnswera());
            viewHolder.mAnswerB.setText(mList.get(position).getAnswerb());
            if(mList.get(position).getOtheranswer()== null){
                viewHolder.mAnswerF.setVisibility(View.GONE);
                viewHolder.mAnswerG.setVisibility(View.GONE);
                viewHolder.mAnswerH.setVisibility(View.GONE);
                viewHolder.mAnswerI.setVisibility(View.GONE);
                viewHolder.mAnswerJ.setVisibility(View.GONE);
                viewHolder.mAnswerK.setVisibility(View.GONE);
                viewHolder.mAnswerL.setVisibility(View.GONE);
                viewHolder.mAnswerM.setVisibility(View.GONE);
            }else {
                viewHolder.mAnswerF.setVisibility(View.VISIBLE);
                viewHolder.mAnswerG.setVisibility(View.VISIBLE);
                viewHolder.mAnswerH.setVisibility(View.VISIBLE);
                viewHolder.mAnswerI.setVisibility(View.VISIBLE);
                viewHolder.mAnswerJ.setVisibility(View.VISIBLE);
                viewHolder.mAnswerK.setVisibility(View.VISIBLE);
                viewHolder.mAnswerL.setVisibility(View.VISIBLE);
                viewHolder.mAnswerM.setVisibility(View.VISIBLE);
                viewHolder.mAnswerF.setText(mList.get(position).getOtheranswer().get(0));
                viewHolder.mAnswerG.setText(mList.get(position).getOtheranswer().get(1));
                viewHolder.mAnswerH.setText(mList.get(position).getOtheranswer().get(2));
                viewHolder.mAnswerI.setText(mList.get(position).getOtheranswer().get(3));
                viewHolder.mAnswerJ.setText(mList.get(position).getOtheranswer().get(4));
                viewHolder.mAnswerK.setText(mList.get(position).getOtheranswer().get(5));
                viewHolder.mAnswerL.setText(mList.get(position).getOtheranswer().get(6));
                viewHolder.mAnswerM.setText(mList.get(position).getOtheranswer().get(7));
            }
            if(mList.get(position).getAnswerc().equals("")){
                viewHolder.mAnswerC.setVisibility(View.GONE);
            }else{
                viewHolder.mAnswerC.setText(mList.get(position).getAnswerc());
                Log.e("*******mAnswerC", mList.get(position).getAnswerc());
            }
            if(mList.get(position).getAnswerd().equals("")){
                viewHolder.mAnswerD.setVisibility(View.GONE);
            }else{
                viewHolder.mAnswerD.setText(mList.get(position).getAnswerd());
                Log.e("*******mAnswerD", mList.get(position).getAnswerd());
            }
            if(mList.get(position).getAnswere().equals("")){
                viewHolder.mAnswerE.setVisibility(View.GONE);
            }else{
                viewHolder.mAnswerE.setText(mList.get(position).getAnswere());
                Log.e("*******mAnswerE", mList.get(position).getAnswere());
            }
            if(mList.get(position).getType().equals("1")){
                viewHolder.mSelectly.setVisibility(View.VISIBLE);
                viewHolder.mInputEditext.setVisibility(View.GONE);
                viewHolder.mOptionRg.setVisibility(View.GONE);
            }else if(mList.get(position).getType().equals("2")){
                viewHolder.mSelectly.setVisibility(View.GONE);
                viewHolder.mInputEditext.setVisibility(View.GONE);
                viewHolder.mOptionRg.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mSelectly.setVisibility(View.GONE);
                viewHolder.mInputEditext.setVisibility(View.VISIBLE);
                viewHolder.mOptionRg.setVisibility(View.GONE);
            }
                viewHolder.mSelectContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == 0){
                            Intent mIntent = new Intent(mContext, ProvinceActivity.class);
                            ((Activity) mContext).startActivityForResult(mIntent, 100);
                        }else{
                            mContext.showDialog();
                        }

                    }
                });


        viewHolder.mOptionRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(position < 13){
                    RadioButton radioButton = (RadioButton) mView.findViewById(viewHolder.mOptionRg.getCheckedRadioButtonId());
//                    mOption.get(position) = (String) radioButton.getText();
                    mOption[position] = (String) radioButton.getText();

                    Log.e("******mOption[position]", mOption[position]);}
            }
        });
            if( mOption[12] != null){
                viewHolder.mInputEditext.setText(mOption[12].toString());
                Log.e("*******mOption[12]", mOption[12].toString());
            }
            viewHolder.mInputEditext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mOption[position] = s.toString();
                }
            });

        }
        ((ViewGroup) container).addView(mView);
        return mView;

    }
public String[] getOPtions(){
    return  mOption;
}
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mView = (View) object;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        if((Integer)mView.getTag() == 0 || (Integer)mView.getTag() == 9){
            return POSITION_NONE;
        }

        return POSITION_UNCHANGED;
    }

    static class ViewHolder{
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.selectly)
        LinearLayout mSelectly;
        @BindView(R.id.select_title)
        TextView mSelectTitle;
        @BindView(R.id.select_content)
        TextView mSelectContent;
        @BindView(R.id.select_content_select)
        TextView getmSelectContentRight;
        @BindView(R.id.input_editext)
        EditText mInputEditext;
        @BindView(R.id.optionrg)
        RadioGroup mOptionRg;
        @BindView(R.id.answerA)
        RadioButton mAnswerA;
        @BindView(R.id.answerB)
        RadioButton mAnswerB;
        @BindView(R.id.answerC)
        RadioButton mAnswerC;
        @BindView(R.id.answerD)
        RadioButton mAnswerD;
        @BindView(R.id.answerE)
        RadioButton mAnswerE;
        @BindView(R.id.answerF)
        RadioButton mAnswerF;
        @BindView(R.id.answerG)
        RadioButton mAnswerG;
        @BindView(R.id.answerH)
        RadioButton mAnswerH;
        @BindView(R.id.answerI)
        RadioButton mAnswerI;
        @BindView(R.id.answerJ)
        RadioButton mAnswerJ;
        @BindView(R.id.answerK)
        RadioButton mAnswerK;
        @BindView(R.id.answerL)
        RadioButton mAnswerL;
        @BindView(R.id.answerM)
        RadioButton mAnswerM;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
