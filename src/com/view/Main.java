package com.view;

import com.model.Model;

import java.sql.*;
import java.sql.Connection;

public class Main {
    private static Model model;
    public static void main(String[] args) {
        model = new Model();
        model.addReservation("Rui", Date.valueOf("2022-03-31"), Date.valueOf("2023-03-31"), 15.2, 234124721, "AS-27-SD", 2);
    }
}
