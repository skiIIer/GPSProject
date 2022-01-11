package com.view;

import com.model.Model;
import com.model.Reservation;
import com.model.State;

import java.sql.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    private static Model model;
    private static Scanner scanner=new Scanner(System.in);

    public static int scanInt(){
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                if (scanner.next().compareToIgnoreCase("quit") == 0)
                    return -1;
            }
        }
    }

    public static void Interface_AddReservations(){
        String dateCheckIn;
        String name, category, vrn, intAux;
        int dayCI, monthCI, yearCI, dayCO, monthCO, yearCO, nif;


        System.out.println("---------------------------------------------------------------------\n" +
                "MMS / Reservations / Add\n" +
                "---------------------------------------------------------------------\n" +
                "Type 'quit' on any field to return to the Reservations submenu\n");
        while (true) {
            System.out.println("Check-In:");
            System.out.print("\tDay: ");
            dayCI = scanInt();
            if(dayCI==-1)
                return;
            System.out.print("\tMonth: ");
            monthCI = scanInt();
            if(monthCI==-1)
                return;
            System.out.print("\tYear: ");
            yearCI = scanInt();
            if(yearCI==-1)
                return;
            if (model.verifyDateCI(dayCI, monthCI, yearCI)) {
                dateCheckIn = dayCI+"-"+monthCI+"-"+yearCI;
                break;
            }
        }

        while (true) {
            System.out.println("Check-Out:");
            System.out.print("\tDay: ");
            dayCO = scanInt();
            if(dayCO==-1)
                return;
            System.out.print("\tMonth: ");
            monthCO = scanInt();
            if(monthCO==-1)
                return;
            System.out.print("\tYear: ");
            yearCO = scanInt();
            if(yearCO==-1)
                return;
            if (model.verifyDateCO(dayCO, monthCO, yearCO, dateCheckIn))
                break;
        }

        while (true) {
            System.out.print("Name: ");
            while (!scanner.hasNext()) scanner.next();
            name = scanner.nextLine();
            if(name.compareToIgnoreCase("quit")==0)
                return;
            if (model.verifyName(name))
                break;
        }

        while (true) {
            System.out.print("NIF / TIN: ");
            nif = scanInt();
            if(nif==-1)
                return;
            if (model.verifyNIF(nif))
                break;
        }

        while (true) {
            System.out.print("Vehicle Category (Large, Medium, Small): ");
            while (!scanner.hasNext()) scanner.next();
            category = scanner.next();
            if(category.compareToIgnoreCase("quit")==0)
                return;
            if (model.verifyCategory(category))
                break;
        }

        while (true) {
            System.out.print("Vehicle Registration Number / VRN (Format:XX-XXXX-XX): ");
            while (!scanner.hasNext()) scanner.next();
            vrn = scanner.next();
            if(vrn.compareToIgnoreCase("quit")==0)
                return;
            if (model.verifyVRN(vrn))
                break;

        }

        //Adds
        if(model.addReservation(name, Date.valueOf(dayCI+"-"+monthCI+"-"+yearCI), Date.valueOf(dayCO+"-"+monthCO+"-"+yearCO), 0, nif, vrn, State.SCHEDULED.getValue(), category))
            System.out.println("Reservation successfully made.");
        else
            System.out.println("\nNo slots available for the specified date.");
    }

    public static void Interface_ReservationsSubmenu(){
        int option;

        while (true) {
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Reservations\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose an option:\n\t1-Add Reservation\n\t2-View Reservations\n\t3-Quit");
            System.out.print("Option:");
            while (!scanner.hasNextInt()) scanner.next();
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    Interface_AddReservations();
                    break;
                case 2:
                    System.out.println(model.viewReservations());
                    break;
                case 3:
                    return;
                default:
                    System.out.println(option + " is not recognized as a command\n\n");
            }
        }
    }

    public static void Interface_Statistics() {
        int year;

        while (true) {
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Statistics\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose the year to consult its statistics or quit by typing 'quit'");
            System.out.print("Command: ");
            while (!scanner.hasNextInt()) scanner.next();
            year = scanner.nextInt();
            System.out.println("---------------------------------------------------------------------");
            if(model.verifyYear(year))
                System.out.println(model.viewStatistics(year));
            else
                System.out.println("Choose a valid year or quit by typing 'quit'");
        }
    }

    public static void Interface(){
        int option;

        while (true){
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Marina's Management System\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose an option:\n\t1-Reservations\n\t2-Refuel\n\t3-Consult Statistics\n\t4-Quit");
            System.out.print("Option:");
            while (!scanner.hasNextInt()) scanner.next();
            option = scanner.nextInt();
            switch (option){
                case 1:
                    Interface_ReservationsSubmenu();
                    break;
                case 2:
                    System.out.println("Refuel!!");
                    break;
                case 3:
                    Interface_Statistics();
                    break;
                case 4:
                    System.out.println("\n...\n");
                    return;
                default:
                    System.out.println(option + " is not recognized as a command\n\n");
            }

        }
    }

    public static void main(String[] args) {
        model = new Model();
        /*
        if(model.addReservation("Rui Pinto", Date.valueOf("2019-03-01"), Date.valueOf("2022-11-31"), 3, 987654321, "AP-27-SD", 2, "Large") &&
                model.addReservation("To Manel", Date.valueOf("2027-03-01"), Date.valueOf("2029-11-31"), 15.2, 234124721, "AS-27-SD", 2, "Medium"))

            System.out.println("Reservation successfully made.");
        else
            System.out.println("\nNo slots available for the specified date.");

        */

        System.out.println(model.addReservation("Rui Pinto", Date.valueOf("2037-03-01"), Date.valueOf("2038-11-31"), 3.2, 987654321, "AP-27-LL", 0, "Small"));
        System.out.println(model.addReservation("Rui Tavares", Date.valueOf("2037-03-01"), Date.valueOf("2038-11-31"), 3.2, 987654321, "PT-27-PT", 0, "Large"));
        System.out.println(model.addReservation("Joca Tavares", Date.valueOf("2037-03-01"), Date.valueOf("2038-11-31"), 3.2, 987654321, "AP-27-TY", 0, "Large"));


        //System.out.print(model.cancelReservation(6));

        //System.out.println(model.viewStatistics(2030));
        Interface();
        return;
    }
}
