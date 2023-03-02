package DAO;

import DAO.Helpers.CodeSmellDAO;
import DAO.Helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class FeatureEnvyDAO extends CodeSmellDAO {
    private int atfd;
    private float laa;
    private int fdp;

    public FeatureEnvyDAO(){
        super();
    }

    public FeatureEnvyDAO(int id,
                          String className,
                          int atfd,
                          float laa,
                          int fdp,
                          int analyzerId,
                          String projectName,
                          String path,
                          Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.atfd = atfd;
        this.laa = laa;
        this.fdp = fdp;
    }

    public FeatureEnvyDAO(String className,
                       int atfd,
                       float laa,
                       int fdp,
                       int analyzerId,
                       String projectName,
                       String path,
                       Date insertDate) {
        super(className, analyzerId, projectName, path, insertDate);
        this.atfd = atfd;
        this.laa = laa;
        this.fdp = fdp;
    }

    public FeatureEnvyDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getAtfd() {
        return atfd;
    }

    public void setAtfd(int atfd) {
        this.atfd = atfd;
    }

    public float getLaa() {
        return laa;
    }

    public void setLaa(float laa) {
        this.laa = laa;
    }

    public int getFdp() {
        return fdp;
    }

    public void setFdp(int fdp) {
        this.fdp = fdp;
    }

    public ArrayList<FeatureEnvyDAO> getFeatureEnvies(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        FeatureEnvyDAO featureEnvy;
        ArrayList<FeatureEnvyDAO> featureEnvies = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                featureEnvy = new FeatureEnvyDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getInt("atfd"),
                        rs.getFloat("laa"),
                        rs.getInt("fdp"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                featureEnvies.add(featureEnvy);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return featureEnvies;
    }

    public ArrayList<FeatureEnvyDAO> getGodClassesOnlyPMD(){
        String query = "SELECT gc1.* FROM FEATUREENVY fe1 LEFT JOIN FEATUREENVY fe2 ON fe1.classname = fe2.classname AND fe2.analyzerid = 1 WHERE fe1.analyzerid = 2 AND fe2.analyzerid IS NULL;";
        return getFeatureEnvies(query);
    }

    public ArrayList<FeatureEnvyDAO> getGodClassesOnlyAnalyzer(){
        String query = "SELECT fe1.* FROM FEATUREENVY fe1 LEFT JOIN FEATUREENVY fe2 ON fe1.classname = fe2.classname AND fe2.analyzerid = 2 WHERE fe1.analyzerid = 1 AND fe2.analyzerid IS NULL";
        return getFeatureEnvies(query);
    }

    public void save(){
        String sql = "INSERT INTO FEATUREENVY(classname, atfd, laa, fdp, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, atfd);
            pstmt1.setFloat(3, laa);
            pstmt1.setFloat(4, fdp);
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
