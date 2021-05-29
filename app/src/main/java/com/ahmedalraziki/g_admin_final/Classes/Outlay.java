package com.ahmedalraziki.g_admin_final.Classes;

public class Outlay {
    String id;
    String date;
    String amount;
    String fromAny;
    String StaffID;
    String Type;
    int year;
    int month;
    int day;

    public Outlay(String id, String date, String amount, String fromAny, String staffID, String type) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.fromAny = fromAny;
        StaffID = staffID;
        Type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromAny() {
        return fromAny;
    }

    public void setFromAny(String fromAny) {
        this.fromAny = fromAny;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
