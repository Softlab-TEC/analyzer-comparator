package testRun;

import DTO.PMD.GodClassDTO;
import DataAccess.PMDResultsReader;
import Services.GodClassService;

import java.io.IOException;

public class Main {
    public void ReadPMFileAndSaveResults(){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readGodClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getGoClasses(){
        GodClassService gc = new GodClassService();
        GodClassDTO godClassDTO = new GodClassDTO();
        godClassDTO.setAnalyzerId(2);
        var x = gc.GetGodClassDaoByProps(godClassDTO);
                x.get(0);
    }

    public static void main(String[] args){
        getGoClasses();
    }
}
