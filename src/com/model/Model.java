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
    java.util.Date dateNow;

    public Model(){
        try {
            dateNow = new java.util.Date();
            databaseModel = new DatabaseModel();//Initializes database
            crud = new CRUD(databaseModel.getConnection());//Initializes API for database operations
            updateState();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateState() {
        ArrayList<Reservation> list = crud.read();
        for(Reservation r : list){
            if(r.getCheckInDate().before(dateNow) && r.getCheckOutDate().after(dateNow))
                crud.update(r.getId(),State.ACTIVE);
            else if(r.getCheckOutDate().before(dateNow))
                crud.update(r.getId(),State.DONE);
        }
    }

    public boolean addReservation(String name, Date checkIn, Date checkOut, double bill, int nif, String regNumber, int state, String category){
        int availableSlot;
        availableSlot = verifySlot(regNumber, category, checkIn, checkOut);
        if(availableSlot!=0) {
            Reservation reservation = new Reservation(name, checkIn, checkOut, bill, nif, regNumber, state, category);
            crud.create(reservation, availableSlot);
            return true;
        } else
            return false;
    }
    public boolean editReservation(Reservation reservation){
        return crud.update(reservation);
    }
//    public boolean verifyFormat(){}

    public boolean verifyDateCI(int day, int month, int year){
        String date = day+"-"+month+"-"+year;
        java.util.Date dateCheckIn;

        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            df.setLenient(false);
            dateCheckIn = df.parse(date);
        } catch (ParseException e) {
            return false;
        }

        if(!dateCheckIn.after(dateNow))
            return false;

        return true;
    }

    public String viewReservations(){
        ArrayList<Reservation> lista = crud.read();
        String s = "";
        DecimalFormat df = new DecimalFormat("0.00");
        for(Reservation x : lista){
            s += "ID: " + x.getId() +
                    " | Name: " + x.getClientName() +
                    " | NIF/TIN: " + x.getNif() +
                    " | VRN: " + x.getRegNumber() +
                    " | Slot: " + x.getSlot()+
                    "\nCheck-In: " + x.getCheckInDate() +
                    " | Check-Out: " + x.getCheckOutDate() +
                    " | Total Bill: " + /*Math.round(*/df.format(x.getBill())/*100.0) / 100.0*/ + " $\n";
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

    public boolean verifyYear(int year){
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        if(/*yearNow<year ||*/ 1500>year)
            return false;
        return true;
    }

    public boolean cancelReservation(int id){
        return crud.delete(id);
    }

      public boolean refuel(String VRN, int fuelInEuros){
        int id = DatabaseModel.getIdReservation(VRN);
        if(id!=0)
            return CRUD.update(id, fuelInEuros);
        return false;
      }
      
    public int calcBill(String VRN){
        int id = DatabaseModel.getIdReservation(VRN);
        if(id!=0)
            return DatabaseModel.calcBill(id);
        return 0;
    }

      public int verifySlot(String vrn, String category, Date checkIn, Date checkOut){
        if(!databaseModel.verifyExistingReservationVrnDates(vrn, checkIn, checkOut))
            return databaseModel.verifySlot(category, checkIn, checkOut);
        return 0;
      }

    public String viewStatistics(int year){
        String result = year + " Statistics:\n";

        result += calcMostCommonCat(year);
        result +=calcMostActiveMonth(year);
        result += "\t-> Marina's Economic Results:\n";
        result += calcIncome(year);
        result += calcMostProfitMonth(year);


        return result;
    }
    public String calcMostCommonCat(int year){
        String result = "\t-> Most common vehicle category: ";

        result += crud.mostCommonCat(year) + "\n";

        return result;
    }
    public String calcMostActiveMonth(int year){
        String result = "\t-> Most Active Month: ";

        result += crud.mostActiveMonth(year) + "\n";

        return result;
    }
    public String calcIncome(int year){
        DecimalFormat df = new DecimalFormat("0.00");

        String result = "\t\t\tAnnual Income: ";
        double annualIncome = crud.viewAnnualIncome(year);
        if(annualIncome==-1)
            result += "No Information Available\n";
        else
            result += df.format(annualIncome) + " $\n";

        return result;
    }

    public String calcMostProfitMonth(int year){
        return "\t\t\tMost profitable month: " + crud.viewMostProfitableMonth(year) + "\n";
    }

    public ArrayList viewReservationByState(State state){
        return crud.viewReservationByState(state);
    }
}
