package com.company;

/**
 * Created by keithmartin on 3/16/16.
 */
import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.sql.*;
import java.util.Vector;
import static javax.management.remote.JMXConnectorFactory.connect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author keithmartin
 */
public class Table {

    public void fillMovieTable(JTable table, String query, Connection conn) {
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
                Object selectedRow = table.getModel().getValueAt(rowIndex, 0);
                rsList.add(conn.createStatement().executeQuery("SELECT star_rating FROM rating WHERE movie_id=" + selectedRow + " AND user_id=" + getUserId(conn)));
                rowIndex++;
            }
            rowIndex = 0;
            Object[] ratingRow = new Object[1];
            while (rowIndex <= (table.getModel().getRowCount() - 1)) {
                if (rsList.get(rowIndex).next()) {
                    ratingRow[0] = rsList.get(rowIndex).getObject(1);
                    table.setValueAt(ratingRow[0], rowIndex, 4);
                }
                rowIndex++;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void fillWatchedAndRatingTable(JTable table, String query, Connection conn) {
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
                rsList.add(conn.createStatement().executeQuery("SELECT star_rating FROM rating WHERE movie_id=" + selectedRow + " AND user_id=" + getUserId(conn)));
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

    public void fillTable(JTable table, String Query, Connection conn) {
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(Query);

            while (table.getRowCount() > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow() - 1, row);
            }
        } catch (SQLException e) {
            System.out.println("SQLException!");
        }
    }

    public int getUserId(Connection conn) throws SQLException{
        ResultSet rs = conn.createStatement().executeQuery("SELECT user_id FROM user WHERE first_name=" + "'" + LoginWindow.userName + "'");
        if (rs.next()) {
            return (Integer) rs.getObject(1);
        }
        return 0;
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

    public String getMovieId(int movieId, Connection conn) throws SQLException{
        ResultSet rs = conn.createStatement().executeQuery("SELECT name FROM movie WHERE movie_id=" + movieId);
        if (rs.next()) {
            return (String) rs.getObject(1);
        }
        return null;
    }

}
