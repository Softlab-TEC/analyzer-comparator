package UI;

import DAO.Helpers.CodeSmellDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel<T extends CodeSmellDAO> extends AbstractTableModel {
    protected List<T> smells;
    protected String[] columns;

    public TableModel(List<T> smells){
        this.smells = smells;
    }

    @Override
    public int getRowCount() {
        return smells.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    // Optional, the name of your column
    public String getColumnName(int col) {
        return columns[col] ;
    }

    protected <T> List<T> castList(Class<T> clazz){
        List<T> specificSmells = new ArrayList<>();
        for(var smell : smells){
            if(clazz.isInstance(smell)){
                T subclass = clazz.cast(smell);
                specificSmells.add(subclass);
            }
        }
        return  specificSmells;
    }

}
