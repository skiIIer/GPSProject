package com.view;

import com.model.Model;

import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    private static Model model;
    private static Scanner scanner=new Scanner(System.in);

    public static void Interface_ReservationsSubmenu(){
        int option;

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
                    System.out.println("Add Reservations!!");
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
