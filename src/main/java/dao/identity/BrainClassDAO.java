package dao.identity;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import utils.AnalyzerConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BrainClassDAO extends CodeSmellDAO {
    private int loc;
    private int wmc;
    private float tcc;
    private int numberBrainMethods;

    public BrainClassDAO(){
        super();
    }

    public BrainClassDAO(
            int id,
            String className,
            String mainClassName,
            int loc,
            int wmc,
            float tcc,
            int numberBrainMethods,
            int analyzerId,
            String projectName,
            String path,
            String language,
            Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.loc = loc;
        this.wmc = wmc;
        this.tcc = tcc;
        this.numberBrainMethods = numberBrainMethods;
    }

    public BrainClassDAO(
            String className,
            String mainClassName,
            int loc,
            int wmc,
            float tcc,
            int numberBrainMethods,
            int analyzerId,
            String projectName,
            String path,
            String language,
            Date insertDate
    )
    {
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.loc = loc;
        this.wmc = wmc;
        this.tcc = tcc;
        this.numberBrainMethods = numberBrainMethods;
    }

    public BrainClassDAO(String className,
                       String mainClassName,
                       int analyzerId,
                       String projectName,
                       String path,
                       String language,
                       Date insertDate
    ){
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
    }

    public BrainClassDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public float getTcc() {
        return tcc;
    }

    public void setTcc(float tcc) {
        this.tcc = tcc;
    }

    public int getNumberBrainMethods() {
        return numberBrainMethods;
    }

    public void setNumberBrainMethods(int numberBrainMethods) {
        this.numberBrainMethods = numberBrainMethods;
    }

    public ArrayList<BrainClassDAO> getBrainClasses(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        BrainClassDAO brainClass;
        ArrayList<BrainClassDAO> brainClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                brainClass = new BrainClassDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("mainclassname"),
                        rs.getInt("loc"),
                        rs.getInt("wmc"),
                        rs.getFloat("tcc"),
                        rs.getInt("numberbrainmethods"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getString("language"),
                        rs.getDate("insertdate")
                );
                brainClasses.add(brainClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainClasses;
    }

    public ArrayList<BrainClassDAO> getBrainClassesOnlyPMD(int analyzerId, String language, String projectName){
        String query = "SELECT bc1.* FROM BRAINCLASS bc1 LEFT JOIN BRAINCLASS bc2 ON bc1.classname = bc2.classname AND bc2.analyzerid = " + AnalyzerConstants.ANALYZER + " WHERE bc1.analyzerid = " + AnalyzerConstants.PMD + " AND bc2.analyzerid IS NULL;";
        return getBrainClasses(query);
    }

    public ArrayList<BrainClassDAO> getBrainClassesOnlyAnalyzer(int analyzerId, String language, String projectName){
        String query = "SELECT dc1.* FROM BRAINCLASS dc1 LEFT JOIN BRAINCLASS dc2 ON (dc1.classname = dc2.classname  OR dc1.mainclassname = dc2.mainclassname) AND dc2.analyzerid <> " + analyzerId +
                " AND dc2.path LIKE '% " + projectName + "%' AND dc2.language = '"+ language + "' WHERE dc1.analyzerid = " + analyzerId
                + " AND dc2.analyzerid IS NULL AND dc1.path LIKE '% " + projectName + "%' AND dc1.language = '"+ language + "'";
        return getBrainClasses(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINCLASS(classname, mainclassname, loc, wmc, tcc, numberbrainmethods, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, loc);
            pstmt1.setInt(4, wmc);
            pstmt1.setFloat(5, tcc);
            pstmt1.setInt(6, numberBrainMethods);
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
