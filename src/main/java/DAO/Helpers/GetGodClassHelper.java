package DAO.Helpers;

import DAO.GodClassDAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetGodClassHelper {
    public static ArrayList<String> getWhereStatementByBasicProps(GodClassDAO godClassDao){
        ArrayList<String> whereStmts = new ArrayList<>();
        if(godClassDao.getClassName() != null && !godClassDao.getClassName().isEmpty()){
            whereStmts.add("classname LIKE ?");
        }
        if(godClassDao.getAnalyzerId() != 0){
            whereStmts.add("analyzerid = ?");
        }
        if(godClassDao.getInsertDate() != null){
            whereStmts.add("insertdate > ?");
        }
        return whereStmts;
    }

    public static void preparedStatementByBasicProps(GodClassDAO godClassDao, PreparedStatement pstmt) throws SQLException {
        int i = 0;
        if(godClassDao.getClassName() != null && !godClassDao.getClassName().isEmpty()){
            pstmt.setString(++i, godClassDao.getClassName());
        }
        if(godClassDao.getAnalyzerId() != 0){
            int analizerid = godClassDao.getAnalyzerId();
            pstmt.setInt(++i, analizerid);
        }
        if(godClassDao.getInsertDate() != null){
            pstmt.setDate(++i, (java.sql.Date) godClassDao.getInsertDate());
        }
    }

}
