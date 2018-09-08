package com.jiaoshizige.teacherexam.activity.aaa.xx.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/9/6.
 */

public class DownManagerBaseBean {
    private List<DownManagerBean> downIngBeans;//下载中
    private List<DownManagerBean> downSuccBeans;//下载成功

    public List<DownManagerBean> getDownIngBeans() {
        return downIngBeans;
    }

    public void setDownIngBeans(List<DownManagerBean> downIngBeans) {
        this.downIngBeans = downIngBeans;
    }

    public List<DownManagerBean> getDownSuccBeans() {
        return downSuccBeans;
    }

    public void setDownSuccBeans(List<DownManagerBean> downSuccBeans) {
        this.downSuccBeans = downSuccBeans;
    }
}
