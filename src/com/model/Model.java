package com.model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    CRUD crud;

    public Model(){
        DatabaseModel databaseModel;
        try {
            databaseModel = new DatabaseModel();//Initializes database
            crud = new CRUD(databaseModel.getConnection());//Initializes API for database operations
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addReservation(String name, Date checkIn, Date checkOut, double bill, int nif, String regNumber, int state){
        Reservation reservation = new Reservation(name, checkIn, checkOut, bill, nif, regNumber, state);
        crud.create(reservation);
        return true;
    }
//    public boolean editReservation(){}
//    public boolean verifyFormat(){}

    public boolean cancelReservation(int id){
        return crud.delete(id);
    }

//    public String viewReservations(){}
//    public boolean refuel(){}
//    public String verifySlot(){}
//    public String viewStatistics(){}
//    public String calcMostCommonCat(){}
//    public String calcMostActiveMonth(){}
//    public String calcIncome(){}
//    public String calcMostProfitMonth(){}
}
