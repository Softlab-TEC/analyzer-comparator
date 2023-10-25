package dao.identity;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class FeatureEnvyDAO extends CodeSmellDAO {
    private int atfd;
    private float laa;
    private int fdp;
    private String methodName;

    public FeatureEnvyDAO(){
        super();
    }

    public FeatureEnvyDAO(int id,
                          String className,
                          String mainClassName,
                          String methodName,
                          int atfd,
                          float laa,
                          int fdp,
                          int analyzerId,
                          String projectName,
                          String path,
                          String language,
                          Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.atfd = atfd;
        this.laa = laa;
        this.fdp = fdp;
        this.methodName = methodName;
    }

    public FeatureEnvyDAO(String className,
                          String mainClassName,
                           String methodNAme,
                           int atfd,
                           float laa,
                           int fdp,
                           int analyzerId,
                           String projectName,
                           String path,
                          String language,
                           Date insertDate
    ) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.atfd = atfd;
        this.laa = laa;
        this.fdp = fdp;
        this.methodName = methodNAme;
    }

    public FeatureEnvyDAO(String className,
                          String mainClassName,
                          String methodName,
                          int analyzerId,
                          String projectName,
                          String path,
                          String language,
                          Date insertDate
    ){
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.methodName = methodName;
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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
                        rs.getString("mainclassname"),
                        rs.getString("methodname"),
                        rs.getInt("atfd"),
                        rs.getFloat("laa"),
                        rs.getInt("fdp"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                featureEnvies.add(featureEnvy);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return featureEnvies;
    }

    public ArrayList<FeatureEnvyDAO> getFeatureEnviesOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                                  boolean avoidTestFiles){
        String avoidTestsLeft = avoidTestFiles ? " AND t1.classname NOT LIKE '%test%' " : "";
        String avoidTestsRight = avoidTestFiles ? " AND t2.classname NOT LIKE '%test%' " : "";
        String query = "SELECT t1.* FROM FEATUREENVY t1 LEFT JOIN FEATUREENVY t2 ON (t1.classname = t2.classname OR " +
                "t1.mainclassname = t2.mainclassname) AND t2.analyzerid <> " + analyzerId + " AND t2.path " +
                "LIKE '%" + projectName + "%' AND t2.language = '"+ language + "'" + avoidTestsRight +
                " WHERE t1.analyzerid = " + analyzerId + " AND t2.analyzerid IS NULL AND t1.path LIKE '%"
                + projectName + "%' AND t1.language = '" + language + "'" + avoidTestsLeft;
        return getFeatureEnvies(query);
    }

    public void save(){
        String sql = "INSERT INTO FEATUREENVY(classname, mainclassname, methodname , atfd, laa, fdp, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setString(3, methodName);
            pstmt1.setInt(4, atfd);
            pstmt1.setFloat(5, laa);
            pstmt1.setFloat(6, fdp);
            pstmt1.setInt(7, analyzerId);
            pstmt1.setString(8, projectName);
            pstmt1.setString(9, path);
            pstmt1.setString(10, language);

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
