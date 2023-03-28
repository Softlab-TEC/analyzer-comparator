package ui.mvc.promiscuousController;

import dao.mvc.PromiscuousControllerDAO;
import ui.TableModel;

import java.util.List;


public class PromiscuousControllerTableModel extends TableModel {

    public PromiscuousControllerTableModel(List<PromiscuousControllerDAO> smells) {
        super(smells, 2);
        smells = super.castList(PromiscuousControllerDAO.class);
        customColumns = new String[]{"NOR", "NSD"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        PromiscuousControllerDAO promiscuousController = (PromiscuousControllerDAO) smells.get(row);
        switch(col) {
            case 0: return promiscuousController.getAnalyzerId();
            case 1: return promiscuousController.getClassName();
            case 2: return promiscuousController.getPath();
            case 3: return promiscuousController.getInsertDate();
            case 4: return promiscuousController.getNor();
            case 5: return promiscuousController.getNsd();
            // to complete here...
            default: return null;
        }
    }
}
