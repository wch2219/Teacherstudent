package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/23.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class EvaluateResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String avg_rank;//平均评分
        private List<mLabel> label;//标签列表
        private List<mComment> comment;

        public String getAvg_rank() {
            return avg_rank;
        }

        public void setAvg_rank(String avg_rank) {
            this.avg_rank = avg_rank;
        }

        public List<mLabel> getLabel() {
            return label;
        }

        public void setLabel(List<mLabel> label) {
            this.label = label;
        }

        public List<mComment> getComment() {
            return comment;
        }

        public void setComment(List<mComment> comment) {
            this.comment = comment;
        }
    }
    public static class mLabel{
        private String id;//标签id
        private String label_name;//标签名
        private String count;//打标签个数

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
    public static class mComment{
        private String id;//评论id
        private String rank;//评论星级
        private String content;//评论内容
        private String created_at;//评论时间
        private String nick_name;//用户名
        private String name;//用户名
        private String avatar;//头像
        private String count_zan;//点赞数量
        private String is_like;
        private List<mImage> image ;//图片

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCount_zan() {
            return count_zan;
        }

        public void setCount_zan(String count_zan) {
            this.count_zan = count_zan;
        }

        public List<mImage> getImage() {
            return image;
        }

        public void setImage(List<mImage> image) {
            this.image = image;
        }
    }
    public static class mImage{
        private String id;//图片id
        private String images;//评论图片地址

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
}
