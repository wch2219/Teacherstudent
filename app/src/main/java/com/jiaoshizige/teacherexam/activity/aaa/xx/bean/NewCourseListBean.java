package com.jiaoshizige.teacherexam.activity.aaa.xx.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class NewCourseListBean {

    /**
     * data : {"course_list":[{"catalog":[{"content":"","id":2694,"learn_percent":0,"learn_time":10,"live_id":0,"live_info":{},"practice":5,"title":"幼儿综合素质测试卷","type":2,"video":""}],"id":"162","name":"幼儿综合素质专项测评"}],"last_learn":{"section_id":100,"section_name":"第1节：教育评价前言","type":1,"vid":"1ff1d46899df72027e950769296c1b03_1"},"learn_percent":0}
     * message : 请求成功
     * status_code : 200
     */

    private DataBean data;
    private String message;
    private int status_code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public static class DataBean {
        /**
         * course_list : [{"catalog":[{"content":"","id":2694,"learn_percent":0,"learn_time":10,"live_id":0,"live_info":{},"practice":5,"title":"幼儿综合素质测试卷","type":2,"video":""}],"id":"162","name":"幼儿综合素质专项测评"}]
         * last_learn : {"section_id":100,"section_name":"第1节：教育评价前言","type":1,"vid":"1ff1d46899df72027e950769296c1b03_1"}
         * learn_percent : 0
         */

        private LastLearnBean last_learn;
        private int learn_percent;
        private List<CourseListBean> course_list;

        public LastLearnBean getLast_learn() {
            return last_learn;
        }

        public void setLast_learn(LastLearnBean last_learn) {
            this.last_learn = last_learn;
        }

        public int getLearn_percent() {
            return learn_percent;
        }

        public void setLearn_percent(int learn_percent) {
            this.learn_percent = learn_percent;
        }

        public List<CourseListBean> getCourse_list() {
            return course_list;
        }

        public void setCourse_list(List<CourseListBean> course_list) {
            this.course_list = course_list;
        }

        public static class LastLearnBean {
            /**
             * section_id : 100
             * section_name : 第1节：教育评价前言
             * type : 1
             * vid : 1ff1d46899df72027e950769296c1b03_1
             */

            private int section_id;
            private String section_name;
            private int type;
            private String vid;

            public int getSection_id() {
                return section_id;
            }

            public void setSection_id(int section_id) {
                this.section_id = section_id;
            }

            public String getSection_name() {
                return section_name;
            }

            public void setSection_name(String section_name) {
                this.section_name = section_name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }
        }

        public static class CourseListBean {
            /**
             * catalog : [{"content":"","id":2694,"learn_percent":0,"learn_time":10,"live_id":0,"live_info":{},"practice":5,"title":"幼儿综合素质测试卷","type":2,"video":""}]
             * id : 162
             * name : 幼儿综合素质专项测评
             */

            private String id;
            private String name;
            private List<CatalogBean> catalog;

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

            public List<CatalogBean> getCatalog() {
                return catalog;
            }

            public void setCatalog(List<CatalogBean> catalog) {
                this.catalog = catalog;
            }

            public static class CatalogBean {
                /**
                 * content :
                 * id : 2694
                 * learn_percent : 0
                 * learn_time : 10
                 * live_id : 0
                 * live_info : {}
                 * practice : 5
                 * title : 幼儿综合素质测试卷
                 * type : 2
                 * video :
                 */

                private String content;
                private int id;
                private int learn_percent;
                private int learn_time;
                private int live_id;
                private LiveInfoBean live_info;
                private int practice;
                private String title;
                private int type;
                private String video;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getLearn_percent() {
                    return learn_percent;
                }

                public void setLearn_percent(int learn_percent) {
                    this.learn_percent = learn_percent;
                }

                public int getLearn_time() {
                    return learn_time;
                }

                public void setLearn_time(int learn_time) {
                    this.learn_time = learn_time;
                }

                public int getLive_id() {
                    return live_id;
                }

                public void setLive_id(int live_id) {
                    this.live_id = live_id;
                }

                public LiveInfoBean getLive_info() {
                    return live_info;
                }

                public void setLive_info(LiveInfoBean live_info) {
                    this.live_info = live_info;
                }

                public int getPractice() {
                    return practice;
                }

                public void setPractice(int practice) {
                    this.practice = practice;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }

                public static class LiveInfoBean {
                    private String video_url;
                    private String push_url;
                    private String token;
                    private String chat_room_id;
                    private String app_name;
                    private long start_time;
                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getPush_url() {
                        return push_url;
                    }

                    public void setPush_url(String push_url) {
                        this.push_url = push_url;
                    }

                    public String getVideo_url() {
                        return video_url;
                    }

                    public void setVideo_url(String video_url) {
                        this.video_url = video_url;
                    }

                    public long getStart_time() {
                        return start_time;
                    }

                    public void setStart_time(long start_time) {
                        this.start_time = start_time;
                    }

                    public String getToken() {
                        return token;
                    }

                    public void setToken(String token) {
                        this.token = token;
                    }

                    public String getChat_room_id() {
                        return chat_room_id;
                    }

                    public void setChat_room_id(String chat_room_id) {
                        this.chat_room_id = chat_room_id;
                    }

                    public String getApp_name() {
                        return app_name;
                    }

                    public void setApp_name(String app_name) {
                        this.app_name = app_name;
                    }
                }
            }
        }
    }
}
