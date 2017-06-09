package com.zhuolang.fu.microdoctorclient.common;

/**
 * Created by wunaifu on 2017/4/13.
 */
public class APPConfig {
//    private static String base_url="http://192.168.43.239:8080/MicroDoctorServer/";
//    private static String base_url="http://139.199.66.242:8080/MicroDoctorServer/";
    private static String base_url="http://119.29.191.73:8080/MicroDoctorServer/";
//    private static String base_url="http://172.27.196.2:8080/MicroDoctorServer/";

    public static String login = base_url + "login_user";
    public static String register = base_url + "add_user";
    public static String findUserByPhone = base_url + "finduser_byphone";
    public static String updatePassword = base_url + "update_password";
    public static String updatePhone = base_url + "update_phone";
    public static String updateUser = base_url + "update_user";
    public static String registerDoctor = base_url + "register_doctor";
    public static String updateRegisterDoctor = base_url + "update_registerdoctor";
    public static String findRegisterDoctor = base_url + "find_registerdoctor";
    public static String agreeRegisterDoctor = base_url + "agree_registerdoctor";
    public static String disagreeRegisterDoctor = base_url + "disagree_registerdoctor";
    public static String findAllDoctor = base_url + "find_alldoctor";
    public static String findAllOffice = base_url + "find_alloffice";
    public static String findAllHospital = base_url + "find_allhospital";
    public static String findDoctorByOfficeAndHospital = base_url + "finddoctor_byOfficeAndHospital";
    public static String addAppointment = base_url + "add_appointment";
    public static String findNowMyappointment = base_url + "find_myappointment";
    public static String findMyappointmentHistory = base_url + "find_myappointment_history";
    public static String updateDoctorlikes = base_url + "update_doctorlikes";
    public static String likesOrNot = base_url + "likes_ornot";
    public static String findDoctorApptm = base_url + "find_apptmbydocId";
    public static String updateDoctorsay = base_url + "update_apptmdoctorsay";
    public static String updateDiagnose = base_url + "update_apptmdiagnose";
    public static String findDoctSeeHistory = base_url + "find_doctseehistory";

    public static String findAllShare = base_url + "findAllShare";
    public static String findAllShareDiscuss = base_url + "findAllShareDiscuss";
    public static String addShareSend = base_url + "addShareSend";
    public static String deleteShareSendBySendId = base_url + "deleteShareSendBySendId";
    public static String addShareDiscuss = base_url + "addShareDiscuss";
    public static String deleteShareDiscuss = base_url + "deleteShareDiscuss";
    public static String updateShareCollect = base_url + "updateShareCollect";
    public static String updateShareLikes = base_url + "updateShareLikes";
    public static String findUserShareInfo = base_url + "findUserShareInfo";
    public static String findMyShareInfoHistory = base_url + "findMyShareInfoHistory";
    public static String findMyCollectShareInfo = base_url + "findMyCollectShareInfo";
    public static String findMyDiscussShareInfo = base_url + "findMyDiscussShareInfo";


    public static String IS_LOGIN = "is_login";
    public static String ACCOUNT = "account";
    public static String PSW = "password";
    public static String USERDATA = "userData";//获取当前用户的key
    public static String TYPE = "userType";
}

