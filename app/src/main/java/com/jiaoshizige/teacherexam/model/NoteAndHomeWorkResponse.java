package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;


@HttpResponse(parser = JsonResponseParser.class)
public class NoteAndHomeWorkResponse extends SupportResponse {
    private mInfo info;

    public mInfo getInfo() {
        return info;
    }

    public void setInfo(mInfo info) {
        this.info = info;
    }

    public static class mInfo{
        private List<mList> list;

        public List<mList> getList() {
            return list;
        }

        public void setList(List<mList> list) {
            this.list = list;
        }
    }
    public static class mList{
        private String id;//id
        private String depname;//名称

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDepname() {
            return depname;
        }

        public void setDepname(String depname) {
            this.depname = depname;
        }

    }
}
