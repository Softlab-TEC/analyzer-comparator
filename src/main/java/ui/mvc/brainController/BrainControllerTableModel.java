package ui.mvc.brainController;

import dao.mvc.BraomRepositoryDAO;
import ui.TableModel;

import java.util.List;

public class BrainControllerTableModel extends TableModel {

    public BrainControllerTableModel(List<BraomRepositoryDAO> smells) {
        super(smells, 2);
        smells = super.castList(BraomRepositoryDAO.class);
        customColumns = new String[]{"NFRC"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        BraomRepositoryDAO brainController = (BraomRepositoryDAO) smells.get(row);
        switch(col) {
            case 0: return brainController.getAnalyzerId();
            case 1: return brainController.getClassName();
            case 2: return brainController.getPath();
            case 3: return brainController.getInsertDate();
            case 4: return brainController.getNfrc();
            default: return null;
        }
    }
}

