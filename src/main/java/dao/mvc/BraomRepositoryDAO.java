package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BraomRepositoryDAO extends CodeSmellDAO {
    private int nfrc;

    public BraomRepositoryDAO(){
        super();
    }

    public BraomRepositoryDAO(
            int id,
            String className,
            int nfrc,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.nfrc = nfrc;
    }

    public BraomRepositoryDAO(
            String className,
            int nfrc,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    ) {
        super(className, analyzerId, projectName, path, insertDate);
        this.nfrc = nfrc;
    }

    public BraomRepositoryDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getNfrc() {
        return nfrc;
    }

    public void setNfrc(int nfrc) {
        this.nfrc = nfrc;
    }

    public ArrayList<BraomRepositoryDAO> getBrainControllers(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        BraomRepositoryDAO brainController;
        ArrayList<BraomRepositoryDAO> brainControllers = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                brainController = new BraomRepositoryDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getInt("nfrc"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                brainControllers.add(brainController);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainControllers;
    }

    public ArrayList<BraomRepositoryDAO> getBrainControllersOnlySpringLint(){
        String query = "SELECT bc1.* FROM BRAINCONTROLLER bc1 LEFT JOIN BRAINCONTROLLER bc2 ON lrm1.classname = bc2.classname AND bc2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE bc1.analyzerid = " + AnalyzerConstants.SPRINGLINT + " AND bc1.analyzerid IS NULL;";
        return getBrainControllers(query);
    }

    public ArrayList<BraomRepositoryDAO> getBrainControllersOnlyAnalyzer(){
        String query = "SELECT bc1.* FROM BRAINCONTROLLER bc1 LEFT JOIN BRAINCONTROLLER bc2 ON lrm1.classname = bc2.classname AND bc2.analyzerid = "+ AnalyzerConstants.SPRINGLINT + " WHERE bc1.analyzerid = " + AnalyzerConstants.ANALYZER+ " AND bc1.analyzerid IS NULL";
        return getBrainControllers(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINCONTROLLER(classname, nfrc, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, nfrc);
            pstmt1.setInt(3, analyzerId);
            pstmt1.setString(4, projectName);
            pstmt1.setString(5, path);

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
