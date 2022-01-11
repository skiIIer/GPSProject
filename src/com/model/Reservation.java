package com.model;

import java.sql.Date;

public class Reservation {
    private int id;
    private String clientName;
    private Date checkInDate;
    private Date checkOutDate;
    private double bill;
    private int nif;
    private String regNumber;
    private int state;
    private String slot;

    private String category;

    public Reservation(String clientName, Date checkInDate, Date checkOutDate, double bill, int nif, String regNumber, int state, String category) {
        this.clientName = clientName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bill = bill;
        this.nif = nif;
        this.regNumber = regNumber;
        this.state = state;
        this.category = category;
    }

    public Reservation(String clientName, Date checkInDate, Date checkOutDate, double bill, int nif, String regNumber, int state, String category,String slot) {
        this.clientName = clientName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bill = bill;
        this.nif = nif;
        this.regNumber = regNumber;
        this.state = state;
        this.category = category;
        this.slot = slot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(float bill) {
        this.bill = bill;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String registrationNumber) {
        this.regNumber = registrationNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) { this.slot = slot; }
}
