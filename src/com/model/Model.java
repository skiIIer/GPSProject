package com.model;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    public boolean editReservation(Reservation reservation){
        return crud.edit(reservation);
    }
//    public boolean verifyFormat(){}

    public boolean verifyDateCI(int day, int month, int year){
        String date = day+"-"+month+"-"+year;
        java.util.Date dateCheckIn, dateNow;

        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            df.setLenient(false);
            dateCheckIn = df.parse(date);
        } catch (ParseException e) {
            return false;
        }

        dateNow = new java.util.Date();
        if(dateCheckIn.before(dateNow))
            return false;

        return true;
    }

    public String viewReservations(){
        ArrayList<Reservation> lista = crud.view();
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

    public boolean verifyDateCO(int day, int month, int year, String dateCheckInStr){
        String date = day+"-"+month+"-"+year;
        java.util.Date dateCheckOut, dateCheckIn;

        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            df.setLenient(false);
            dateCheckOut = df.parse(date);
            dateCheckIn = df.parse(dateCheckInStr);
        } catch (ParseException e) {
            return false;
        }

        if(!dateCheckOut.after(dateCheckIn))
            return false;

        return true;
    }

    public boolean verifyName(String name) {
        return name.length()<=45;
    }

    public boolean verifyNIF(int NIF){
        return String.valueOf(NIF).length()==9;
    }

    public boolean verifyCategory(String category){
        return (category.compareToIgnoreCase("small") == 0 || category.compareToIgnoreCase("medium") == 0 || category.compareToIgnoreCase("large") == 0);
    }

    public boolean verifyVRN(String vrn){
        return vrn.matches("^[a-zA-Z]{2}-[0-9]{4}-[a-zA-z]{2}$");
    }

    public boolean cancelReservation(int id){
        return crud.delete(id);
    }

//    public String viewReservations(){}
//    public boolean refuel(){}
      public int verifySlot(String category, Date checkIn, Date checkOut){
        return databaseModel.verifySlot(category, checkIn, checkOut);
      }

    public String viewStatistics(int year){
        String result = "";

        result += calcIncome(year);
        result += calcMostProfitMonth(year);

        return result;
    }
//    public String calcMostCommonCat(){}
//    public String calcMostActiveMonth(){}
    public String calcIncome(int year){
        DecimalFormat df = new DecimalFormat("0.00");

        String result = "Annual Income: ";
        double annualIncome = crud.viewAnnualIncome(year);
        if(annualIncome==-1)
            result += "No Information Available\n";
        else
            result += df.format(annualIncome) + " $\n";

        return result;
    }

    public String calcMostProfitMonth(int year){
        return "Most profitable month: " + crud.viewMostProfitableMonth(year);
    }
}
