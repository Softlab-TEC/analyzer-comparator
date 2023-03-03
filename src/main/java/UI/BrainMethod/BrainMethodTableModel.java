package UI.BrainMethod;

import DAO.BrainMethodDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class BrainMethodTableModel extends AbstractTableModel {
    private ArrayList<BrainMethodDAO> brainMethods;

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
