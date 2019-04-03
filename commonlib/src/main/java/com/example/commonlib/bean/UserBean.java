package com.example.commonlib.bean;

/**
 * Created by smile on 2019/3/15.
 */

public class UserBean {
    private String userName;
    private String userPassword;
    private String userToken;
    private int age;
    private boolean booStudent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getBooStudent() {
        return booStudent;
    }

    public void setBooStudent(boolean booStudent) {
        this.booStudent = booStudent;
    }
}
