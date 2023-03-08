package dao;

import java.sql.*;

public class Connect {
    public static void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:/Users/williamcasasola/Documents/TEC/Detector/Analyzer-Comparator/Analizer-Comparator/DB/AnalyzerResult.db");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:/Users/williamcasasola/Documents/TEC/Detector/Analyzer-Comparator/Analizer-Comparator/DB/AnalyzerResult.db");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void autocommit(){
        //https://www.sqlitetutorial.net/sqlite-java/transaction/
        //conn.setAutoCommit(false);
        //conn.commit();
        //conn.rollback();
    }

    public void insert(String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAnalyzers() {
    }
}