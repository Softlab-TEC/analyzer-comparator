package ui.brainMethod;

import dao.BrainMethodDAO;
import ui.TableModel;

import java.util.List;

public class BrainMethodTableModel extends TableModel {

    public BrainMethodTableModel(List<BrainMethodDAO> brainClasses){
        super(brainClasses);
        smells = super.castList(BrainMethodDAO.class);
        customColumns = new String[]{"Method", "LOC", "CYCLO", "MAXNESTING", "NOAV"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        BrainMethodDAO brianClass = (BrainMethodDAO) smells.get(row);
        switch (col) {
            case 0: return brianClass.getAnalyzerId();
            case 1: return  brianClass.getClassName();
            case 2: return brianClass.getMethodName();
            case 3: return  brianClass.getPath();
            case 4: return  brianClass.getInsertDate();
            case 5: return  brianClass.getLoc();
            case 6: return  brianClass.getCyclo();
            case 7: return  brianClass.getMaxNesting();
            case 8: return  brianClass.getNoav();
            // to complete here...
            default: return null;
        }
    }
}
