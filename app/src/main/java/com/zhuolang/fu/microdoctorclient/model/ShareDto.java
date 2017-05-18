package com.zhuolang.fu.microdoctorclient.model;

import java.util.Date;

/**
 * Created by wunaifu on 2017/5/15.
 */
public class ShareDto {

    private int sendId;//帖子的ID
    private int userId;//发帖者的ID
    private String userName;//发帖人的名字
    private String userNickName;//发帖人的昵称
    private int userType;//发帖人的权限类型
    private String sendTitle;//帖子标题
    private String sendContent;//帖子内容
    private String sendTime;//发帖时间
    private int likesAmount;//点赞数
    private int discussAmount;//评论数
    private int collectAmount;//收藏数
    private String collectOrNot;//是否收藏
    private String likesOrNot;//是否点赞

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

    public String getSendTitle() {
        return sendTitle;
    }

    public void setSendTitle(String sendTitle) {
        this.sendTitle = sendTitle;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getLikesAmount() {
        return likesAmount;
    }

    public void setLikesAmount(int likesAmount) {
        this.likesAmount = likesAmount;
    }

    public int getDiscussAmount() {
        return discussAmount;
    }

    public void setDiscussAmount(int discussAmount) {
        this.discussAmount = discussAmount;
    }

    public int getCollectAmount() {
        return collectAmount;
    }

    public void setCollectAmount(int collectAmount) {
        this.collectAmount = collectAmount;
    }

    public String getCollectOrNot() {
        return collectOrNot;
    }

    public void setCollectOrNot(String collectOrNot) {
        this.collectOrNot = collectOrNot;
    }

    public String getLikesOrNot() {
        return likesOrNot;
    }

    public void setLikesOrNot(String likesOrNot) {
        this.likesOrNot = likesOrNot;
    }

    @Override
    public String toString() {
        return "ShareDto{" +
                "sendId=" + sendId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userType=" + userType +
                ", sendTitle='" + sendTitle + '\'' +
                ", sendContent='" + sendContent + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", likesAmount=" + likesAmount +
                ", discussAmount=" + discussAmount +
                ", collectAmount=" + collectAmount +
                ", collectOrNot='" + collectOrNot + '\'' +
                ", likesOrNot='" + likesOrNot + '\'' +
                '}';
    }
}
