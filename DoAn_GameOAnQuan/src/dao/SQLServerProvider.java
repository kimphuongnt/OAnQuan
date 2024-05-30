/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SQLServerProvider {

    private Connection connection;

    public void open() {
        String strServer = "ACER\\FUANG";
        String strDatabase = "QL_GAME";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionURL = "jdbc:sqlserver://" + strServer
                    + ":1433;databaseName=" + strDatabase
                    + ";user = sa; password = 123"
                    + ";encrypt = false";
            try {
                connection = DriverManager.getConnection(connectionURL);
            } catch (SQLException ex) {
                Logger.getLogger(SQLServerProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLServerProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLServerProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet excuteQuery(String sql) {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(sql);
        } catch (Exception e) {
        }
        return rs;
    }

    public int excuteUpdate(String sql) {
        int n = -1;
        try {
            Statement statement = connection.createStatement();
            n = statement.executeUpdate(sql);
        } catch (Exception e) {
        }
        return n;
    }

    public Connection getConnection() {
        return this.connection;
    }
}   
