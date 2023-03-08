package ui.brainClass;

import dao.BrainClassDAO;
import ui.TableModel;

import java.util.List;

public class BrainClassTableModel extends TableModel {
    public BrainClassTableModel(List<BrainClassDAO> brainClasses){
        super(brainClasses);
        smells = super.castList(BrainClassDAO.class);
        customColumns = new String[]{"LOC", "WMC", "TCC", "# Brain methods"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        BrainClassDAO brainClass = (BrainClassDAO) smells.get(row);
        switch(col) {
            case 0: return brainClass.getAnalyzerId();
            case 1: return brainClass.getClassName();
            case 2: return brainClass.getPath();
            case 3: return brainClass.getInsertDate();
            case 4: return brainClass.getLoc();
            case 5: return brainClass.getWmc();
            case 6: return brainClass.getTcc();
            case 7: return brainClass.getNumberBrainMethods();
            // to complete here...
            default: return null;
        }
    }
}
