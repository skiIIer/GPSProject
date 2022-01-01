package com.view;

import java.sql.*;
import java.sql.Connection;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306", user = "root", password = "root";
    private static String sql = "CREATE DATABASE IF NOT EXISTS mms";

    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    private static Connection createConnectionMySQL() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.execute();

        Statement statement = connection.createStatement();

        if(!tableExists(connection,"categories")) {
            sql = "CREATE TABLE `mms`.`categories` (`idCategories` INT NOT NULL, `name` VARCHAR(45) NOT NULL, PRIMARY KEY (`idCategories`));";
            statement.executeUpdate(sql);
        }else {
            System.out.println("A tabela Categories ja existe!");
        }

        if(!tableExists(connection,"slots")) {
            sql = "CREATE TABLE `mms`.`slots` (" +
                    " `idSlots` INT NOT NULL," +
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
                    "  `idReservations` INT NOT NULL," +
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

        return connection;
    }

    public static void main(String[] args) {
        try{
            Connection conn = createConnectionMySQL();  //Initializes database
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
