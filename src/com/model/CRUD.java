package com.model;

import java.sql.*;

public class CRUD {
    private static Connection connection;
    private static PreparedStatement pstm;

    public CRUD(Connection connection){
        this.connection = connection;
        pstm = null;
    }

    public static void create(Reservation reservation){
        //inserir isto na bd para o create funcionar
        //INSERT INTO mms.categories(idCategories, name) VALUES(1, 'Large');
        //INSERT INTO mms.slots (idSlots, Categories_idCategories) VALUES (1, 1);
        String sql = "INSERT INTO mms.reservations (idReservations, clientName, checkInDate, checkOutDate, bill, nif, vehicleRegistrationNumber, state, Slots_idSlots)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, 2);
            pstm.setString(2, reservation.getClientName());
            pstm.setDate(3, reservation.getCheckInDate());
            pstm.setDate(4, reservation.getCheckOutDate());
            pstm.setDouble(5, reservation.getBill());
            pstm.setInt(6, reservation.getNif());
            pstm.setString(7, reservation.getRegNumber());
            pstm.setInt(8, reservation.getState());
            pstm.setInt(9, 1);
            pstm.execute();
            //ResultSet rs = pstm.executeQuery();
            /*while (rs.next()) {
                System.out.println(rs.getInt("idCategories"));
                System.out.println(rs.getString("name"));
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean delete(int id){
        String sql = "DELETE FROM mms.reservations WHERE idReservations=2";

        try {
            pstm = connection.prepareStatement(sql);
            if (pstm.executeUpdate() == 0)
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
