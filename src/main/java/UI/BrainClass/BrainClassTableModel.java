package UI.BrainClass;

import DAO.BrainClassDAO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BrainClassTableModel extends AbstractTableModel {
    private List<BrainClassDAO> brainClasses;

    public BrainClassTableModel(List<BrainClassDAO> brainClasses){
        super();
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
