package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/23.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ClassDetailResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }




    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;//班级id
        private String qq;
        private String assistant_id;//
        private String group_name;//班级名称
        private String images;//封面图片
        private String price;//班级价格
        private String market_price;//市场价
        private String sale_num;//已售数量
        private String mobile_detail;//详情
        private String tag_id;//标签id
        private String course_id;//课程id
        private String book_id;//图书id
        private String start_time;//有效期
        private String end_time;//有效期
        private String course_num;//课程数量
        private String give_book;//是否送书 yes送 no不送
        private String collect_status;//收藏状态1已收藏0未收藏
        private mTeacher teacher ;//班主任
        private List<mAssistant> assistant;//助教信息
        private List<mCoures> course;//课程信息
        private List<mTags> tags;//班级介绍里的班级特色
        private List<mActivity> activity;//

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getAssistant_id() {
            return assistant_id;
        }

        public void setAssistant_id(String assistant_id) {
            this.assistant_id = assistant_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
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

        public String getMobile_detail() {
            return mobile_detail;
        }

        public void setMobile_detail(String mobile_detail) {
            this.mobile_detail = mobile_detail;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getCourse_num() {
            return course_num;
        }

        public void setCourse_num(String course_num) {
            this.course_num = course_num;
        }

        public String getGive_book() {
            return give_book;
        }

        public void setGive_book(String give_book) {
            this.give_book = give_book;
        }

        public String getCollect_status() {
            return collect_status;
        }

        public void setCollect_status(String collect_status) {
            this.collect_status = collect_status;
        }

        public mTeacher getTeacher() {
            return teacher;
        }

        public void setTeacher(mTeacher teacher) {
            this.teacher = teacher;
        }

        public List<mAssistant> getAssistant() {
            return assistant;
        }

        public void setAssistant(List<mAssistant> assistant) {
            this.assistant = assistant;
        }

        public List<mCoures> getCourse() {
            return course;
        }

        public void setCourse(List<mCoures> course) {
            this.course = course;
        }

        public List<mTags> getTags() {
            return tags;
        }

        public void setTags(List<mTags> tags) {
            this.tags = tags;
        }

        public List<mActivity> getActivity() {
            return activity;
        }

        public void setActivity(List<mActivity> activity) {
            this.activity = activity;
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
    public static class mCoures{
        private String id;//课程id
        private String cover_image;//
        private String name;//
        private String price;//
        private String market_price;//
        private String sale_num;//
        private String book_id;//

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
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

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }
    }
   public static class mTags{
       private String tag_name;//标签名称
       private String img_path;//图标

       public String getImg_path() {
           return img_path;
       }

       public void setImg_path(String img_path) {
           this.img_path = img_path;
       }

       public String getTag_name() {
           return tag_name;
       }

       public void setTag_name(String tag_name) {
           this.tag_name = tag_name;
       }
   }
   public static class mActivity{
       private String activity_name;//活动名称
       private String icon;//活动图标
       private String id;
       private String type;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public String getType() {
           return type;
       }

       public void setType(String type) {
           this.type = type;
       }

       public String getIcon() {
           return icon;
       }

       public void setIcon(String icon) {
           this.icon = icon;
       }

       public String getActivity_name() {
           return activity_name;
       }

       public void setActivity_name(String activity_name) {
           this.activity_name = activity_name;
       }
   }
}
