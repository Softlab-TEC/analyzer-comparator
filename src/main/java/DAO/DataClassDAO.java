package DAO;

import DAO.Helpers.CodeSmellDAO;
import DAO.Helpers.GetCodeSmellHelper;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DataClassDAO extends CodeSmellDAO {
    private float woc;
    private int nopa;
    private int noam;
    private int wmc;

    public DataClassDAO(){
        super();
    }

    public DataClassDAO(int id,
                        String className,
                        float woc,
                        int nopa,
                        int noam,
                        int wmc,
                        int analyzerId,
                        String projectName,
                        String path,
                        Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.woc = woc;
        this.nopa = nopa;
        this.noam = noam;
        this.wmc = wmc;
    }

    public DataClassDAO(String className,
                        float woc,
                        int nopa,
                        int noam,
                        int wmc,
                        int analyzerId,
                        String projectName,
                        String path,
                        Date insertDate) {
        super(className, analyzerId, projectName, path, insertDate);
        this.woc = woc;
        this.nopa = nopa;
        this.noam = noam;
        this.wmc = wmc;
    }

    public DataClassDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getNoam() {
        return noam;
    }

    public void setNoam(int noam) {
        this.noam = noam;
    }

    public float getWoc() {
        return woc;
    }

    public void setWoc(float woc) {
        this.woc = woc;
    }

    public int getNopa() {
        return nopa;
    }

    public void setNopa(int nopa) {
        this.nopa = nopa;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public ArrayList<DataClassDAO> getDataClasses(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        DataClassDAO dataClass;
        ArrayList<DataClassDAO> dataClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                dataClass = new DataClassDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getFloat("woc"),
                        rs.getInt("nopa"),
                        rs.getInt("noam"),
                        rs.getInt("wmc"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                dataClasses.add(dataClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return dataClasses;
    }

    public ArrayList<DataClassDAO> getGodClassesOnlyPMD(){
        String query = "SELECT dc1.* FROM DATACLASS dc1 LEFT JOIN DATACLASS dc2 ON dc1.classname = dc2.classname AND dc2.analyzerid = 1 WHERE dc1.analyzerid = 2 AND dc2.analyzerid IS NULL;";
        return getDataClasses(query);
    }

    public ArrayList<DataClassDAO> getGodClassesOnlyAnalyzer(){
        String query = "SELECT dc1.* FROM DATACLASS dc1 LEFT JOIN DATACLASS dc2 ON dc1.classname = dc2.classname AND dc2.analyzerid = 2 WHERE dc1.analyzerid = 1 AND dc2.analyzerid IS NULL";
        return getDataClasses(query);
    }

    public void save(){
        String sql = "INSERT INTO DATACLASS(classname, woc, nopa, noam, wmc, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setFloat(2, woc);
            pstmt1.setFloat(3, nopa);
            pstmt1.setFloat(4, noam);
            pstmt1.setInt(5, wmc);
            pstmt1.setInt(6, analyzerId);
            pstmt1.setString(7, projectName);
            pstmt1.setString(8, path);

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
