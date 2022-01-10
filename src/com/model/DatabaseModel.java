package com.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseModel {
    private static Connection connection;
    private static PreparedStatement pstm;
    private static Statement stm;
    private static ResultSet resultSet;
    private static final String url = "jdbc:mysql://localhost:3306", user = "root", password = "root";
    private static String sql = "CREATE DATABASE IF NOT EXISTS mms";

    public DatabaseModel() throws SQLException {
        initializeDatabase();
        resultSet=null;
        pstm=null;
        stm=null;
    }

    public static int calcBill(int id){
        int bill = getBill(id);
        String sql = "SELECT c.basePrice\n" +
                "FROM mms.categories c \n" +
                "INNER JOIN mms.reservations r \n" +
                "ON c.name = r.category " +
                "AND r.idReservations = "+id;

        try {
            resultSet = stm.executeQuery(sql);
            if(!resultSet.next())
                return 0;
            return resultSet.getInt("basePrice") * bill;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getBill(int id){
        String sql = "SELECT r.bill" +
                "FROM mms.reservations r" +
                "WHERE r.idReservations = " + id + ";";

        try {
            stm = connection.createStatement();

            resultSet = stm.executeQuery(sql);
            if(!resultSet.next())
                return 0;
            return resultSet.getInt("bill");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIdReservation(String VRN){
        String sql = "SELECT r.idReservations\n" +
                "FROM mms.reservations r \n" +
                "WHERE r.vehicleRegistrationNumber = '" + VRN +"';";

        try {
            stm = connection.createStatement();

            resultSet = stm.executeQuery(sql);
            if(!resultSet.next())
                return 0;
            return resultSet.getInt("idReservations");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int verifySlot(String category, Date checkI, Date checkO){
        List al = new ArrayList<Integer>();

        String sql = "SELECT s.idSlots \n" +
                "FROM mms.slots s\n" +
                "INNER JOIN mms.categories c\n" +
                "ON c.name = '" + category + "' and c.idCategories = s.Categories_idCategories;";

        try {
            stm = connection.createStatement();

            resultSet = stm.executeQuery(sql);

            while(resultSet.next()){
                al.add(resultSet.getInt("idSlots"));
            }

            for(Object i: al){
                sql = "SELECT s.idSlots \n" +
                        "FROM mms.slots s\n" +
                        "WHERE s.idSlots = " + i + " AND NOT EXISTS \n" +
                        "(SELECT r.Slots_idSlots\n" +
                        "FROM mms.reservations r\n" +
                        "WHERE s.idSlots = r.Slots_idSlots and \n" +
                        "((r.checkInDate > " + checkI + " or r.checkInDate < " + checkO +")" +
                        "and (r.checkOutDate > " + checkI + " or r.checkOutDate < " + checkO + ")))";

                resultSet = stm.executeQuery(sql);
                while(resultSet.next()){
                    return resultSet.getInt("idSlots");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public static void addCategoriesSlots() throws SQLException {
        String auxString = null;
        int auxInt=0, auxIntBasePrice=0;
        String sql;
        int cont=0;
        Statement stm = connection.createStatement();
        ResultSet rs;

        for(int i=0; i<3; i++){
            if(i==0){ auxInt=15; auxString="Small"; auxIntBasePrice=100;}
            if(i==1){ auxInt=5; auxString="Medium"; auxIntBasePrice=200;}
            if(i==2){ auxInt=2; auxString="Large"; auxIntBasePrice=300;}
            sql = "SELECT * FROM mms.categories WHERE idCategories=" + (i+1);
            rs = stm.executeQuery(sql);
            if(!rs.next()) {
                sql = "INSERT INTO mms.categories (name, basePrice) VALUES ('" + auxString + "', " + auxIntBasePrice + "); ";
                stm.executeUpdate(sql);
            }
            for(int j=0; j<auxInt; j++){
                cont++;
                sql = "SELECT * FROM mms.slots WHERE idSlots=" + cont;
                rs = stm.executeQuery(sql);
                if(!rs.next()) {
                    sql = "INSERT INTO mms.slots (slotName,Categories_idCategories) VALUES ('" + auxString.charAt(0) + (j + 1) + "'," + (i + 1) + "); ";
                    stm.executeUpdate(sql);
                }
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
            sql = "CREATE TABLE `mms`.`categories` (" +
                    " `idCategories` INT NOT NULL AUTO_INCREMENT, " +
                    " `name` VARCHAR(45) NOT NULL, " +
                    " `basePrice` FLOAT NOT NULL, " +
                    " PRIMARY KEY (`idCategories`));";
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
                    "  `category` VARCHAR(45) NOT NULL," +
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
