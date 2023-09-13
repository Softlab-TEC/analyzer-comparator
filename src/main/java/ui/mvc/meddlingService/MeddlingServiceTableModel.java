package ui.mvc.meddlingService;

import dao.mvc.MeddlingServiceDAO;
import ui.TableModel;

import java.util.List;

public class MeddlingServiceTableModel extends TableModel {

    public MeddlingServiceTableModel(List<MeddlingServiceDAO> smells) {
        super(smells, 1);
        smells = super.castList(MeddlingServiceDAO.class);
        customColumns = new String[]{"# Persistence"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        MeddlingServiceDAO meddlingService = (MeddlingServiceDAO) smells.get(row);
        switch(col) {
            case 0: return meddlingService.getAnalyzerId();
            case 1: return meddlingService.getLanguage();
            case 2: return meddlingService.getClassName();
            case 3: return meddlingService.getMainClassName();
            case 4: return meddlingService.getPath();
            case 5: return meddlingService.getInsertDate();
            case 6: return meddlingService.getPersistenceDependencies();
            default: return null;
        }
    }
}
