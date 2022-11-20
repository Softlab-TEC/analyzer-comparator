package Services;

import DAO.GodClassDAO;
import DTO.PMD.GodClassDTO;
import Helpers.Converters;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GodClassService {
    private GodClassDAO godClassDAO;
    public ArrayList<GodClassDTO> GetGodClassDaoByProps(GodClassDTO godClassDTO){
        godClassDAO = new GodClassDAO(godClassDTO.getClassName(), godClassDTO.getAnalyzerId(), godClassDTO.getInsertDate());
        var godClassList = godClassDAO.getGodClasses();
        return godClassList.stream().map(g -> Converters.getGodClassDTO(g)).collect(Collectors.toCollection(ArrayList::new));
    }
}
