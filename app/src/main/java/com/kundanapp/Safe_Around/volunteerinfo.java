package com.kundanapp.Safe_Around;

public class volunteerinfo {

    private String Name;
    private String State;
    private String City;
    private String phone;
    private String desc;
    private Double lat;
    private Double lang;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public volunteerinfo() {

    }

    public volunteerinfo(String name, String state, String city, String phone, String desc, Double lat, Double lang) {
        Name = name;
        State = state;
        City = city;
        this.phone = phone;
        this.desc = desc;
        this.lat = lat;
        this.lang = lang;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
