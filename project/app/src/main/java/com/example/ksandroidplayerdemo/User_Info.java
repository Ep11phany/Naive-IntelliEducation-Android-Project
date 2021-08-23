package com.example.ksandroidplayerdemo;

public class User_Info {
    String Username;
    String Email;
    String Password;
    String newPassword;
    public User_Info(String username,String email,String password ){
        this.newPassword="";
        this.Username=username;
        this.Email=email;
        this.Password=password;
    }
}
