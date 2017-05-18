package com.zhuolang.fu.microdoctorclient.model;

import java.util.Date;

/**
 * Created by wunaifu on 2017/5/15.
 */
public class ShareHouseDto {

    private int userId;//用户的ID
    private String userName;//用户的名字
    private String userNickName;//用户的昵称
    private int userType;//用户的权限类型
    private String userJianjie;//用户简介

    private int likesAmount;//点赞数
    private int discussAmount;//评论数
    private int collectAmount;//收藏数

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

    public String getUserJianjie() {
        return userJianjie;
    }

    public void setUserJianjie(String userJianjie) {
        this.userJianjie = userJianjie;
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

    @Override
    public String toString() {
        return "ShareHouseDto{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userType=" + userType +
                ", userJianjie='" + userJianjie + '\'' +
                ", likesAmount=" + likesAmount +
                ", discussAmount=" + discussAmount +
                ", collectAmount=" + collectAmount +
                '}';
    }
}
