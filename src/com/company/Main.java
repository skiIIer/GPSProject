package com.company;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Main {

    static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3001", user = "root", password = "espera10"; //ALTERAR

        String sql = "CREATE DATABASE IF NOT EXISTS mms";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.execute();

            Statement statement = conn.createStatement();

            if(!tableExists(conn,"categories")) {
                sql = "CREATE TABLE `mms`.`categories` (`idCategories` INT NOT NULL, `name` VARCHAR(45) NOT NULL, PRIMARY KEY (`idCategories`));";
                statement.executeUpdate(sql);
            }else {
                System.out.println("A tabela Categories ja existe!");
            }

            if(!tableExists(conn,"slots")) {
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

            if(!tableExists(conn,"reservations")) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
