package ui.mvc.brainRepository;

import dao.mvc.BrainRepositoryDAO;
import ui.TableModel;

import java.util.List;

public class BrainRepositoryTableModel extends TableModel {

    public BrainRepositoryTableModel(List<BrainRepositoryDAO> smells) {
        super(smells, 2);
        smells = super.castList(BrainRepositoryDAO.class);
        customColumns = new String[]{"Cyclo", "SQL complexity"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        BrainRepositoryDAO brainRepository = (BrainRepositoryDAO) smells.get(row);
        switch (col) {
            case 0: return brainRepository.getAnalyzerId();
            case 1: return brainRepository.getLanguage();
            case 2: return brainRepository.getClassName();
            case 3: return brainRepository.getMainClassName();
            case 4: return brainRepository.getPath();
            case 5: return brainRepository.getInsertDate();
            case 6: return brainRepository.getCyclo();
            case 7: return brainRepository.getSqlComplexity();
            // to complete here...
            default:
                return null;
        }
    }
}
