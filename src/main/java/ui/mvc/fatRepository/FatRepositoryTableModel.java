package ui.mvc.fatRepository;

import dao.mvc.FatRepositoryDAO;
import ui.TableModel;

import java.util.List;

public class FatRepositoryTableModel extends TableModel {

    public FatRepositoryTableModel(List<FatRepositoryDAO> smells) {
        super(smells, 1);
        smells = super.castList(FatRepositoryDAO.class);
        customColumns = new String[]{"# Entities"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        FatRepositoryDAO fatRepository = (FatRepositoryDAO) smells.get(row);
        switch (col) {
            case 0:
                return fatRepository.getAnalyzerId();
            case 1:
                return fatRepository.getClassName();
            case 2:
                return fatRepository.getMainClassName();
            case 3:
                return fatRepository.getPath();
            case 4:
                return fatRepository.getInsertDate();
            case 5:
                return fatRepository.getEntities();
            // to complete here...
            default:
                return null;
        }
    }
}