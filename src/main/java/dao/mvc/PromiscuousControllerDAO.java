package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class PromiscuousControllerDAO extends CodeSmellDAO {
    private int nor;
    private int nsd;
    private int id;

    public PromiscuousControllerDAO(){
        super();
    }

    public PromiscuousControllerDAO(
            int id,
            String className,
            String mainClassName,
            int nor,
            int nsd,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.nor = nor;
        this.nsd = nsd;
        this.id = id;
    }

    public PromiscuousControllerDAO(
            String className,
            String mainClassName,
            int nor,
            int nsd,
            int analyzerId,
            String projectName,
            String path,
            String language,
            java.util.Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.nor = nor;
        this.nsd = nsd;
    }

    public PromiscuousControllerDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getNor() {
        return nor;
    }

    public void setNor(int nor) {
        this.nor = nor;
    }

    public int getNsd() {
        return nsd;
    }

    public void setNsd(int nsd) {
        this.nsd = nsd;
    }

    public ArrayList<PromiscuousControllerDAO> getPromiscuousControllers(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        PromiscuousControllerDAO promiscuousController;
        ArrayList<PromiscuousControllerDAO> promiscuousControllers = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                promiscuousController = new PromiscuousControllerDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getInt("nor"),
                        rs.getInt("nsd"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                promiscuousControllers.add(promiscuousController);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return promiscuousControllers;
    }

    public ArrayList<PromiscuousControllerDAO> getPromiscuousControllersOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                                                     boolean avoidTestFiles){
        String query = getOnlyOneAnalyzerResult("PROMISCUOUSCONTROLLER", analyzerId, language, projectName, avoidTestFiles);
        return getPromiscuousControllers(query);
    }


    public void save(){
        String sql = "INSERT INTO PROMISCUOUSCONTROLLER(classname, mainclassname, nor, nsd, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, nor);
            pstmt1.setInt(4, nsd);
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
