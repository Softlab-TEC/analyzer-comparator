package DAO;

import DAO.Helpers.GetGodClassHelper;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public int getAtfd() {
        return atfd;
    }

    public void setAtfd(int atfd) {
        this.atfd = atfd;
    }

    public float getTcc() {
        return tcc;
    }

    public void setTcc(float tcc) {
        this.tcc = tcc;
    }

    public int getAnalyzerId() {
        return analyzerId;
    }

    public void setAnalyzerId(int analyzerId) {
        this.analyzerId = analyzerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Connect getConnection() {
        return connection;
    }

    public void setConnection(Connect connection) {
        this.connection = connection;
    }

    private Connect connection;

    public GodClassDAO(String className, int analyzerId, Date insertDate) {
        this.className = className;
        this.analyzerId = analyzerId;
        this.insertDate = insertDate;
    }

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

    public ArrayList<GodClassDAO> getGodClasses(){
        String sql = "SELECT * FROM GODCLASS";
        ArrayList<String> whereStatement = GetGodClassHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            sql += where;
        }
        
        GodClassDAO godClass;
        ArrayList<GodClassDAO> godClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            GetGodClassHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                godClass = new GodClassDAO(
                    rs.getInt("id"),
                    rs.getString("classname"),
                    rs.getInt("wmc"),
                    rs.getInt("atfd"),
                    rs.getFloat("tcc"),
                    rs.getInt("analyzerid"),
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
