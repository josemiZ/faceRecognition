package com.bixspace.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by MIGUEL ZEA on 12/11/2016.
 */
public class ConnectionClass {
    private static String url = "jdbc:mysql://localhost:3306/javabase";
    private static String username = "java";
    private static String password = "password";
    private Connection connection = null;
    public Connection conect() throws SQLException {
        if(connection==null) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
    public void disconnect() throws SQLException {
        if(connection!=null)
            connection.close();
    }
}
