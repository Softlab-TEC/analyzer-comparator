package services;

import dao.GodClassDAO;
import DTO.PMD.GodClassDTO;
import helpers.Converters;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GodClassService {
    private GodClassDAO godClassDAO;
    public ArrayList<GodClassDTO> GetGodClassDaoByProps(GodClassDTO godClassDTO){
        godClassDAO = new GodClassDAO(godClassDTO.getClassName(), godClassDTO.getAnalyzerId(), godClassDTO.getInsertDate());
        String sql = "SELECT * FROM GODCLASS ORDER BY classname, analyzerid, insertdate";
        var godClassList = godClassDAO.getGodClasses(sql);
        return godClassList.stream().map(g -> Converters.getGodClassDTO(g)).collect(Collectors.toCollection(ArrayList::new));
    }
}
