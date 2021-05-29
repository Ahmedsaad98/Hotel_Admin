package com.ahmedalraziki.g_admin_final.Classes;

import com.google.firebase.database.DatabaseReference;

public class Staff {
    String id;
    String username;
    String password;
    String name;
    String phone;
    String address;
    String position;
    String salary;
    String email;
    int lvl;
    DatabaseReference staRef;

    public Staff(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Staff(String id, String username, String password, String name, String phone,
                 String address, String position, String salary, String email, int lvl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.position = position;
        this.salary = salary;
        this.email = email;
        this.lvl = lvl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() { return salary; }

    public void setSalary(String salary) { this.salary = salary; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getLvl() { return lvl; }

    public void setLvl(int lvl) { this.lvl = lvl; }

    public DatabaseReference getStaRef() {
        return staRef;
    }

    public void setStaRef(DatabaseReference staRef) {
        this.staRef = staRef;
    }
}
