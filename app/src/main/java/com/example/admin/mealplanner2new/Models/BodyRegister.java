package com.example.admin.mealplanner2new.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyRegister {

    @SerializedName("wourkout_place")
    @Expose
    private String workout_place;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("aim")
    @Expose
    private String aim;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("training_level")
    @Expose
    private String training_level;

    @SerializedName("past_ex_ex")
    @Expose
    private String past_ex_ex;

    @SerializedName("schedule")
    @Expose
    private String schedule;

    @SerializedName("week_minutes")
    @Expose
    private String week_minutes;

    @SerializedName("ex_days")
    @Expose
    private String ex_days;

    @SerializedName("coach_id")
    @Expose
    private String coach_id;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("password_confirmation")
    @Expose
    private String password_confirmation;


    public BodyRegister(String workout_place, String gender, String aim, String height, String age, String weight, String training_level, String past_ex_ex, String schedule, String week_minutes, String ex_days, String coach_id, String address, String country, String state, String city, String name, String email, String mobile, String password, String password_confirmation) {
        this.workout_place = workout_place;
        this.gender = gender;
        this.aim = aim;
        this.height = height;
        this.age = age;
        this.weight = weight;
        this.training_level = training_level;
        this.past_ex_ex = past_ex_ex;
        this.schedule = schedule;
        this.week_minutes = week_minutes;
        this.ex_days = ex_days;
        this.coach_id = coach_id;
        this.address = address;
        this.country = country;
        this.state = state;
        this.city = city;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWorkout_place() {
        return workout_place;
    }

    public void setWorkout_place(String workout_place) {
        this.workout_place = workout_place;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTraining_level() {
        return training_level;
    }

    public void setTraining_level(String training_level) {
        this.training_level = training_level;
    }

    public String getPast_ex_ex() {
        return past_ex_ex;
    }

    public void setPast_ex_ex(String past_ex_ex) {
        this.past_ex_ex = past_ex_ex;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getWeek_minutes() {
        return week_minutes;
    }

    public void setWeek_minutes(String week_minutes) {
        this.week_minutes = week_minutes;
    }

    public String getEx_days() {
        return ex_days;
    }

    public void setEx_days(String ex_days) {
        this.ex_days = ex_days;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }


}

