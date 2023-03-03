package UI.FeatureEnvy;

import DAO.FeatureEnvyDAO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FeatureEnvyTableModel extends AbstractTableModel {
    private List<FeatureEnvyDAO> featureEnvies;

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
