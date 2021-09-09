package com.example.Sumuhandemo.bean;

public class User_Info {
    public String Username;
    public String Email;
    public String Password;
    public String newPassword;
    public User_Info(String username,String email,String password ){
        this.newPassword="";
        this.Username=username;
        this.Email=email;
        this.Password=password;
    }
}
