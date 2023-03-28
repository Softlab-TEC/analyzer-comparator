package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class FatRepositoryDAO extends CodeSmellDAO {
    private int entities;

    public FatRepositoryDAO(){
        super();
    }

    public FatRepositoryDAO(
            int id,
            String className,
            int entities,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.entities = entities;
    }

    public FatRepositoryDAO(
            String className,
            int entities,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    ) {
        super(className, analyzerId, projectName, path, insertDate);
        this.entities = entities;
    }

    public FatRepositoryDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getEntities() {
        return entities;
    }

    public void setEntities(int entities) {
        this.entities = entities;
    }

    public ArrayList<FatRepositoryDAO> geFatRepositories(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        FatRepositoryDAO fatRepository;
        ArrayList<FatRepositoryDAO> fatRepositories = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                fatRepository = new FatRepositoryDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getInt("entities"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                fatRepositories.add(fatRepository);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return fatRepositories;
    }

    public ArrayList<FatRepositoryDAO> getFatRepositoriesOnlySpringLint(){
        String query = "SELECT fr1.* FROM FATREPOSITORY fr1 LEFT JOIN FATREPOSITORY fr2 ON fr1.classname = fr2.classname AND fr2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE fr1.analyzerid = " + AnalyzerConstants.SPRINGLINT + " AND fr1.analyzerid IS NULL;";
        return geFatRepositories(query);
    }

    public ArrayList<FatRepositoryDAO> getFatRepositoriesOnlyAnalyzer(){
        String query = "SELECT fr1.* FROM FATREPOSITORY fr1 LEFT JOIN FATREPOSITORY fr2 ON fr1.classname = fr2.classname AND fr2.analyzerid = "+ AnalyzerConstants.SPRINGLINT + " WHERE fr1.analyzerid = " + AnalyzerConstants.ANALYZER+ " AND fr1.analyzerid IS NULL";
        return geFatRepositories(query);
    }

    public void save(){
        String sql = "INSERT INTO FATREPOSITORY(classname, entities, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, entities);
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
