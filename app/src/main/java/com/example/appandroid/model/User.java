package com.example.appandroid.model;

import java.io.Serializable;

public class User implements Serializable {
  private String username;
  private String password;
  private String fullname;
  private String address;
  private String phone;

  public User(String username, String password, String fullname, String address, String phone){
      this.username = username;
      this.password = password;
      this.fullname = fullname;
      this.address = address;
      this.phone = phone;
  }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
