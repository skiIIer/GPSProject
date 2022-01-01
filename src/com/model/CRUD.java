package com.model;

public class CRUD {
    public CRUD(){}
    public void create(Reservation reservation){
        String sql = "INSERT INTO reservations (clientName, checkInDate, checkOutDate, bill, nif, vehicleRegistration, state)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        DatabaseModel.getConnection();
    }
}
