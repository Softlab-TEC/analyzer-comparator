package ui.identity.featureEnvy;

import dao.identity.FeatureEnvyDAO;
import ui.TableModel;

import java.util.List;

public class FeatureEnvyTableModel extends TableModel {

    public FeatureEnvyTableModel(List<FeatureEnvyDAO> featureEnvies){
        super(featureEnvies, 4);
        smells = super.castList(FeatureEnvyDAO.class);
        customColumns = new String[]{"Method Name", "ATFD", "LAA", "FPD"};
        mergeColumns();
    }

    // The object to render in a cell
    public Object getValueAt(int row, int col) {
        FeatureEnvyDAO featureEnvy = (FeatureEnvyDAO) smells.get(row);
        switch (col) {
            case 0: return featureEnvy.getAnalyzerId();
            case 1: return  featureEnvy.getClassName();
            case 2: return  featureEnvy.getMainClassName();
            case 3: return  featureEnvy.getPath();
            case 4: return  featureEnvy.getInsertDate();
            case 5: return  featureEnvy.getMethodName();
            case 6: return  featureEnvy.getAtfd();
            case 7: return  featureEnvy.getLaa();
            case 8: return  featureEnvy.getFdp();
            // to complete here...
            default: return null;
        }
    }
}
