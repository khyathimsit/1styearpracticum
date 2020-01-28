package com.example.wingman;

public class Users {
    private String name, password, phone, hashkey, mail;

    public Users() {
    }

    public Users(String name, String password, String phone, String hashkey, String mail) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.hashkey = hashkey;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHashkey() {
        return hashkey;
    }

    public void setHashkey(String hashkey) {
        this.hashkey = hashkey;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
