package com.zhuolang.fu.microdoctorclient.model;

/**
 * Created by wunaifu on 2017/5/4.
 */
public class AppointmentDto {
    private int id;
    private int patientId;//病人ID
    private String patientName;//病人名字
    private int doctorId;//医师ID
    private String doctorName;//医师名字
    private String seeTime;//预约了去就诊日期时间
    private String disease;//病症
    private String dateTime;//预约时间、
    private String diagnose;//医生诊断
    private int dLikenum;//好评数
    private int dNumber;
    private String doctorSay;//医生预约时留言

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSeeTime() {
        return seeTime;
    }

    public void setSeeTime(String seeTime) {
        this.seeTime = seeTime;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public int getdLikenum() {
        return dLikenum;
    }

    public void setdLikenum(int dLikenum) {
        this.dLikenum = dLikenum;
    }

    public int getdNumber() {
        return dNumber;
    }

    public void setdNumber(int dNumber) {
        this.dNumber = dNumber;
    }

    public String getDoctorSay() {
        return doctorSay;
    }

    public void setDoctorSay(String doctorSay) {
        this.doctorSay = doctorSay;
    }

    @Override
    public String toString() {
        return "AppointmentDto{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", seeTime='" + seeTime + '\'' +
                ", disease='" + disease + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", diagnose='" + diagnose + '\'' +
                ", dLikenum=" + dLikenum +
                ", dNumber=" + dNumber +
                ", doctorSay='" + doctorSay + '\'' +
                '}';
    }
}
