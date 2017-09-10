package com.sobag.beaconplayground;

/**
 * Created by bdesh on 8/24/2017.
 */

public class Login {
    private String loginEmail;
    private String loginPassword;


    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }



    public String toString() {
        return "[UserId :" + loginEmail + ", UserName=" + loginPassword + "]";
    }

}
