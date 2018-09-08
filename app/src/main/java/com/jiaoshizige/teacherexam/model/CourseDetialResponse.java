package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/24.
 * 课程详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class CourseDetialResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData {
        private String id;//
        private String qq;
        private String name;//课程名
        private String price;//价钱
        private String market_price;//市场价
        private String sale_num;//销量（在学人数）
        private String image;//封面图片
        private String description;//描述
        private String tag_id;//标签id
        private String start_time;//
        private String end_time;//
        private String book_id;//图书id（课程送书时才有值）
        private List<mTagList> tag_list;//
        private String is_buy;
        private String collect_status;//0未收藏，1已收藏
        private String belong_group;//0未加入班级，1已加入
        private String course_type;//1为章节课 2为任务课
        private List<mActivity> activity;
        private ClassDetailResponse.mTeacher teacher ;//班主任
        private List<ClassDetailResponse.mAssistant> assistant;//助教信息
        private String learn_percent;//学习进度
        private String days;

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getLearn_percent() {
            return learn_percent;
        }

        public void setLearn_percent(String learn_percent) {
            this.learn_percent = learn_percent;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public ClassDetailResponse.mTeacher getTeacher() {
            return teacher;
        }

        public void setTeacher(ClassDetailResponse.mTeacher teacher) {
            this.teacher = teacher;
        }

        public List<ClassDetailResponse.mAssistant> getAssistant() {
            return assistant;
        }

        public void setAssistant(List<ClassDetailResponse.mAssistant> assistant) {
            this.assistant = assistant;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getCourse_type() {
            return course_type;
        }

        public void setCourse_type(String course_type) {
            this.course_type = course_type;
        }

        public List<mTagList> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<mTagList> tag_list) {
            this.tag_list = tag_list;
        }

        public List<mActivity> getActivity() {
            return activity;
        }

        public void setActivity(List<mActivity> activity) {
            this.activity = activity;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getCollect_status() {
            return collect_status;
        }

        public void setCollect_status(String collect_status) {
            this.collect_status = collect_status;
        }

        public String getBelong_group() {
            return belong_group;
        }

        public void setBelong_group(String belong_group) {
            this.belong_group = belong_group;
        }
    }

    public static class mTagList {
        private String id;//标签id
        private String tag_name;//标签名
        private String images;//标签logo

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }

    public static class mActivity {
        private String activity_name;//
        private String icon;//活动图标
        private String type;
        private String id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
    public static class mTeacher{
        private String id;//班主任id
        private String name;//班主任名称
        private String headImg;//班主任头像
        private String qq;//qq

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public static class mAssistant{
        private String id;
        private String name;//
        private String headImg;//
        private String qq;//

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
