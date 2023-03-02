package DAO.Helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetCodeSmellHelper {
    public static ArrayList<String> getWhereStatementByBasicProps(CodeSmellDAO codeSmellDAO){
        ArrayList<String> whereStmts = new ArrayList<>();
        if(codeSmellDAO.getClassName() != null && !codeSmellDAO.getClassName().isEmpty()){
            whereStmts.add("classname LIKE ?");
        }
        if(codeSmellDAO.getAnalyzerId() != 0){
            whereStmts.add("analyzerid = ?");
        }
        if(codeSmellDAO.getInsertDate() != null){
            whereStmts.add("insertdate > ?");
        }
        return whereStmts;
    }

    public static void preparedStatementByBasicProps(CodeSmellDAO codeSmellDAO, PreparedStatement pstmt) throws SQLException {
        int i = 0;
        if(codeSmellDAO.getClassName() != null && !codeSmellDAO.getClassName().isEmpty()){
            pstmt.setString(++i, codeSmellDAO.getClassName());
        }
        if(codeSmellDAO.getAnalyzerId() != 0){
            int analizerid = codeSmellDAO.getAnalyzerId();
            pstmt.setInt(++i, analizerid);
        }
        if(codeSmellDAO.getInsertDate() != null){
            pstmt.setDate(++i, (java.sql.Date) codeSmellDAO.getInsertDate());
        }
    }

}
