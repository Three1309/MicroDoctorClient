package com.zhuolang.fu.microdoctorclient.model;

import java.util.Date;

/**
 * Created by wunaifu on 2017/5/15.
 */
public class ShareDiscussDto {

    private int id;//评论的ID
    private int sendId;//帖子的ID
    private int userId;//评论人的ID
    private String userName;//评论人的名字
    private String userNickName;//评论人的昵称
    private int userType;//评论人的权限类型
    private String discussContent;//评论内容
    private String discussTime;//评论时间

    @Override
    public String toString() {
        return "ShareDiscussDto{" +
                "id=" + id +
                ", sendId=" + sendId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userType=" + userType +
                ", discussContent='" + discussContent + '\'' +
                ", discussTime='" + discussTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDiscussContent() {
        return discussContent;
    }

    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }

    public String getDiscussTime() {
        return discussTime;
    }

    public void setDiscussTime(String discussTime) {
        this.discussTime = discussTime;
    }
}
