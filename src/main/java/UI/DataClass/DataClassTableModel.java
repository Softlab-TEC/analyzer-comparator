package UI.DataClass;

import DAO.DataClassDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DataClassTableModel  extends AbstractTableModel {
    private ArrayList<DataClassDAO> dataclasses;

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
