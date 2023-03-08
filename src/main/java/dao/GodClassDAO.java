package dao;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class GodClassDAO extends CodeSmellDAO {
    private int wmc;
    private int atfd;
    private float tcc;

    public GodClassDAO(){
        super();
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
        super(id, className, analyzerId, projectName, path, insertDate);
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
    }

    public GodClassDAO(String className,
                       int wmc,
                       int atfd,
                       float tcc,
                       int analyzerId,
                       String projectName,
                       String path,
                       Date insertDate
    ){
        super(className, analyzerId, projectName, path, insertDate);
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
    }

    public GodClassDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
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

    public ArrayList<GodClassDAO> getGodClasses(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }
        
        GodClassDAO godClass;
        ArrayList<GodClassDAO> godClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

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

    public ArrayList<GodClassDAO> getGodClassesOnlyPMD(){
        String query = "SELECT gc1.* FROM GODCLASS gc1 LEFT JOIN GODCLASS gc2 ON gc1.classname = gc2.classname AND gc2.analyzerid = 1 WHERE gc1.analyzerid = 2 AND gc2.analyzerid IS NULL;";
        return getGodClasses(query);
    }

    public ArrayList<GodClassDAO> getGodClassesOnlyAnalyzer(){
        String query = "SELECT gc1.* FROM GODCLASS gc1 LEFT JOIN GODCLASS gc2 ON gc1.classname = gc2.classname AND gc2.analyzerid = 2 WHERE gc1.analyzerid = 1 AND gc2.analyzerid IS NULL";
        return getGodClasses(query);
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
