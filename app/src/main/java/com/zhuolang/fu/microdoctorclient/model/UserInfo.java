package com.zhuolang.fu.microdoctorclient.model;

/**
 * Created by wunaifu on 2017/4/28.
 */
public class UserInfo {
    private int id;
    private String phone;
    private String password;
    private String name;
    private String nickname;//昵称
    private int gender;//性别:男（0）女（1）
    private int age;
    private String address;
    private String signature;//个性签名
    private String introduction;
    private int type ;//用户类型:病人（0）医师（1）
    //    private int doctorId;
    private String hospital;
    private String office;
    private int amount;
    private int likenum;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", signature='" + signature + '\'' +
                ", introduction='" + introduction + '\'' +
                ", type=" + type +
                ", hospital='" + hospital + '\'' +
                ", office='" + office + '\'' +
                ", amount=" + amount +
                ", likenum=" + likenum +
                '}';
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }
}
