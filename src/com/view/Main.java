package com.view;

import com.model.Model;
import com.model.Reservation;
import com.model.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    private static Model model;
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumericDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int scanInt() {
        while (true) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                if (scanner.next().compareToIgnoreCase("quit") == 0)
                    return -1;
            }
        }
    }
    public static void Interface_EditReservations(int id){
        String command;
        Reservation aux;
        int dayCI, monthCI, yearCI, auxInt, flag=0, dayCO, monthCO, yearCO;
        String dateCheckIn = null, dateCheckOut, auxStr;
        boolean COafterCI=false;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("---------------------------------------------------------------------\n" +
                "MMS / Reservations / Edit\n" +
                "---------------------------------------------------------------------");
        aux = model.getReservation(id);
        if(aux==null){
            System.out.println("Error selecting a Reservation");
            return;
        }
        System.out.println(model.showDetails(aux));
        System.out.println("Choose the field to edit, 'help' to check available commands or quit by typing 'quit'");
        System.out.print("Command: ");
        command = scanner.next();
        while(!command.equalsIgnoreCase("quit")){
            flag=0;
            switch (command){
                case "name":
                    System.out.print("Name: ");
                    aux.setClientName(scanner.next());
                    break;
                case "check-in":
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
                        dateCheckIn = yearCI+"-"+monthCI+"-"+dayCI;

                        if(Date.valueOf(dateCheckIn).before(aux.getCheckOutDate())){
                            aux.setCheckInDate(Date.valueOf(dateCheckIn));
                            break;
                        }
                        else{
                            System.out.println("Check-out Date must be after Check-In Date");
                            COafterCI = true;
                        }
                    }
                    else{
                        System.out.println("Invalid date");
                        break;
                    }

                case "check-out":
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
                    if(!COafterCI)
                            dateCheckIn = aux.getCheckInDate().toString();
                    dateCheckOut = yearCO+"-"+monthCO+"-"+dayCO;
                    if (Date.valueOf(dateCheckOut).after(Date.valueOf(dateCheckIn))) {
                        if(COafterCI)
                            aux.setCheckInDate(Date.valueOf(dateCheckIn));
                        aux.setCheckOutDate(Date.valueOf(dateCheckOut));
                        COafterCI=false;
                        break;
                    }
                    System.out.println("Data invalida");
                    break;
                case "nif":
                    do{
                        System.out.print("NIF: ");
                        auxInt = scanInt();
                        flag=1;
                        if(auxInt==-1){
                            flag=0;
                            break;
                        }
                    } while(!model.verifyNIF(auxInt));
                    if(flag==1)
                        aux.setNif(auxInt);
                    break;
                case "vrn":

                    do{
                        System.out.print("VRN: ");
                        auxStr = scanner.next();
                        flag=1;
                        if(auxStr.equals("quit")){
                            flag=0;
                            break;
                        }
                    } while(!model.verifyVRN(auxStr));
                    if(flag==1)
                        aux.setRegNumber(auxStr.toUpperCase());
                    break;
                case "confirm":
                    model.editReservation(aux);
                    return;
                case "help":
                    System.out.println("Commands: \n\tname | check-in | check-out | nif | vrn | details | confirm");
                    break;
                case "details":
                    System.out.println(model.showDetails(aux));
                    break;
                default:
                    System.out.println("Invalid command\n");
            }
            System.out.print("Command: ");
            command = scanner.next();
        }
        System.out.println(model.showDetails(aux));
        System.out.print("Apply configurations?\nCommand (yes/no): ");
        command = scanner.next();
        if(command.equalsIgnoreCase("yes"))
            model.editReservation(aux);
        else
            return;
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
            if (dayCI == -1)
                return;
            System.out.print("\tMonth: ");
            monthCI = scanInt();
            if (monthCI == -1)
                return;
            System.out.print("\tYear: ");
            yearCI = scanInt();
            if (yearCI == -1)
                return;
            if (model.verifyDateCI(dayCI, monthCI, yearCI)) {
                dateCheckIn = dayCI + "-" + monthCI + "-" + yearCI;
                break;
            }
        }

        while (true) {
            System.out.println("Check-Out:");
            System.out.print("\tDay: ");
            dayCO = scanInt();
            if (dayCO == -1)
                return;
            System.out.print("\tMonth: ");
            monthCO = scanInt();
            if (monthCO == -1)
                return;
            System.out.print("\tYear: ");
            yearCO = scanInt();
            if (yearCO == -1)
                return;
            if (model.verifyDateCO(dayCO, monthCO, yearCO, dateCheckIn))
                break;
        }

        while (true) {
            System.out.print("Name: ");
            name = scanner.nextLine();
            if (name.compareToIgnoreCase("quit") == 0)
                return;
            if (model.verifyName(name))
                break;
        }

        while (true) {
            System.out.print("NIF / TIN: ");
            nif = scanInt();
            if (nif == -1)
                return;
            if (model.verifyNIF(nif))
                break;
        }

        while (true) {
            System.out.print("Vehicle Category (Large, Medium, Small): ");
            while (!scanner.hasNext()) scanner.next();
            category = scanner.next();
            if (category.compareToIgnoreCase("quit") == 0)
                return;
            if (model.verifyCategory(category))
                break;
        }

        while (true) {
            System.out.print("Vehicle Registration Number / VRN (Format:XX-XXXX-XX): ");
            while (!scanner.hasNext()) scanner.next();
            vrn = scanner.next();
            if (vrn.compareToIgnoreCase("quit") == 0)
                return;
            if (model.verifyVRN(vrn))
                break;

        }

        //Adds
        if (model.addReservation(name, Date.valueOf(yearCI + "-" + monthCI + "-" + dayCI), Date.valueOf(yearCO + "-" + monthCO + "-" + dayCO), 0, nif, vrn, State.SCHEDULED.getValue(), category))
            System.out.println("Reservation successfully made.");
        else
            System.out.println("\nNo slots available for the specified date.");
    }

    public static void Interface_ViewReservations() {
        String command = "", cmd = "", value = "";
        int val;

        System.out.println("---------------------------------------------------------------------\n" +
                "MMS / Reservations / View\n" +
                "---------------------------------------------------------------------");
        System.out.println("Reservations:");
        System.out.println(model.viewReservations());
        System.out.println("---------------------------------------------------------------------\n" +
                "Commands available:\n\t-> search <VRN>\n\t-> edit <id>\n\t-> cancel <id>\n\t-> quit");
        while (true) {
            System.out.print("Command: ");
            //while (scanner.hasNext())
            //command += scanner.next();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            try {
                command = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.trim().split("\\s+").length == 2) {
                String[] arr = command.split(" ", 2);
                cmd = arr[0];
                value = arr[1];

                if (cmd.compareToIgnoreCase("search") == 0) {
                    if (model.verifyVRN(value)) {
                        Reservation r = model.getReservation(value);
                        if(r!=null)
                            System.out.println(model.showDetails(r));
                        else
                            System.out.println("Please insert a valid VRN with an active Reservation");
                    }else
                        System.out.println("Please insert a valid VRN with an active Reservation");

                } else if (cmd.compareToIgnoreCase("edit") == 0) {
                    if (isNumeric(value)) {
                        val = Integer.parseInt(value);
                        Interface_EditReservations(val);
                    } else
                        System.out.println("Please insert a valid id");

                } else if (cmd.compareToIgnoreCase("cancel") == 0) {
                    if (isNumeric(value)) {
                        val = Integer.parseInt(value);
                        System.out.print("Apply configurations?\nCommand (yes/no): ");
                        command = scanner.next();
                        if(command.equalsIgnoreCase("yes")) {
                            if (model.cancelReservation(val))
                                System.out.println("Reservation with id " + val + " cancelled with success");
                            else
                                System.out.println("Reservation with id " + val + " not found");
                        } else
                            return;
                    }
                } else
                    System.out.println(cmd + " is not recognized as a command\n\n");
            } else{
                if (command.compareToIgnoreCase("quit") == 0)
                    return;
                System.out.println(command + " is not recognized as a command\n\n");
            }
        }
    }

    public static void Interface_ReservationsSubmenu() {
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
                    Interface_ViewReservations();
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

            year = scanInt();
            System.out.println("---------------------------------------------------------------------");
            if(year == -1)
                break;
            else if (model.verifyYear(year))
                System.out.println(model.viewStatistics(year));
            else
                System.out.println("Choose a valid year or quit by typing 'quit'");
        }
    }

    private static void Interface_Refuel() {
        String command = "", cmd, vrn, value;
        Double b;

        System.out.println("---------------------------------------------------------------------\n" +
                "MMS / Refuel\n" +
                "---------------------------------------------------------------------");
        System.out.print(model.viewReservationByState(State.ACTIVE));
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Commands available:");
        System.out.println("\t->refuel <VRN> <Valor>\n\t->quit");

        while (true) {
            System.out.print("Command: ");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            try {
                command = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.trim().split("\\s+").length == 3) {
                String[] arr = command.split(" ", 3);
                cmd = arr[0];
                vrn = arr[1];
                value = arr[2];

                if (cmd.compareToIgnoreCase("refuel") == 0) {
                    if (model.verifyVRN(vrn))
                        if (isNumericDouble(value)) {
                            b = Double.parseDouble(value);
                            if (model.refuel(vrn, b))
                                System.out.println("Boat with vrn " + vrn + " refueled with success");
                            else
                                System.out.println("Boat with vrn " + vrn + " not found");
                        } else
                            System.out.println("Please insert a valid VRN");
                }
            }
            else {
                if (command.compareToIgnoreCase("quit") == 0){
                    break;
                }
                System.out.println(command + " is not recognized as a command\n\n");
            }
        }
        return;
    }


    public static void Interface() {
        int option;

        while (true) {
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Marina's Management System\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose an option:\n\t1-Reservations\n\t2-Refuel\n\t3-Consult Statistics\n\t4-Quit");
            System.out.print("Option:");
            while (!scanner.hasNextInt()) scanner.next();
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    Interface_ReservationsSubmenu();
                    break;
                case 2:
                    Interface_Refuel();
                    break;
                case 3:
                    Interface_Statistics();
                    break;
                case 4:
                    System.out.println("\nShutting down...");
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

        //System.out.println(model.addReservation("Rui Pinto", Date.valueOf("2010-03-01"), Date.valueOf("2038-11-31"), 3.2, 987654321, "AL-2712-SP", 0, "Small"));
        //System.out.println(model.addReservation("Rui Tavares", Date.valueOf("2037-03-01"), Date.valueOf("2038-11-31"), 3.2, 987654321, "AP-27-SP", 0, "Small"));

        //System.out.print(model.cancelReservation(6));
        //System.out.println(model.viewStatistics(2030));

        Interface();
        return;
    }
}
