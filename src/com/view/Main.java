package com.view;

import com.model.CRUD;
import com.model.DatabaseModel;

import java.sql.*;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        try{
            new DatabaseModel();//Initializes database
            new CRUD();//Initializes API for database operations
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
