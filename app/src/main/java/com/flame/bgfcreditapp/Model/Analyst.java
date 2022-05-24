package com.flame.bgfcreditapp.Model;

public class Analyst {

    String name,email,id,phonenumber,profilepictureurl;

    public Analyst() {

    }

    public Analyst(String name, String email, String id, String phonenumber, String profilepictureurl) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.phonenumber = phonenumber;
        this.profilepictureurl = profilepictureurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }
}
