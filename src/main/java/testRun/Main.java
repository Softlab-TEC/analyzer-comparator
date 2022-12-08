package testRun;

import DTO.PMD.GodClassDTO;
import DataAccess.PMDResultsReader;
import Services.GodClassService;
import UI.GodClassWindow;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void getGoClasses(){
        GodClassService gc = new GodClassService();
        GodClassDTO godClassDTO = new GodClassDTO();
        godClassDTO.setAnalyzerId(2);
        var x = gc.GetGodClassDaoByProps(godClassDTO);
                x.get(0);
    }

    public static void main(String[] args){
        //getGoClasses();
        JFrame frame = new JFrame("Analyzer-Comparator");
        frame.add(new GodClassWindow());
        frame.setVisible(true);
        frame.setSize(500, 2000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
