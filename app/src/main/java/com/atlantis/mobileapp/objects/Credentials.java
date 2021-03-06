package com.atlantis.mobileapp.objects;

public class Credentials {

    private String login;
    private String password;
    private int userId;

    public Credentials(String login, String password, int userId) {
        this.login = login;
        this.password = password;
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
