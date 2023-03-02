package DAO;

import DAO.Helpers.CodeSmellDAO;
import DAO.Helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class BrainMethodDAO extends CodeSmellDAO {
    private int loc;
    private int cyclo;
    private int maxNesting;
    private int noav;

    public BrainMethodDAO(){
        super();
    }

    public BrainMethodDAO(int id,
                              String className,
                              int loc,
                              int cyclo,
                              int maxNestig,
                              int noav,
                              int analyzerId,
                              String projectName,
                              String path,
                              Date insertDate
    ) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.cyclo = cyclo;
        this.maxNesting = maxNestig;
        this.noav = noav;
    }

    public BrainMethodDAO(String className,
                          int loc,
                          int cyclo,
                          int maxNestig,
                          int noav,
                       int analyzerId,
                       String projectName,
                       String path,
                       Date insertDate
    ){
        super(className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.cyclo = cyclo;
        this.maxNesting = maxNestig;
        this.noav = noav;
    }

    public BrainMethodDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
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

    public ArrayList<BrainMethodDAO> getGodClassesOnlyPMD(){
        String query = "SELECT bm1.* FROM BRAINMETHOD bm1 LEFT JOIN BRAINMETHOD bm2 ON bm1.classname = bm2.classname AND bm2.analyzerid = 1 WHERE bm1.analyzerid = 2 AND bm2.analyzerid IS NULL;";
        return getBrainMethods(query);
    }

    public ArrayList<BrainMethodDAO> getGodClassesOnlyAnalyzer(){
        String query = "SELECT bm1.* FROM BRAINMETHOD bm1 LEFT JOIN BRAINMETHOD bm2 ON bm1.classname = bm2.classname AND bm2.analyzerid = 2 WHERE bm1.analyzerid = 1 AND bm2.analyzerid IS NULL";
        return getBrainMethods(query);
    }

    public void save(){
        String sql = "INSERT INTO BRAINMETHOD(classname, loc, cyclo, maxnesting, noav, analyzerid, projectname, path)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){

            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setInt(2, loc);
            pstmt1.setInt(3, cyclo);
            pstmt1.setInt(4, maxNesting);
            pstmt1.setInt(5, noav);
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
