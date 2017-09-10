package com.sobag.beaconplayground;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Person {

    private String email;
    private String password;
    private String confirmpassword;
    private String phonenumber;
    private String userid = "";


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmPassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }




    @Override
    public String toString() {
        return "[UserId :" + userid + ",UserName=" + email +", Password="
                + password + ",ConfirmPassword="
                + confirmpassword + ",  PhoneNumber=" + phonenumber + "]";
    }


}