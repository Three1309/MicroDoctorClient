package com.zhuolang.fu.microdoctorclient.model;

/**
 * Created by wunaifu on 2017/5/4.
 */
public class Doctor {
    private int id;
    private int doctorId;
    private String hospital;
    private String office;
    private int amount;
    private int likenum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", hospital='" + hospital + '\'' +
                ", office='" + office + '\'' +
                ", amount=" + amount +
                ", likenum=" + likenum +
                '}';
    }
}
