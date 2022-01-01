package com.model;

import java.sql.Date;
import java.sql.SQLException;

public class Model {
    public Model(){
        try {
            new DatabaseModel();//Initializes database
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new CRUD();//Initializes API for database operations
    }
    public boolean addReservation(String name, Date checkIn, Date checkOut, double bill, int nif, String regNumber, int state){
        Reservation reservation = new Reservation(name, checkIn, checkOut, bill, nif, regNumber, state);
        CRUD.create(reservation);
        return true;
    }
//    public boolean editReservation(){}
//    public boolean verifyFormat(){}
//    public boolean cancelReservation(){}
//    public String viewReservations(){}
//    public boolean refuel(){}
//    public String verifySlot(){}
//    public String viewStatistics(){}
//    public String calcMostCommonCat(){}
//    public String calcMostActiveMonth(){}
//    public String calcIncome(){}
//    public String calcMostProfitMonth(){}
}
