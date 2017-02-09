package com.company;

import com.sun.xml.internal.ws.addressing.WsaTubeHelperImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by keithmartin on 4/5/16.
 */
public class AdminTable {

    public void fillWatchedAndRatingTable(String userName, JTable table, String query, Connection conn) {
        try {

            ResultSet rs = conn.createStatement().executeQuery(query);
            int rowIndex = 0;

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            Object[] row = new Object[columns + 1];
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }

            ArrayList<ResultSet> rsList = new ArrayList<>();
            while (rowIndex < table.getModel().getRowCount()) {
                Object selectedRow = table.getModel().getValueAt(rowIndex, 1);
                rsList.add(conn.createStatement().executeQuery("SELECT star_rating FROM rating WHERE movie_id=" + selectedRow + " AND user_id=" + getUserId(userName, conn)));
                rowIndex++;
            }
            rowIndex = 0;
            Object[] ratingRow = new Object[1];
            while (rowIndex <= (table.getModel().getRowCount() - 1)) {
                if (rsList.get(rowIndex).next()) {
                    ratingRow[0] = rsList.get(rowIndex).getObject(1);
                    table.setValueAt(ratingRow[0], rowIndex, 3);
                }
                rowIndex++;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public int getUserId(String userName, Connection conn) throws SQLException{
        ResultSet rs = conn.createStatement().executeQuery("SELECT user_id FROM user WHERE first_name=" + "'" + userName + "'");
        if (rs.next()) {
            return (Integer) rs.getObject(1);
        }
        return 0;
    }

    public ArrayList<String> getUserNames(Connection connection) throws SQLException{
        ArrayList<String> userNames = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT first_name FROM user;");
        while (rs.next()) {
            userNames.add((String) rs.getObject(1));
        }
        return userNames;
    }


    public void fillMovieName(JTable table, String query, Connection conn) {
        try {

            ResultSet rs = conn.createStatement().executeQuery(query);

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            Object[] row = new Object[columns + 1];
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }


    public int getNumPartialOrFull(String query, Connection conn) {
        int count = 0;
        try {

            ResultSet rs = conn.createStatement().executeQuery(query);

            int columns = rs.getMetaData().getColumnCount();
            Object[] row = new Object[columns + 1];
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return count;
    }


    public int getCategory(String query, Connection conn) {
        try {

            ResultSet rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                return (Integer) rs.getObject(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public String getCategoryName(String query, Connection conn) {
        try {

            ResultSet rs = conn.createStatement().executeQuery(query);

            if (rs.next()) {
                return (String) rs.getObject(1);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }


    public ArrayList<String> queryForRecommendations(String Query, Connection conn) {
        ArrayList<String> recommendations = new ArrayList<>();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(Query);
            while (rs.next()) {
                recommendations.add("We think you'll really like " + rs.getObject(1) + " because you gave " + rs.getObject(2) + " 5" + " stars.");
            }

        } catch (SQLException e) {
            System.out.println("SQLException!" + e.getMessage());
        }
        return recommendations;
    }

}
