package UI.GodClass;

import DAO.GodClassDAO;
import UI.TableModel;

import java.util.List;

public class GodClassTableModel extends TableModel {

    public GodClassTableModel(List<GodClassDAO> godClasses){
        super(godClasses);
        smells = super.castList(GodClassDAO.class);
        columns = new String[]{"Analizer", "Class Name", "Path", "ATFD", "WMC", "TCC", "Date"};
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        GodClassDAO godClass = (GodClassDAO) smells.get(row);
        switch(col) {
            case 0: return godClass.getAnalyzerId();
            case 1: return godClass.getClassName();
            case 2: return godClass.getPath();
            case 3: return godClass.getAtfd();
            case 4: return godClass.getWmc();
            case 5: return godClass.getTcc();
            case 6: return godClass.getInsertDate();
            // to complete here...
            default: return null;
        }
    }

}
