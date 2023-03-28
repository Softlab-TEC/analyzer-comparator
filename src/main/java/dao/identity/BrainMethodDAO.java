package dao.identity;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BrainMethodDAO extends CodeSmellDAO {
    private String methodName;
    private int loc;
    private int cyclo;
    private int maxNesting;
    private int noav;

    public BrainMethodDAO(){
        super();
    }

    public BrainMethodDAO(int id,
                          String className,
                          String methodName,
                          int loc,
                          int cyclo,
                          int maxNesting,
                          int noav,
                          int analyzerId,
                          String projectName,
                          String path,
                          Date insertDate
    ) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.methodName = methodName;
        this.loc = loc;
        this.cyclo = cyclo;
        this.maxNesting = maxNesting;
        this.noav = noav;
    }

    public BrainMethodDAO(String className,
                          String methodName,
                          int loc,
                          int cyclo,
                          int maxNesting,
                          int noav,
                       int analyzerId,
                       String projectName,
                       String path,
                       Date insertDate
    ){
        super(className, analyzerId, projectName, path, insertDate);
        this.methodName = methodName;
        this.loc = loc;
        this.cyclo = cyclo;
        this.maxNesting = maxNesting;
        this.noav = noav;
    }

    public BrainMethodDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getCyclo() {
        return cyclo;
    }

    public void setCyclo(int cyclo) {
        this.cyclo = cyclo;
    }

    public int getMaxNesting() {
        return maxNesting;
    }

    public void setMaxNesting(int maxNesting) {
        this.maxNesting = maxNesting;
    }

    public int getNoav() {
        return noav;
    }

    public void setNoav(int noav) {
        this.noav = noav;
    }

    public ArrayList<BrainMethodDAO> getBrainMethods(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }

        BrainMethodDAO brainMethod;
        ArrayList<BrainMethodDAO> brainMethods = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                brainMethod = new BrainMethodDAO(
                        rs.getInt("id"),
                        rs.getString("classname"),
                        rs.getString("methodname"),
                        rs.getInt("loc"),
                        rs.getInt("cyclo"),
                        rs.getInt("maxnesting"),
                        rs.getInt("noav"),
                        rs.getInt("analyzerid"),
                        rs.getString("projectname"),
                        rs.getString("path"),
                        rs.getDate("insertdate")
                );
                brainMethods.add(brainMethod);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return brainMethods;
    }

    public ArrayList<BrainMethodDAO> getBrainMethodsOnlyPMD(){
        String query = "SELECT bm1.* FROM BRAINMETHOD bm1 LEFT JOIN BRAINMETHOD bm2 ON bm1.classname = bm2.classname AND bm1.methodname = bm2.methodname AND bm2.analyzerid = 1 WHERE bm1.analyzerid = 2 AND bm2.analyzerid IS NULL;";
        return getBrainMethods(query);
    }

    public ArrayList<BrainMethodDAO> getBrainMethodsOnlyAnalyzer(){
        String query = "SELECT bm1.* FROM BRAINMETHOD bm1 LEFT JOIN BRAINMETHOD bm2 ON bm1.classname = bm2.classname AND bm1.methodname = bm2.methodname AND bm2.analyzerid = 2 WHERE bm1.analyzerid = 1 AND bm2.analyzerid IS NULL";
        return getBrainMethods(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINMETHOD(classname, methodname ,loc, cyclo, maxnesting, noav, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, methodName);
            pstmt1.setInt(3, loc);
            pstmt1.setInt(4, cyclo);
            pstmt1.setInt(5, maxNesting);
            pstmt1.setInt(6, noav);
            pstmt1.setInt(7, analyzerId);
            pstmt1.setString(8, projectName);
            pstmt1.setString(9, path);

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
