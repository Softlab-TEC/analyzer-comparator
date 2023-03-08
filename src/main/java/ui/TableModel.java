package ui;

import dao.helpers.CodeSmellDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel<T extends CodeSmellDAO> extends AbstractTableModel {
    protected List<T> smells;
    protected String[] columns;
    protected String[] customColumns;
    protected String[] temp;

    public TableModel(List<T> smells){
        this.smells = smells;
        columns = new String[]{"Analizer", "Class Name", "Path",  "Date"};
        temp = new String[columns.length + 3];
        System.arraycopy(columns, 0,  temp, 0 , columns.length);
    }

    protected void mergeColumns(){
        System.arraycopy(customColumns, 0, temp, columns.length, customColumns.length);
        columns = temp;
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
