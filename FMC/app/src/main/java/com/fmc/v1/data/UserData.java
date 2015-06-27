package com.fmc.v1.data;

import java.util.ArrayList;

/**
 * Created by Nilesh on 28/06/15.
 */
public class UserData {
    String name,bio,website,bitchUserName,bitchPassword,birthDay;

    ArrayList<ChildData> arrChildData = new ArrayList<>();

    public ArrayList<ChildData> getArrChildData() {
        return arrChildData;
    }

    public void setArrChildData(ArrayList<ChildData> arrChildData) {
        this.arrChildData = arrChildData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBitchUserName() {
        return bitchUserName;
    }

    public void setBitchUserName(String bitchUserName) {
        this.bitchUserName = bitchUserName;
    }

    public String getBitchPassword() {
        return bitchPassword;
    }

    public void setBitchPassword(String bitchPassword) {
        this.bitchPassword = bitchPassword;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
