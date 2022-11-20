package Helpers;

import DAO.GodClassDAO;
import DTO.PMD.GodClassDTO;

import java.util.Date;

//Todo: Change this class for a mapper
public class Converters {
    public static GodClassDTO getGodClassDTO(GodClassDAO godClassDAO){
        return new GodClassDTO(
                godClassDAO.getId(),
                godClassDAO.getClassName() ,
                godClassDAO.getWmc(),
                godClassDAO.getAtfd(),
                godClassDAO.getTcc(),
                godClassDAO.getAnalyzerId(),
                godClassDAO.getProjectName(),
                godClassDAO.getPath(),
                godClassDAO.getInsertDate()
        );
    }
}
