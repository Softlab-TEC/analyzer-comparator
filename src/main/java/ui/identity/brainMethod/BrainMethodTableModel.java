package ui.identity.brainMethod;

import dao.identity.BrainMethodDAO;
import ui.TableModel;

import java.util.List;

public class BrainMethodTableModel extends TableModel {

    public BrainMethodTableModel(List<BrainMethodDAO> brainMethods){
        super(brainMethods, 5);
        smells = super.castList(BrainMethodDAO.class);
        customColumns = new String[]{"Method", "LOC", "CYCLO", "MAXNESTING", "NOAV"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        BrainMethodDAO brianClass = (BrainMethodDAO) smells.get(row);
        switch (col) {
            case 0: return brianClass.getAnalyzerId();
            case 1: return brianClass.getLanguage();
            case 2: return  brianClass.getClassName();
            case 3: return  brianClass.getMainClassName();
            case 4: return brianClass.getMethodName();
            case 5: return  brianClass.getPath();
            case 6: return  brianClass.getInsertDate();
            case 7: return  brianClass.getLoc();
            case 8: return  brianClass.getCyclo();
            case 9: return  brianClass.getMaxNesting();
            case 10: return  brianClass.getNoav();
            // to complete here...
            default: return null;
        }
    }
}
