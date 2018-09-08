package com.jiaoshizige.teacherexam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/a1/6.
 */

public class ExamSubjectBean {
    public static List<ExamSubjectModel> bean(){
        List<ExamSubjectModel> list = new ArrayList<>();
        ExamSubjectModel model = new ExamSubjectModel();
        model.setTitle("1.您要报考教师资格证的地区是？");
        model.setSubtitle("考试地区");
        model.setAnswera("");
        model.setAnswerb("");
        model.setAnswerc("");
        model.setAnswerd("");
        model.setAnswere("");
        model.setType("1");
        model.setOtheranswer(null);
        list.add(model);
        ExamSubjectModel model1 = new ExamSubjectModel();
        model1.setTitle("2.您要报考教师资格证的类型是？");
        model1.setSubtitle("");
        model1.setAnswera("幼儿");
        model1.setAnswerb("小学");
        model1.setAnswerc("初中");
        model1.setAnswerd("高中");
        model1.setAnswere("");
        model1.setType("2");
        model1.setOtheranswer(null);
        list.add(model1);
        ExamSubjectModel model2 = new ExamSubjectModel();
        model2.setTitle("3.您想从教的学科是？");
        model2.setSubtitle("");
        model2.setAnswera("幼儿不分学科");
        model2.setAnswerb("语文");
        model2.setAnswerc("数学");
        model2.setAnswerd("英语");
        model2.setAnswere("政治");
        model2.setType("2");
        List<String> mList = new ArrayList<>();
        mList.add("历史");
        mList.add("地理");
        mList.add("化学");
        mList.add("生物");
        mList.add("信息技术");
        mList.add("美术");
        mList.add("音乐");
        mList.add("体育");
        model2.setOtheranswer(mList);
        list.add(model2);
        ExamSubjectModel model3 = new ExamSubjectModel();
        model3.setTitle("4.您之前参加过教师资格证考试吗？");
        model3.setSubtitle("");
        model3.setAnswera("未参加过");
        model3.setAnswerb("参加过一次");
        model3.setAnswerc("参加过多次");
        model3.setAnswerd("");
        model3.setAnswere("");
        model3.setType("2");
        model3.setOtheranswer(null);
        list.add(model3);
        ExamSubjectModel model4 = new ExamSubjectModel();
        model4.setTitle("5.您现在备考教师资格证准备的怎么样了？");
        model4.setSubtitle("");
        model4.setAnswera("还没开始学习");
        model4.setAnswerb("刚开始学习");
        model4.setAnswerc("已掌握一些知识");
        model4.setAnswerd("已准备很充分");
        model4.setAnswere("");
        model4.setType("2");
        model4.setOtheranswer(null);
        list.add(model4);
        ExamSubjectModel model5 = new ExamSubjectModel();
        model5.setTitle("6.您每天的学习时间是");
        model5.setSubtitle("");
        model5.setAnswera("1~2小时");
        model5.setAnswerb("3~5小时");
        model5.setAnswerc("6~8小时");
        model5.setAnswerd("有充足时间备考");
        model5.setAnswere("");
        model5.setType("2");
        model5.setOtheranswer(null);
        list.add(model5);
        ExamSubjectModel model6 = new ExamSubjectModel();
        model6.setTitle("7.您是否已经购买教材？");
        model6.setSubtitle("");
        model6.setAnswera("是");
        model6.setAnswerb("否");
        model6.setAnswerc("");
        model6.setAnswerd("");
        model6.setAnswere("");
        model6.setType("2");
        model6.setOtheranswer(null);
        list.add(model6);
        ExamSubjectModel model7 = new ExamSubjectModel();
        model7.setTitle("8.您会选择的备考方式是？");
        model7.setSubtitle("");
        model7.setAnswera("自学");
        model7.setAnswerb("报班");
        model7.setAnswerc("");
        model7.setAnswerd("");
        model7.setAnswere("");
        model7.setType("2");
        model7.setOtheranswer(null);
        list.add(model7);
        ExamSubjectModel model8 = new ExamSubjectModel();
        model8.setTitle("9.您是在校生还是社会生？");
        model8.setSubtitle("");
        model8.setAnswera("在校生");
        model8.setAnswerb("社会生");
        model8.setAnswerc("");
        model8.setAnswerd("");
        model8.setAnswere("");
        model8.setType("2");
        model8.setOtheranswer(null);
        list.add(model8);
        ExamSubjectModel model9 = new ExamSubjectModel();
        model9.setTitle("10.您的毕业时间是？");
        model9.setSubtitle("毕业时间");
        model9.setAnswera("");
        model9.setAnswerb("");
        model9.setAnswerc("");
        model9.setAnswerd("");
        model9.setAnswere("");
        model9.setType("1");
        model9.setOtheranswer(null);
        list.add(model9);
        ExamSubjectModel model10 = new ExamSubjectModel();
        model10.setTitle("11.您是师范生还是非师范生？");
        model10.setSubtitle("");
        model10.setAnswera("师范生");
        model10.setAnswerb("非师范生");
        model10.setAnswerc("");
        model10.setAnswerd("");
        model10.setAnswere("");
        model10.setType("2");
        model10.setOtheranswer(null);
        list.add(model10);
        ExamSubjectModel model11 = new ExamSubjectModel();
        model11.setTitle("12.您的学历是？");
        model11.setSubtitle("");
        model11.setAnswera("大专以下学历");
        model11.setAnswerb("大专");
        model11.setAnswerc("本科");
        model11.setAnswerd("本科以上");
        model11.setAnswere("");
        model11.setType("2");
        model11.setOtheranswer(null);
        list.add(model11);
        ExamSubjectModel model12 = new ExamSubjectModel();
        model12.setTitle("13.您所学专业是？");
        model12.setSubtitle("");
        model12.setAnswera("");
        model12.setAnswerb("");
        model12.setAnswerc("");
        model12.setAnswerd("");
        model12.setAnswere("");
        model12.setType("3");
        model12.setOtheranswer(null);
        list.add(model12);
        return list;
    }
}
