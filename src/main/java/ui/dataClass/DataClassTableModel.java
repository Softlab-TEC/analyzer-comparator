package ui.dataClass;

import dao.DataClassDAO;
import ui.TableModel;

import java.util.List;

public class DataClassTableModel  extends TableModel {
    public DataClassTableModel(List<DataClassDAO> featureEnvies){
        super(featureEnvies);
        smells = super.castList(DataClassDAO.class);
        customColumns = new String[] {"WOC", "NOPA", "NOAM", "WMC"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        DataClassDAO dataClass = (DataClassDAO) smells.get(row);
        switch(col) {
            case 0: return dataClass.getAnalyzerId();
            case 1: return dataClass.getClassName();
            case 2: return dataClass.getPath();
            case 3: return dataClass.getInsertDate();
            case 4: return dataClass.getWoc();
            case 5: return dataClass.getNopa();
            case 6: return dataClass.getNoam();
            case 7: return dataClass.getWmc();
            // to complete here...
            default: return null;
        }
    }
}
