package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MeddlingServiceDAO extends CodeSmellDAO {
    private int persistenceDependencies;

    public MeddlingServiceDAO(){
        super();
    }

    public MeddlingServiceDAO(
            int id,
            String className,
            String mainClassName,
            int persistenceDependencies,
            int analyzerId,
            String projectName,
            String path,
            Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, insertDate);
        this.persistenceDependencies = persistenceDependencies;
    }

    public MeddlingServiceDAO(
            String className,
            String mainClassName,
            int persistenceDependencies,
            int analyzerId,
            String projectName,
            String path,
            Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, insertDate);
        this.persistenceDependencies = persistenceDependencies;
    }

    public MeddlingServiceDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getPersistenceDependencies() {
        return persistenceDependencies;
    }

    public void setPersistenceDependencies(int persistenceDependencies) {
        this.persistenceDependencies = persistenceDependencies;
    }

    public ArrayList<MeddlingServiceDAO> getMeddlingServices(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        MeddlingServiceDAO meddlingService;
        ArrayList<MeddlingServiceDAO> meddlingServices = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                meddlingService = new MeddlingServiceDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getInt("persistenceDependencies"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                meddlingServices.add(meddlingService);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return meddlingServices;
    }

    public ArrayList<MeddlingServiceDAO> getMeddlingServiceOnlySpringLint(){
        String query = "SELECT mc1.* FROM MEDDLINGSERVICE mc1 LEFT JOIN MEDDLINGSERVICE mc2 ON mc1.classname = mc2.classname AND mc2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE mc1.analyzerid = " + AnalyzerConstants.SPRINGLINT + " AND mc1.analyzerid IS NULL;";
        return getMeddlingServices(query);
    }

    public ArrayList<MeddlingServiceDAO> getMeddlingServicesOnlyAnalyzer(){
        String query = "SELECT mc1.* FROM MEDDLINGSERVICE mc1 LEFT JOIN MEDDLINGSERVICE mc2 ON mc1.classname = mc2.classname AND mc2.analyzerid = "+ AnalyzerConstants.SPRINGLINT + " WHERE mc1.analyzerid = " + AnalyzerConstants.ANALYZER+ " AND mc2.analyzerid IS NULL";
        return getMeddlingServices(query);
    }

    public void save(){
        String sql = "INSERT INTO MEDDLINGSERVICE(classname, mainclassname,  persistenceDependencies, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, persistenceDependencies);
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
