package com.sobag.beaconplayground;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Person {

    private String email;
    private String password;
    private String phonenumber;
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    @Override
    public String toString() {
        return "Person [UserId :" + 12 + ", UserName=" + username + ", UserPasswrod="
                + password + ", UserEmail=" + email +", UserTelephone=" + phonenumber + ", UserRegisteredTime=" + formattedDate + " ]";
    }


}