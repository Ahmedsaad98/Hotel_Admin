package com.ahmedalraziki.g_admin_final.Classes;

public class Reservation {
    String id;
    String name;
    String phone;
    String email;
    String dci;
    String dco;
    String nob;
    String total;
    String roomNo;
    String staffCki;
    String staffCko;

    public Reservation(String id, String name, String phone, String email, String dci, String dco,
                       String nob, String total) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dci = dci;
        this.dco = dco;
        this.nob = nob;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDci() {
        return dci;
    }

    public void setDci(String dci) {
        this.dci = dci;
    }

    public String getDco() {
        return dco;
    }

    public void setDco(String dco) {
        this.dco = dco;
    }

    public String getNob() {
        return nob;
    }

    public void setNob(String nob) {
        this.nob = nob;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getStaffCki() {
        return staffCki;
    }

    public void setStaffCki(String staffCki) {
        this.staffCki = staffCki;
    }

    public String getStaffCko() {
        return staffCko;
    }

    public void setStaffCko(String staffCko) {
        this.staffCko = staffCko;
    }
}
