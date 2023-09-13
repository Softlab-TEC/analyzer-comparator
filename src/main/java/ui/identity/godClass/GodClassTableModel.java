package ui.identity.godClass;

import dao.identity.GodClassDAO;
import ui.TableModel;

import java.util.List;

public class GodClassTableModel extends TableModel {

    public GodClassTableModel(List<GodClassDAO> godClasses){
        super(godClasses, 3);
        smells = super.castList(GodClassDAO.class);
        customColumns = new String[]{"ATFD", "WMC", "TCC"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        GodClassDAO godClass = (GodClassDAO) smells.get(row);
        switch(col) {
            case 0: return godClass.getAnalyzerId();
            case 1: return godClass.getLanguage();
            case 2: return godClass.getClassName();
            case 3: return godClass.getMainClassName();
            case 4: return godClass.getPath();
            case 5: return godClass.getInsertDate();
            case 6: return godClass.getAtfd();
            case 7: return godClass.getWmc();
            case 8: return godClass.getTcc();
            // to complete here...
            default: return null;
        }
    }

}
