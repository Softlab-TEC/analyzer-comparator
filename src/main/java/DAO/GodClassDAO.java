package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class GodClassDAO {
    private int id;
    private String className;
    private int wmc;
    private int atfd;
    private float tcc;
    private int analyzerId;
    private String projectName;
    private String path;
    private Date insertDate;

    private Connect connection;

    public GodClassDAO(int id,
             String className,
             int wmc,
             int atfd,
             float tcc,
             int analyzerId,
             String projectName,
             String path,
             Date insertDate
    )
    {
        this.id = id;
        this.className = className;
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.insertDate = insertDate;
    }

    public GodClassDAO(String className,
                       int wmc,
                       int atfd,
                       float tcc,
                       int analyzerId,
                       String projectName,
                       String path,
                       Date insertDate
    )
    {
        this.className = className;
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.insertDate = insertDate;
    }

    public ArrayList<GodClassDAO> getGodClasses(GodClassDAO godClassDao){
        String sql = "SELECT * FROM GODCLASS";
        GodClassDAO godClass;
        ArrayList<GodClassDAO> godClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection();
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            // set the value
            //pstmt.setDouble(1,capacity);
            while (rs.next()){
                godClass = new GodClassDAO(
                    rs.getInt("id"),
                    rs.getString("classname"),
                    rs.getInt("wmc"),
                    rs.getInt("atfd"),
                    rs.getFloat("acc"),
                    rs.getInt("analizerid"),
                    rs.getString("projectname"),
                    rs.getString("path"),
                    rs.getDate("insertdate")
                );
                godClasses.add(godClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return godClasses;
    }
    public void save(){
        String sql = "INSERT INTO GODCLASS(classname, wmc, atfd, tcc, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){
            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, wmc);
            pstmt1.setInt(3, atfd);
            pstmt1.setFloat(4, tcc);
            pstmt1.setInt(5, analyzerId);
            pstmt1.setString(6, projectName);
            pstmt1.setString(7, path);

            int rowAffected = pstmt1.executeUpdate();

            // get the material id
           ResultSet rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                System.out.println(rs.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
