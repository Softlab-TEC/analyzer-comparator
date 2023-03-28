package ui.mvc.laboriousRepositoryMethod;

import dao.mvc.LaboriousRepositoryMethodDAO;
import ui.TableModel;

import java.util.List;

public class LaboriousRepositoryMethodTableModel extends TableModel {

    public LaboriousRepositoryMethodTableModel(List<LaboriousRepositoryMethodDAO> smells) {
        super(smells, 1);
        smells = super.castList(LaboriousRepositoryMethodDAO.class);
        customColumns = new String[]{"# Actions"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        LaboriousRepositoryMethodDAO laboriousRepoMethod = (LaboriousRepositoryMethodDAO) smells.get(row);
        switch (col) {
            case 0:
                return laboriousRepoMethod.getAnalyzerId();
            case 1:
                return laboriousRepoMethod.getClassName();
            case 2:
                return laboriousRepoMethod.getPath();
            case 3:
                return laboriousRepoMethod.getInsertDate();
            case 4:
                return laboriousRepoMethod.getQuantityPersistenceActions();
            default:
                return null;
        }
    }
}