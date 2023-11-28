package dao.identity;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DataClassDAO extends CodeSmellDAO {
    private float woc;
    private int nopa;
    private int noam;
    private int wmc;

    public DataClassDAO(){
        super();
    }

    public DataClassDAO(int id,
                        String className,
                        String mainClassName,
                        float woc,
                        int nopa,
                        int noam,
                        int wmc,
                        int analyzerId,
                        String projectName,
                        String path,
                        String language,
                        Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.woc = woc;
        this.nopa = nopa;
        this.noam = noam;
        this.wmc = wmc;
    }

    public DataClassDAO(String className,
                        String mainClassName,
                        float woc,
                        int nopa,
                        int noam,
                        int wmc,
                        int analyzerId,
                        String projectName,
                        String path,
                        String language,
                        Date insertDate) {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.woc = woc;
        this.nopa = nopa;
        this.noam = noam;
        this.wmc = wmc;
    }

    public DataClassDAO(String className,
                       String mainClassName,
                       int analyzerId,
                       String projectName,
                       String path,
                       String language,
                       Date insertDate
    ){
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
    }

    public DataClassDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getNoam() {
        return noam;
    }

    public void setNoam(int noam) {
        this.noam = noam;
    }

    public float getWoc() {
        return woc;
    }

    public void setWoc(float woc) {
        this.woc = woc;
    }

    public int getNopa() {
        return nopa;
    }

    public void setNopa(int nopa) {
        this.nopa = nopa;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public ArrayList<DataClassDAO> getDataClasses(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        DataClassDAO dataClass;
        ArrayList<DataClassDAO> dataClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                dataClass = new DataClassDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getFloat("woc"),
                        rs.getInt("nopa"),
                        rs.getInt("noam"),
                        rs.getInt("wmc"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                dataClasses.add(dataClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return dataClasses;
    }

    public ArrayList<DataClassDAO> getAllDataClasses(String language, boolean avoidTestFiles){
        String avoidTestsLeft = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String query = "SELECT * FROM DATACLASS t1  WHERE  AND t1.language = '" + language + "'" + avoidTestsLeft;
        return getDataClasses(query);
    }

    public ArrayList<DataClassDAO> getDataClassesOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                              boolean avoidTestFiles){
        String query = getOnlyOneAnalyzerResult("DATACLASS", analyzerId, language, projectName, avoidTestFiles);
        return getDataClasses(query);
    }

    public void save(){
        String sql = "INSERT INTO DATACLASS(classname, mainclassname, woc, nopa, noam, wmc, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setFloat(3, woc);
            pstmt1.setFloat(4, nopa);
            pstmt1.setFloat(5, noam);
            pstmt1.setInt(6, wmc);
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
