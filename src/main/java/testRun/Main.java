package testRun;

import DataAccess.PMDResultsReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readGodClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
