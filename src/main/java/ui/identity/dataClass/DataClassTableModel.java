package ui.identity.dataClass;

import dao.identity.DataClassDAO;
import ui.TableModel;

import java.util.List;

public class DataClassTableModel  extends TableModel {
    public DataClassTableModel(List<DataClassDAO> dataClasses){
        super(dataClasses, 4);
        smells = super.castList(DataClassDAO.class);
        customColumns = new String[] {"WOC", "NOPA", "NOAM", "WMC"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        DataClassDAO dataClass = (DataClassDAO) smells.get(row);
        switch(col) {
            case 0: return dataClass.getAnalyzerId();
            case 1: return dataClass.getLanguage();
            case 2: return dataClass.getClassName();
            case 3: return dataClass.getMainClassName();
            case 4: return dataClass.getPath();
            case 5: return dataClass.getInsertDate();
            case 6: return dataClass.getWoc();
            case 7: return dataClass.getNopa();
            case 8: return dataClass.getNoam();
            case 9: return dataClass.getWmc();
            // to complete here...
            default: return null;
        }
    }
}
