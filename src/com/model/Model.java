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
//    public boolean editReservation(int id){
//        
//    }
//    public boolean verifyFormat(){}

    public boolean cancelReservation(int id){
        if (crud.delete(id) == false)
            return false;

        return true;
    }

    public String viewReservations(){
        ArrayList<Reservation> lista = new ArrayList<Reservation>();
        lista = crud.view();
        String s = "";
        for(Reservation x : lista){
            s += x.getId() +
                    " | " + x.getClientName() +
                    " | " + x.getCheckInDate() +
                    " | " + x.getCheckOutDate() +
                    " | " + Math.round(x.getBill()*100.0) / 100.0 +
                    " | " + x.getNif() +
                    " | " + x.getRegNumber() +
                    " | " + x.getState() + "\n";
        }
        return s;

    }
//    public boolean refuel(){}
//    public String verifySlot(){}
//    public String viewStatistics(){}
//    public String calcMostCommonCat(){}
//    public String calcMostActiveMonth(){}
//    public String calcIncome(){}
//    public String calcMostProfitMonth(){}
}
