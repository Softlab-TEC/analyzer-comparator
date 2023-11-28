package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BrainControllerDAO extends CodeSmellDAO {
    private int nfrc;

    public BrainControllerDAO(){
        super();
    }

    public BrainControllerDAO(
            int id,
            String className,
            String mainClassName,
            int nfrc,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.nfrc = nfrc;
    }

    public BrainControllerDAO(
            String className,
            String mainClassName,
            int nfrc,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.nfrc = nfrc;
    }

    public BrainControllerDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getNfrc() {
        return nfrc;
    }

    public void setNfrc(int nfrc) {
        this.nfrc = nfrc;
    }

    public ArrayList<BrainControllerDAO> getBrainControllers(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        BrainControllerDAO brainController;
        ArrayList<BrainControllerDAO> brainControllers = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                brainController = new BrainControllerDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getInt("nfrc"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                brainControllers.add(brainController);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainControllers;
    }


    public ArrayList<BrainControllerDAO> getBrainControllersOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                                         boolean avoidTestFiles){
        String query = getOnlyOneAnalyzerResult("BRAINCONTROLLER", analyzerId, language, projectName, avoidTestFiles);
        return getBrainControllers(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINCONTROLLER(classname, mainclassname, nfrc, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, nfrc);
            pstmt1.setInt(4, analyzerId);
            pstmt1.setString(5, projectName);
            pstmt1.setString(6, path);
            pstmt1.setString(7, language);

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
