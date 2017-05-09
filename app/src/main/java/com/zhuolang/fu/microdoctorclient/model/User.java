package com.zhuolang.fu.microdoctorclient.model;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class User {
    private int id;//自增
    private String phone;//联系方式（登陆账号）
    private String nickname;//昵称
    private String password;
    private String name;
    private int age;
    private int gender;//性别:男（0）女（1）
    private String address;
    private String signature;//个性签名
    private String introduction;//个人简介（医师擅长、病人病历）
    private int type;//用户类型:病人（0）医师（1）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", signature='" + signature + '\'' +
                ", introduction='" + introduction + '\'' +
                ", type=" + type +
                '}';
    }
}
