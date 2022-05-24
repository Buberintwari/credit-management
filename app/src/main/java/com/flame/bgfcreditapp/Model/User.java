package com.flame.bgfcreditapp.Model;

public class User {
    String name,email,idNumber,id,phonenumber,profilepictureurl,search;

    public User() {

    }

    public User(String name, String email, String idNumber, String id, String phonenumber, String profilepictureurl, String search) {
        this.name = name;
        this.email = email;
        this.idNumber = idNumber;
        this.id = id;
        this.phonenumber = phonenumber;
        this.profilepictureurl = profilepictureurl;
        this.search = search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
