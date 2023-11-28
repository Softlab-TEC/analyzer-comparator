package dao.identity;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class GodClassDAO extends CodeSmellDAO {
    private int wmc;
    private int atfd;
    private float tcc;

    public GodClassDAO(){
        super();
    }

    public GodClassDAO(int id,
                       String className,
                       String mainClassName,
                       int wmc,
                       int atfd,
                       float tcc,
                       int analyzerId,
                       String projectName,
                       String path,
                       String language,
                       Date insertDate
    )
    {
        super(id, className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
    }

    public GodClassDAO(String className,
                       String mainClassName,
                       int wmc,
                       int atfd,
                       float tcc,
                       int analyzerId,
                       String projectName,
                       String path,
                       String language,
                       Date insertDate
    ){
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
    }

    public GodClassDAO(String className,
                       String mainClassName,
                       int analyzerId,
                       String projectName,
                       String path,
                       String language,
                       Date insertDate
    ){
        super(className, mainClassName, analyzerId, projectName, path, language, insertDate);
    }


    public GodClassDAO(String className, int analyzerId, Date insertDate){
        super(className, analyzerId, insertDate);
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public int getAtfd() {
        return atfd;
    }

    public void setAtfd(int atfd) {
        this.atfd = atfd;
    }

    public float getTcc() {
        return tcc;
    }

    public void setTcc(float tcc) {
        this.tcc = tcc;
    }

    public ArrayList<GodClassDAO> getGodClasses(String query){
        ArrayList<String> whereStatement = GetCodeSmellHelper.getWhereStatementByBasicProps(this);
        if(!whereStatement.isEmpty()){
            String where = " WHERE " + String.join("AND ", whereStatement);
            query += where;
        }
        
        GodClassDAO godClass;
        ArrayList<GodClassDAO> godClasses = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                godClass = new GodClassDAO(
                    rs.getInt("id"),
                    rs.getString("classname"),
                    rs.getString("mainclassname"),
                    rs.getInt("wmc"),
                    rs.getInt("atfd"),
                    rs.getFloat("tcc"),
                    rs.getInt("analyzerid"),
                    rs.getString("projectname"),
                    rs.getString("path"),
                    rs.getString("language"),
                    rs.getDate("insertdate")
                );
                godClasses.add(godClass);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return godClasses;
    }

    public ArrayList<GodClassDAO> getAllGodClasses(String language, boolean avoidTestFiles){
        String avoidTestsLeft = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String query = "SELECT * FROM GODCLASS t1  WHERE  AND t1.language = '" + language + "'" + avoidTestsLeft;
        return getGodClasses(query);
    }

    public ArrayList<GodClassDAO> getGodClassesOnlyAnalyzer(int analyzerId, String language, String projectName,
                                                            boolean avoidTestFiles){
        String query = getOnlyOneAnalyzerResult("GODCLASS", analyzerId, language, projectName, avoidTestFiles);
        return getGodClasses(query);
    }

    public void save(){
        String sql = "INSERT INTO GODCLASS(classname, mainclassname, wmc, atfd, tcc, analyzerid, projectname, path, language)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getConnection()){
            
            PreparedStatement pstmt1 = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt1.setString(1, className);
            pstmt1.setString(2, mainClassName);
            pstmt1.setInt(3, wmc);
            pstmt1.setInt(4, atfd);
            pstmt1.setFloat(5, tcc);
            pstmt1.setInt(6, analyzerId);
            pstmt1.setString(7, projectName);
            pstmt1.setString(8, path);
            pstmt1.setString(9, language);

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
