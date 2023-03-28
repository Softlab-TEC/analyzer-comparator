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
            int cyclo,
            int sqlComplexity,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.cyclo = cyclo;
        this.sqlComplexity = sqlComplexity;
    }

    public BrainRepositoryDAO(
            String className,
            int cyclo,
            int sqlComplexity,
            int analyzerId,
            String projectName,
            String path,
            java.util.Date insertDate
    ) {
        super(className, analyzerId, projectName, path, insertDate);
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
                        rs.getInt("cyclo"),
                        rs.getInt("sqlComplexity"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                brainRepositories.add(brainRepository);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainRepositories;
    }

    public ArrayList<BrainRepositoryDAO> getBrainRepositoriesOnlySpringLint(){
        String query = "SELECT br1.* FROM BRAINREPOSITORY br1 LEFT JOIN BRAINREPOSITORY br2 ON lrm1.classname = br2.classname AND br2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE br1.analyzerid = " + AnalyzerConstants.SPRINGLINT + " AND br1.analyzerid IS NULL;";
        return getBrainRepositories(query);
    }

    public ArrayList<BrainRepositoryDAO> getBrainRepositoriesOnlyAnalyzer(){
        String query = "SELECT br1.* FROM BRAINREPOSITORY br1 LEFT JOIN BRAINREPOSITORY br2 ON lrm1.classname = br2.classname AND br2.analyzerid = "+ AnalyzerConstants.SPRINGLINT + " WHERE br1.analyzerid = " + AnalyzerConstants.ANALYZER+ " AND br1.analyzerid IS NULL";
        return getBrainRepositories(query);
    }

    public void save(){
        String sql = "INSERT INTO LABORIOUSREPOSITORYMETHOD(classname, cyclo, sqlComplexity, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, cyclo);
            pstmt1.setInt(3, sqlComplexity);
            pstmt1.setInt(4, analyzerId);
            pstmt1.setString(5, projectName);
            pstmt1.setString(6, path);

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
