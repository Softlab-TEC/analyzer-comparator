package UI.GodClass;

import DAO.GodClassDAO;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GodClassTableModel extends AbstractTableModel {
    private List<GodClassDAO> godclasses;
    private String[] columns ;

    public GodClassTableModel(List<GodClassDAO> aClubList){
        super();
        godclasses = aClubList ;
        columns = new String[]{"Analizer", "Class Name", "Path", "ATFD", "WMC", "TCC", "Date"};
    }

    @Override
    public int getRowCount() {
        return godclasses.size();
    }

    // Number of column of your table
        public int getColumnCount() {
            return columns.length ;
        }

        // Number of row of your table
        public int getRowsCount() {
            return godclasses.size();
        }

        // The object to render in a cell
        public Object getValueAt(int row, int col) {
            GodClassDAO godClass = godclasses.get(row);
            switch(col) {
                case 0: return godClass.getAnalyzerId();
                case 1: return godClass.getClassName();
                case 2: return godClass.getPath();
                case 3: return godClass.getAtfd();
                case 4: return godClass.getWmc();
                case 5: return godClass.getTcc();
                case 6: return godClass.getInsertDate();
                // to complete here...
                default: return null;
            }
        }

        // Optional, the name of your column
        public String getColumnName(int col) {
            return columns[col] ;
        }
}
