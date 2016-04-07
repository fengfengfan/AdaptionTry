import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by fun on 15/10/29.
 */
public class ReadData {
    private String filePath;
    private int lengthOfTime;

    public ReadData (String filePath , int lengthOfTime){
        this.filePath = filePath;
        this.lengthOfTime = lengthOfTime;
    }

    public String getFileContent() throws IOException {
        File file = new File(filePath);
        FileInputStream fileIn = new FileInputStream(file);
        FileChannel fchan = fileIn.getChannel();
        MappedByteBuffer buf = fchan.map(FileChannel.MapMode.READ_ONLY,0,file.length());
        byte[] b = new byte[(int) file.length()];
        int len = 0;
        while (buf.hasRemaining()){
            b[len] = buf.get();
            len++;
        }
        fchan.close();
        fileIn.close();
        return new String(b,0,len,"GBK");
    }
    public ArrayList<ArrayList<String>> tidyData(String fileContent) {
        //put every row of fileContent into finalList
        String[] contentAfterSplitEnter = fileContent.split("\n");
        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0;i < contentAfterSplitEnter.length;i++){
            for (int j = 0; j < contentAfterSplitEnter[i].split(",").length; j++) {
                temp.add(contentAfterSplitEnter[i].split(",")[j]);
            }
            if(temp.size() < lengthOfTime){
                for(int j = temp.size();j < lengthOfTime;j++){
                    temp.add(" ");
                }
            }
            finalList.add((ArrayList<String>) temp.clone());
            temp.clear();
        }
        return finalList;
    }

    public ArrayList<ArrayList<String>> generateTimeSeriesData (ArrayList<ArrayList<String>> data){
        //put every column of data into finalList
        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0;i < lengthOfTime;i++){
            for (int j = 0; j < data.size(); j++) {
                temp.add(data.get(j).get(i));
            }
            finalList.add((ArrayList<String>) temp.clone());
            temp.clear();
        }
        return finalList;
    }

}
