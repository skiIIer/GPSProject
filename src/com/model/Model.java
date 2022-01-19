package com.model;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Model {
    CRUD crud;
    DatabaseModel databaseModel;
    java.util.Date dateNow;

    public Model(){
        try {
            dateNow = new java.util.Date();
            databaseModel = new DatabaseModel();//Initializes database
            crud = new CRUD(DatabaseModel.getConnection());//Initializes API for database operations
            updateState();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateState() {
        ArrayList<Reservation> list = CRUD.read();
        for(Reservation r : list){
            if(r.getCheckInDate().before(dateNow) && r.getCheckOutDate().after(dateNow))
                CRUD.update(r.getId(),State.ACTIVE);
            else if(r.getCheckOutDate().before(dateNow))
                CRUD.update(r.getId(),State.DONE);
        }
    }



    public int nmrDias(Date checkIn, Date checkOut){
        long diff = checkOut.getTime() - checkIn.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public boolean addReservation(String name, Date checkIn, Date checkOut, double bill, int nif, String regNumber, int state, String category){
        int availableSlot;
        availableSlot = verifySlot(regNumber, category, checkIn, checkOut);
        if(availableSlot!=0) {
            Reservation reservation = new Reservation(name, checkIn, checkOut, calcBill(nmrDias(checkIn, checkOut), category), nif, regNumber, state, category);
            CRUD.create(reservation, availableSlot);
            return true;
        } else
            return false;
    }
    public boolean editReservation(Reservation reservation){
        reservation.setBill(calcBill(nmrDias(reservation.getCheckInDate(), reservation.getCheckOutDate()), reservation.getCategory()));
        return CRUD.update(reservation);
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

        return dateCheckIn.after(dateNow);
    }

    public String viewReservations(){
        ArrayList<Reservation> lista = CRUD.read();
        String s = "";
        DecimalFormat df = new DecimalFormat("0.00");
        for(Reservation x : lista){
            s += "\tID: " + x.getId() +
                    " | Name: " + x.getClientName() +
                    " | NIF/TIN: " + x.getNif() +
                    " | VRN: " + x.getRegNumber() +
                    " | Slot: " + x.getSlot()+
                    "\n\tCheck-In: " + x.getCheckInDate() +
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

        return dateCheckOut.after(dateCheckIn);
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
        return 1 <= year && year <= yearNow;
    }

    public boolean cancelReservation(int id){
        return CRUD.delete(id);
    }

      public boolean refuel(String VRN, Double fuelInEuros){
        int id = DatabaseModel.getIdReservation(VRN);
        if(id!=0)
            return CRUD.update(id, fuelInEuros);
        return false;
      }

    public int calcBill(int nmrDays, String category){

        //int id = DatabaseModel.getIdReservation(VRN);
        //if(id!=0)
            return DatabaseModel.calcBill(nmrDays, category);
        //return 0;
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
        double annualIncome = CRUD.viewAnnualIncome(year);
        if(annualIncome==-1)
            result += "No Information Available\n";
        else
            result += df.format(annualIncome) + " $\n";

        return result;
    }

    public String calcMostProfitMonth(int year){
        return "\t\t\tMost profitable month: " + crud.viewMostProfitableMonth(year) + "\n";
    }

    public String viewReservationByState(State state){
        ArrayList<Reservation> lista = crud.viewReservationByState(state);
        String s = "";
        for(Reservation x : lista){
            s += "\tName: " + x.getClientName() +
                    " | NIF/TIN: " + x.getNif() +
                    " | VRN: " + x.getRegNumber() +
                    " | Slot: " + x.getSlot() + "\n";
        }
        return s;
    }

    public Reservation getReservation(int id){
        return crud.getReservation(id);
    }

    public Reservation getReservation(String vrn){
        return crud.getReservation(vrn);
    }

    public String showDetails(Reservation aux){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return "ID " + aux.getId() +
                "\nCheck-In: " + df.format(aux.getCheckInDate()) +
                "\nCheck-Out: " + df.format(aux.getCheckOutDate()) +
                "\nName: " + aux.getClientName() +
                "\nNIF / TIN: " + aux.getNif() +
                "\nVehicle Category(Large, Medium, Small): " + aux.getCategory() +
                "\nVehicle Registration Number / VRN: " + aux.getRegNumber() +
                "\nReserved Slot: " + aux.getSlot() +
                "\n---------------------------------------------------------------------";
    }
}
