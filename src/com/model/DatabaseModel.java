package com.model;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseModel {
    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3001", user = "root", password = "espera10";
    private static String sql = "CREATE DATABASE IF NOT EXISTS mms";

    public DatabaseModel() throws SQLException {
        initializeDatabase();
    }

    public static void addCategoriesSlots() throws SQLException {
        String auxString = null; int auxInt=0; String sql;
        Statement stm = connection.createStatement();

        for(int i=0; i<3; i++){
            if(i==0){ auxInt=15; auxString="Small";}
            if(i==1){ auxInt=5; auxString="Medium";}
            if(i==2){ auxInt=2; auxString="Large";}
            sql = "INSERT INTO mms.categories (name) VALUES ('" + auxString + "'); ";
            stm.executeUpdate(sql);
            for(int j=0; j<auxInt; j++){
                sql =  "INSERT INTO mms.slots (slotName,Categories_idCategories) VALUES ('" + auxString.charAt(0) + (j+1) + "'," + (i+1) + "); ";
                stm.executeUpdate(sql);
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    public static Connection createConnectionMySQL() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void initializeDatabase() throws SQLException {
        connection = createConnectionMySQL();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.execute();

        Statement statement = connection.createStatement();

        if(!tableExists(connection,"categories")) {
            sql = "CREATE TABLE `mms`.`categories` (`idCategories` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, PRIMARY KEY (`idCategories`));";
            statement.executeUpdate(sql);
        }else {
            System.out.println("A tabela Categories ja existe!");
        }

        if(!tableExists(connection,"slots")) {
            sql = "CREATE TABLE `mms`.`slots` (" +
                    " `idSlots` INT NOT NULL AUTO_INCREMENT," +
                    " `slotName` VARCHAR(45) NOT NULL," +
                    " `Categories_idCategories` INT NOT NULL," +
                    " PRIMARY KEY (`idSlots`)," +
                    " INDEX `idCategories_idx` (`Categories_idCategories` ASC) VISIBLE," +
                    " CONSTRAINT `idCategories`" +
                    " FOREIGN KEY (`Categories_idCategories`)" +
                    " REFERENCES `mms`.`categories` (`idCategories`)" +
                    " ON DELETE NO ACTION" +
                    " ON UPDATE NO ACTION);";
            statement.executeUpdate(sql);
        }else {
            System.out.println("A tabela Slots ja existe!");
        }

        if(!tableExists(connection,"reservations")) {
            sql = "CREATE TABLE `mms`.`reservations` (" +
                    "  `idReservations` INT NOT NULL AUTO_INCREMENT," +
                    "  `clientName` VARCHAR(45) NOT NULL," +
                    "  `checkInDate` DATE NOT NULL," +
                    "  `checkOutDate` DATE NOT NULL," +
                    "  `bill` FLOAT NOT NULL," +
                    "  `nif` INT NOT NULL," +
                    "  `vehicleRegistrationNumber` VARCHAR(45) NOT NULL," +
                    "  `state` INT NOT NULL," +
                    "  `Slots_idSlots` INT NOT NULL," +
                    "  PRIMARY KEY (`idReservations`)," +
                    "  INDEX `idSlots_idx` (`Slots_idSlots` ASC) VISIBLE," +
                    "  CONSTRAINT `idSlots`" +
                    "    FOREIGN KEY (`Slots_idSlots`)" +
                    "    REFERENCES `mms`.`slots` (`idSlots`)" +
                    "    ON DELETE NO ACTION" +
                    "    ON UPDATE NO ACTION);";
            statement.executeUpdate(sql);
        }else {
            System.out.println("A tabela Reservations ja existe!");
        }

        addCategoriesSlots();
    }
}
