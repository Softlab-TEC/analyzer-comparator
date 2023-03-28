package ui.identity.featureEnvy;

import dao.identity.FeatureEnvyDAO;
import ui.TableModel;

import java.util.List;

public class FeatureEnvyTableModel extends TableModel {

    public FeatureEnvyTableModel(List<FeatureEnvyDAO> featureEnvies){
        super(featureEnvies, 3);
        smells = super.castList(FeatureEnvyDAO.class);
        customColumns = new String[]{"ATFD", "LAA", "FPD"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        FeatureEnvyDAO featureEnvy = (FeatureEnvyDAO) smells.get(row);
        switch (col) {
            case 0: return featureEnvy.getAnalyzerId();
            case 1: return  featureEnvy.getClassName();
            case 2: return  featureEnvy.getPath();
            case 3: return  featureEnvy.getInsertDate();
            case 4: return  featureEnvy.getAtfd();
            case 5: return  featureEnvy.getLaa();
            case 6: return  featureEnvy.getFdp();
            // to complete here...
            default: return null;
        }
    }
}
