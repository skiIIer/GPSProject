package com.model;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

public class CRUD {
    private static Connection connection;
    private static PreparedStatement pstm;

    public CRUD(Connection connection){
        CRUD.connection = connection;
        pstm = null;
    }

    public static void create(Reservation reservation, int availableSlot){
        String sql = "INSERT INTO mms.reservations (clientName, checkInDate, checkOutDate, bill, nif, vehicleRegistrationNumber, state, category, Slots_idSlots)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            pstm = connection.prepareStatement(sql);
            //pstm.setInt(1, 2);
            pstm.setString(1, reservation.getClientName());
            pstm.setDate(2, reservation.getCheckInDate());
            pstm.setDate(3, reservation.getCheckOutDate());
            pstm.setDouble(4, reservation.getBill());
            pstm.setInt(5, reservation.getNif());
            pstm.setString(6, reservation.getRegNumber());
            pstm.setInt(7, reservation.getState());
            pstm.setString(8, reservation.getCategory());
            pstm.setInt(9, availableSlot);
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }/*finally {
            try{
                if(pstm!=null)
                    pstm.close();
                if(connection!=null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static boolean delete(int id){
        Date data;
        LocalDate dataCheckIn;
        LocalDate dataAtual = LocalDate.now();

        String sql1 = "SELECT * FROM mms.reservations WHERE idReservations = ?";
        String sql2 = "DELETE FROM mms.reservations WHERE idReservations = ?";

        try {
            pstm = connection.prepareStatement(sql1);
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();

            //verifica se id existe
            if (!rs.next())
                return false;

            //converter data em LocalDate
            data = rs.getDate("checkInDate");
            dataCheckIn = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();


            //reserva ativa nao se pode cancelar
            if(dataCheckIn.isBefore(dataAtual)){
                return false;
            }

            pstm = connection.prepareStatement(sql2);
            pstm.setInt(1, id);

            //delete
            if (pstm.executeUpdate() == 0)
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static ArrayList read(){
        String sql="SELECT * FROM mms.reservations ORDER BY checkInDate";
        String result;
        ArrayList<Reservation> lista= new ArrayList<Reservation>();
        Reservation reservation;

        try {
            pstm=connection.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                reservation = new Reservation(
                        rs.getString("clientName"),
                        rs.getDate("checkInDate"),
                        rs.getDate("checkOutDate"),
                        rs.getFloat("bill"),
                        rs.getInt("nif"),
                        rs.getString("vehicleRegistrationNumber"),
                        rs.getInt("state"),
                        rs.getString("category"),
                        read(rs.getInt("idReservations")));
                reservation.setId(rs.getInt("idReservations"));

                lista.add(reservation);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    private static String read(int idReservations) {
        String sql="SELECT s.slotName FROM mms.slots s INNER JOIN mms.reservations r ON r.idReservations = " + idReservations + " AND s.idSlots = r.Slots_idSlots";

        try {
            Statement stm = connection.createStatement();

            ResultSet resultSet = stm.executeQuery(sql);
            if(resultSet.next())
                return resultSet.getString("slotName");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "No Information Available";
    }

    public static ArrayList read(State state){
        String sql="SELECT * FROM mms.reservations WHERE state=" + state.getValue() + " ORDER BY checkInDate";
        String result;
        ArrayList<Reservation> lista= new ArrayList<Reservation>();
        Reservation reservation;

        try {
            pstm=connection.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                reservation = new Reservation(
                        rs.getString("clientName"),
                        rs.getDate("checkInDate"),
                        rs.getDate("checkOutDate"),
                        rs.getFloat("bill"),
                        rs.getInt("nif"),
                        rs.getString("vehicleRegistrationNumber"),
                        rs.getInt("state"),
                        rs.getString("category"));
                reservation.setId(rs.getInt("idReservations"));

                lista.add(reservation);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean update(int id, Double fuelInEuros){
        String sql = "UPDATE mms.reservations " +
                "SET bill = bill + ? " +
                "WHERE idReservations = ?";

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setDouble(1, fuelInEuros);
            pstm.setInt(2, id);

            if (pstm.execute())
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String mostCommonCat(int year) {
        int large = 0, medium = 0, small = 0;
        String result = "";
        String sql = "SELECT COUNT(category) AS x FROM mms.reservations WHERE category = ? AND year(checkOutDate) = ?;";
        ResultSet rs;

        try {
            pstm=connection.prepareStatement(sql);

            pstm.setString(1,"Large");
            pstm.setInt(2,year);
            rs = pstm.executeQuery();
            if (!rs.next())
                return "Error";
            large = rs.getInt("x");

            pstm.setString(1, "Medium");
            pstm.setInt(2,year);
            rs = pstm.executeQuery();
            if (!rs.next())
                return "Error";
            medium = rs.getInt("x");

            pstm.setString(1,"Small");
            pstm.setInt(2,year);
            rs = pstm.executeQuery();
            if (!rs.next())
                return "Error";
            small = rs.getInt("x");

            if (large == 0 && medium == 0 && small == 0){
                result = "No Information Available";
                return result;
            }


            if(large > medium && large > small)
            {
                result = "Large (" + large + " vehicles)";
            }
            else
            if(medium > small){
                result = "Medium (" + medium + " vehicles)";
            }
            else{
                result = "Small (" + small + " vehicles)";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static double viewAnnualIncome(int year){
        String sql="SELECT SUM(bill) as totalIncome FROM mms.reservations WHERE YEAR(checkOutDate) = ?";

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1,year);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                return rs.getFloat("totalIncome");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public String mostActiveMonth(int year){
        String result = "";
        String monthName;
        String sql = "SELECT MONTH(checkOutDate) as month, COUNT(idReservations) as reservations FROM mms.reservations " +
                "WHERE YEAR(checkOutDate) = ? GROUP BY month ORDER BY reservations desc;";
        int month, flag=0, count, lastCount=0;
        Calendar c = Calendar.getInstance();

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1,year);
            ResultSet rs = pstm.executeQuery();

            while(rs.next()){
                month = rs.getInt("month");
                count = rs.getInt("reservations");

                if((flag==0 || lastCount==count) && !rs.isLast()) {
                    c.set(Calendar.MONTH, month - 1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

                    result += monthName.substring(0, 1).toUpperCase() + monthName.substring(1) + " (" + count + " Reservations) ";
                    lastCount = count;
                }else{
                    return result;
                }
                if(flag==0)
                    flag=1;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "No Information Available";
    }

    public String viewMostProfitableMonth(int year) {
        int month, flag=0;
        double profit, lastProfit=0;
        String result="", monthName;
        String sql="SELECT MONTH(checkOutDate) as month, SUM(bill) as totalIncome FROM mms.reservations WHERE YEAR(checkOutDate) = ? GROUP BY month ORDER BY totalIncome DESC";
        Calendar c = Calendar.getInstance();

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1,year);
            ResultSet rs = pstm.executeQuery();

            while(rs.next()){
                month = rs.getInt("month");
                profit = rs.getFloat("totalIncome");

                if((flag==0 || lastProfit==profit) && !rs.isLast()) {
                    c.set(Calendar.MONTH, month - 1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

                    result += monthName.substring(0, 1).toUpperCase() + monthName.substring(1) + " (" + profit + " $) ";
                    lastProfit = profit;
                }else{
                    return result;
                }
                if(flag==0)
                    flag=1;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "No Information Available";
    }

    public static boolean update(Reservation reservation){
        String sql = "UPDATE mms.reservations " +
                "SET clientName=?, checkInDate=?, checkOutDate=?, nif=?, vehicleRegistrationNumber=?, bill=? " +
                "WHERE idReservations=?";

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, reservation.getClientName());
            pstm.setDate(2, reservation.getCheckInDate());
            pstm.setDate(3, reservation.getCheckOutDate());
            pstm.setInt(4, reservation.getNif());
            pstm.setString(5, reservation.getRegNumber());
            pstm.setDouble(6, reservation.getBill());
            pstm.setInt(7, reservation.getId());

            if (!pstm.execute())
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean update(int id, State state){
        String sql = "UPDATE mms.reservations " +
                "SET state=? " +
                "WHERE idReservations=?";

        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1,state.getValue());
            pstm.setInt(2,id);

            if (!pstm.execute())
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static Reservation getReservation(int id){
        String sql = "SELECT * FROM mms.reservations WHERE idReservations=?";
        ResultSet rs;
        Reservation reservation = null;
        try {
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);

            rs = pstm.executeQuery();
            rs.next();
            reservation = new Reservation(
                    rs.getString("clientName"),
                    rs.getDate("checkInDate"),
                    rs.getDate("checkOutDate"),
                    rs.getFloat("bill"),
                    rs.getInt("nif"),
                    rs.getString("vehicleRegistrationNumber"),
                    rs.getInt("state"),
                    rs.getString("category"));
            reservation.setId(rs.getInt("idReservations"));
            reservation.setSlot(read(id));

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public static Reservation getReservation(String vrn){
        String sql = "SELECT * FROM mms.reservations WHERE vehicleRegistrationNumber=? AND state=1";
        ResultSet rs;
        Reservation reservation = null;
        try {
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, vrn);

            rs = pstm.executeQuery();
            if(rs.next()) {
                reservation = new Reservation(
                        rs.getString("clientName"),
                        rs.getDate("checkInDate"),
                        rs.getDate("checkOutDate"),
                        rs.getFloat("bill"),
                        rs.getInt("nif"),
                        rs.getString("vehicleRegistrationNumber"),
                        rs.getInt("state"),
                        rs.getString("category"));
                reservation.setId(rs.getInt("idReservations"));
                reservation.setSlot(read(reservation.getId()));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public ArrayList viewReservationByState(State state) {
        ArrayList<Reservation> lista = read(state);
        int idReservation;

        for (int i = 0; i<lista.size(); i++){

            idReservation = lista.get(i).getId();
            lista.get(i).setSlot(read(idReservation));
        }

        return lista;
    }
}
