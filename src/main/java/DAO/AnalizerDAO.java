package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnalizerDAO {
    private int id;
    private String name;
    private Connect connection;

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public AnalizerDAO(int id, String name){
        this.id = id;
        this.name = name;
    }

    public ArrayList<AnalizerDAO> getAnalizers(DAO.GodClassDAO godClassDao){
        String sql = "SELECT * FROM ANALYZER";
        AnalizerDAO analizer;
        ArrayList<AnalizerDAO> analizers = new ArrayList<>();
        try (Connection conn = connection.getConnection();
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            // set the value
            //pstmt.setDouble(1,capacity);
            while (rs.next()){
                analizer = new DAO.AnalizerDAO(rs.getInt("id"), rs.getString("name"));
                analizers.add(analizer);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return analizers;
    }
}
