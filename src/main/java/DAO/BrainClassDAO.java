package DAO;

import DAO.Helpers.CodeSmellDAO;
import DAO.Helpers.GetCodeSmellHelper;
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
            int loc,
            int wmc,
            float tcc,
            int numberBrainMethods,
            int analyzerId,
            String projectName,
            String path,
            Date insertDate
    )
    {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.wmc = wmc;
        this.tcc = tcc;
        this.numberBrainMethods = numberBrainMethods;
    }

    public BrainClassDAO(
            String className,
            int loc,
            int wmc,
            float tcc,
            int numberBrainMethods,
            int analyzerId,
            String projectName,
            String path,
            Date insertDate
    )
    {
        super(className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.wmc = wmc;
        this.tcc = tcc;
        this.numberBrainMethods = numberBrainMethods;
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

    public ArrayList<BrainClassDAO> getGodClasses(String query){
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
                        rs.getInt("loc"),
                        rs.getInt("wmc"),
                        rs.getFloat("tcc"),
                        rs.getInt("numberbrainmethods"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                brainClasses.add(brainClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainClasses;
    }

    public ArrayList<BrainClassDAO> getGodClassesOnlyPMD(){
        String query = "SELECT bc1.* FROM BRAINCLASS bc1 LEFT JOIN BRAINCLASS bc2 ON bc1.classname = bc2.classname AND bc2.analyzerid = 1 WHERE bc1.analyzerid = 2 AND bc2.analyzerid IS NULL;";
        return getGodClasses(query);
    }

    public ArrayList<BrainClassDAO> getGodClassesOnlyAnalyzer(){
        String query = "SELECT bc1.* FROM BRAINCLASS bc1 LEFT JOIN BRAINCLASS bc2 ON bc1.classname = bc2.classname AND bc2.analyzerid = 2 WHERE bc1.analyzerid = 1 AND bc2.analyzerid IS NULL";
        return getGodClasses(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINCLASS(classname, loc, wmc, tcc, numberbrainmethods, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, loc);
            pstmt1.setInt(3, wmc);
            pstmt1.setFloat(4, tcc);
            pstmt1.setInt(5, numberBrainMethods);
            pstmt1.setInt(6, analyzerId);
            pstmt1.setString(7, projectName);
            pstmt1.setString(8, path);

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
