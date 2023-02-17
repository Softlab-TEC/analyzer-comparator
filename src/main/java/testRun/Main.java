package testRun;

import DTO.PMD.GodClassDTO;
import Services.GodClassService;
import UI.MainWindow;

public class Main {
    public static void getGoClasses(){
        GodClassService gc = new GodClassService();
        GodClassDTO godClassDTO = new GodClassDTO();
        godClassDTO.setAnalyzerId(2);
        var x = gc.GetGodClassDaoByProps(godClassDTO);
                x.get(0);
    }

    public static void main(String[] args){
        MainWindow mm = new MainWindow();
        //getGoClasses();
        /*JFrame frame = new JFrame();
        frame.add(new GodClassWindow());
        frame.setVisible(true);
        frame.setSize(500, 2000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         */
    }
}
