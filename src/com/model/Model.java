package com.model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    CRUD crud;
    DatabaseModel databaseModel;

    public Model(){
        try {
            databaseModel = new DatabaseModel();//Initializes database
            crud = new CRUD(databaseModel.getConnection());//Initializes API for database operations
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addReservation(String name, Date checkIn, Date checkOut, double bill, int nif, String regNumber, int state, String category){
        int availableSlot;
        availableSlot = verifySlot(category, checkIn, checkOut);
        if(availableSlot!=0) {
            Reservation reservation = new Reservation(name, checkIn, checkOut, bill, nif, regNumber, state, category);
            crud.create(reservation, availableSlot);
            return true;
        } else
            return false;
    }
//    public boolean editReservation(){}
//    public boolean verifyFormat(){}

    public boolean cancelReservation(int id){
        if (crud.delete(id) == false)
            return false;

        return true;
    }

//    public String viewReservations(){}
//    public boolean refuel(){}
      public int verifySlot(String category, Date checkIn, Date checkOut){
        return databaseModel.verifySlot(category, checkIn, checkOut);
      }
//    public String viewStatistics(){}
//    public String calcMostCommonCat(){}
//    public String calcMostActiveMonth(){}
//    public String calcIncome(){}
//    public String calcMostProfitMonth(){}
}
