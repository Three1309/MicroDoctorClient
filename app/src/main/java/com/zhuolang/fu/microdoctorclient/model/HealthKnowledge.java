package com.zhuolang.fu.microdoctorclient.model;

import java.sql.Timestamp;

/**
 * Created by wunaifu on 2017/5/4.
 */
public class HealthKnowledge {
    private long id;
    private String keywords;//关键字
    private String title;//资讯标题
    private String description;//描述
    private String img;//图片
    private String message;//资讯内容
    private int infoclass;//分类
    private int count ;//访问次数
    private int rcount;//评论读数
    private int fcount;//收藏数
    private long time;//发布时间

    @Override
    public String toString() {
        return "HealthKnowledge{" +
                "id=" + id +
                ", keywords='" + keywords + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                ", message='" + message + '\'' +
                ", infoclass=" + infoclass +
                ", count=" + count +
                ", rcount=" + rcount +
                ", fcount=" + fcount +
                ", time=" + time +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getInfoclass() {
        return infoclass;
    }

    public void setInfoclass(int infoclass) {
        this.infoclass = infoclass;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
