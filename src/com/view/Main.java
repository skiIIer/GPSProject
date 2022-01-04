package com.view;

import com.model.Model;
import com.model.Reservation;

import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    private static Model model;
    private static Scanner scanner=new Scanner(System.in);

    public static void Interface_ReservationsSubmenu(){
        int option;

        String name, category, vrn;

        while (true){
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Reservations\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose an option:\n\t1-Add Reservation\n\t2-View Reservations\n\t3-Quit");
            System.out.print("Option:");
            while (!scanner.hasNextInt()) scanner.next();
            option = scanner.nextInt();
            switch (option){
                case 1:
                    System.out.println("---------------------------------------------------------------------\n" +
                            "MMS / Reservations / Add\n" +
                            "---------------------------------------------------------------------\n" +
                            "Type 'quit' on any field to return to the Reservations submenu\n");
                    System.out.println("Check-In:");
                    System.out.print("\tDay: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int dayCI = scanner.nextInt();
                    System.out.print("\tMonth: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int monthCI = scanner.nextInt();
                    System.out.print("\tYear: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int yearCI = scanner.nextInt();

                    System.out.println("Check-Out:");
                    System.out.print("\tDay: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int dayCO = scanner.nextInt();
                    System.out.print("\tMonth: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int monthCO = scanner.nextInt();
                    System.out.print("\tYear: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int yearCO = scanner.nextInt();

                    System.out.print("Name: ");
                    while (!scanner.hasNextLine()) name = scanner.nextLine();

                    System.out.print("NIF / TIN: ");
                    while (!scanner.hasNextInt()) scanner.next();
                    int nif = scanner.nextInt();

                    System.out.print("Vehicle Category (Large, Medium, Small): ");
                    while(!scanner.hasNext()) category = scanner.next();

                    System.out.print("Vehicle Registration Number / VRN (Format:XX-XXXX-XX): ");
                    while(!scanner.hasNext()) vrn = scanner.next();

                    break;
                case 2:
                    System.out.println("View Reservations!!");
                    break;
                case 3:
                    return;
                default:
                    System.out.println(option + " is not recognized as a command\n\n");
            }
        }
    }

    public static void Interface(){
        int option;

        while (true){
            System.out.println("---------------------------------------------------------------------\n" +
                    "MMS / Marina's Management System\n" +
                    "---------------------------------------------------------------------");
            System.out.println("Choose an option:\n\t1-Reservations\n\t4-Quit");
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
                    System.out.println("Consult Statistics!!");
                    break;
                case 4:
                    return;
                default:
                    System.out.println(option + " is not recognized as a command\n\n");
            }

        }
    }

    public static void main(String[] args) {
        model = new Model();
        model.addReservation("Rui", Date.valueOf("2022-03-31"), Date.valueOf("2023-03-31"), 15.2, 234124721, "AS-27-SD", 2);
        //model.addReservation("Bob", Date.valueOf("2022-04-23"), Date.valueOf("2023-02-12"), 15.2, 521124721, "SA-27-SD", 2);
        //model.cancelReservation(2);

        //Interface();
        return;
    }
}
