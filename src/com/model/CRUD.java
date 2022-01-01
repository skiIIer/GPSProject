package com.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CRUD {
    private static Connection connection;
    private static PreparedStatement pstm;

    public CRUD(){
        connection = DatabaseModel.getConnection();
        pstm = null;
    }
    public static void create(Reservation reservation){
        String sql = "INSERT INTO reservations (clientName, checkInDate, checkOutDate, bill, nif, vehicleRegistration, state)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            pstm.setString(1, reservation.getClientName());
            pstm.setDate(2, reservation.getCheckInDate());
            pstm.setDate(3, reservation.getCheckOutDate());
            pstm.setDouble(4, reservation.getBill());
            pstm.setInt(5, reservation.getNif());
            pstm.setString(6, reservation.getRegNumber());
            pstm.setInt(7, reservation.getState());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
