/**
 * Created by fun on 15/10/27.
 */

import java.io.IOException;
import java.util.ArrayList;

public class My1stJava {

    static int timeLength = 10;  //length of time of synthetic data
    static String filePath = "1.txt";
    static int support = 3;
    static int POI = 5;

    public static void main(String[] args) throws IOException {
        
        ReadData readData = new ReadData(filePath,timeLength);
        String fileContent = readData.getFileContent();
        ArrayList<ArrayList<String>> timeSeriesData = readData.generateTimeSeriesData(readData.tidyData(fileContent));
        for(int i = 0;i < timeSeriesData.size();i++){
            System.out.println(timeSeriesData.get(i));
        }
        System.out.println(Runtime.getRuntime().availableProcessors());
        ParallelPisa myPisa = new ParallelPisa(support,POI);
        myPisa.parallelPisaAlgorithm(timeSeriesData, timeLength);
    }
}
