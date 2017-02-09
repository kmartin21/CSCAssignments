package com.company;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LicenseServlet extends HttpServlet {

    String PUBLIC_DNS = "ec2-54-149-52-62.us-west-2.compute.amazonaws.com";
    Connection connection = null;


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + PUBLIC_DNS + ":3306/keithmartinA1P2db", "kmartin5", "Colden21");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement stat = null;
        try {
            stat = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet = stat.executeQuery("SELECT * FROM movie;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        out.print("<html><body><table>");

        // ... pseudocode
        try {
            while (resultSet.next()) {
                out.print("<tr><td>");
                out.print(resultSet.getString("name"));
                out.print("</td></tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print("</table></body></html>");
    }
}
