package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class LaboriousRepositoryMethodDAO extends CodeSmellDAO {

    private int quantityPersistenceActions;
    private String methodName;

    public LaboriousRepositoryMethodDAO(){
        super();
    }

    public LaboriousRepositoryMethodDAO(
            int id,
            String className,
            String mainClassName,
            String methodName,
            int quantityPersistenceActions,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.methodName = methodName;
        this.quantityPersistenceActions = quantityPersistenceActions;
    }

    public LaboriousRepositoryMethodDAO(
            String className,
            String mainClassName,
            String methodName,
            int quantityPersistenceActions,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.methodName = methodName;
        this.quantityPersistenceActions = quantityPersistenceActions;
    }

    public LaboriousRepositoryMethodDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getQuantityPersistenceActions() {
        return quantityPersistenceActions;
    }

    public void setQuantityPersistenceActions(int quantityPersistenceActions) {
        this.quantityPersistenceActions = quantityPersistenceActions;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ArrayList<LaboriousRepositoryMethodDAO> getLaboriousRepositoryMethods(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        LaboriousRepositoryMethodDAO laboriousRepoMethod;
        ArrayList<LaboriousRepositoryMethodDAO> laboriousRepoMethods = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                laboriousRepoMethod = new LaboriousRepositoryMethodDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getString("methodname"),
                        rs.getInt("quantityPersistenceActions"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                laboriousRepoMethods.add(laboriousRepoMethod);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return laboriousRepoMethods;
    }

    public ArrayList<LaboriousRepositoryMethodDAO> getLaboriousRepoMethodOnlySpringLint(int analyzerId, String language, String projectName){
        String query = "SELECT lrm1.* FROM LABORIOUSREPOSITORYMETHOD lrm1 LEFT JOIN LABORIOUSREPOSITORYMETHOD lrm2 ON lrm1.classname = lrm2.classname AND lrm2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE lrm1.analyzerid = " + AnalyzerConstants.SPRINGLINT + " AND lrm1.analyzerid IS NULL;";
        return getLaboriousRepositoryMethods(query);
    }

    public ArrayList<LaboriousRepositoryMethodDAO> getLaboriousRepoMethodOnlyAnalyzer(int analyzerId, String language, String projectName){
        String query = "SELECT lrm1.* FROM LABORIOUSREPOSITORYMETHOD lrm1 LEFT JOIN LABORIOUSREPOSITORYMETHOD lrm2 ON lrm1.classname = lrm2.classname AND lrm2.analyzerid = "+ AnalyzerConstants.SPRINGLINT + " WHERE lrm1.analyzerid = " + AnalyzerConstants.ANALYZER+ " AND lrm2.analyzerid IS NULL";
        return getLaboriousRepositoryMethods(query);
    }

    public void save(){
        String sql = "INSERT INTO LABORIOUSREPOSITORYMETHOD(classname, mainclassname, methodname, quantityPersistenceActions, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setString(3, methodName);
            pstmt1.setInt(4, quantityPersistenceActions);
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
