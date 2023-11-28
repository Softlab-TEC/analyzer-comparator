package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BrainRepositoryDAO extends CodeSmellDAO {
    private int cyclo;
    private int sqlComplexity;

    public BrainRepositoryDAO(){
        super();
    }

    public BrainRepositoryDAO(
            int id,
            String className,
            String mainClassName,
            int cyclo,
            int sqlComplexity,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.cyclo = cyclo;
        this.sqlComplexity = sqlComplexity;
    }

    public BrainRepositoryDAO(
            String className,
            String mainClassName,
            int cyclo,
            int sqlComplexity,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.cyclo = cyclo;
        this.sqlComplexity = sqlComplexity;
    }

    public BrainRepositoryDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getCyclo() {
        return cyclo;
    }

    public void setCyclo(int cyclo) {
        this.cyclo = cyclo;
    }

    public int getSqlComplexity() {
        return sqlComplexity;
    }

    public void setSqlComplexity(int sqlComplexity) {
        this.sqlComplexity = sqlComplexity;
    }

    public ArrayList<BrainRepositoryDAO> getBrainRepositories(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        BrainRepositoryDAO brainRepository;
        ArrayList<BrainRepositoryDAO> brainRepositories = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                brainRepository = new BrainRepositoryDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getInt("cyclo"),
                        rs.getInt("sqlComplexity"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                brainRepositories.add(brainRepository);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainRepositories;
    }

    public ArrayList<BrainRepositoryDAO> getBrainRepositoriesOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                                          boolean avoidTestFiles){
        String query = getOnlyOneAnalyzerResult("BRAINREPOSITORY", analyzerId, language, projectName, avoidTestFiles);
        return getBrainRepositories(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINREPOSITORY(classname, mainclassname, cyclo, sqlComplexity, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, cyclo);
            pstmt1.setInt(4, sqlComplexity);
            pstmt1.setInt(5, analyzerId);
            pstmt1.setString(6, projectName);
            pstmt1.setString(7, path);
            pstmt1.setString(8, language);

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
