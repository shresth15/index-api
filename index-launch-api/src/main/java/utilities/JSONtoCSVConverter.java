package utilities;

import com.github.opendevl.JFlat;
import org.json.JSONObject;


public class JSONtoCSVConverter {

    private static int counter = 0;

    public static void convertResponseToDataCSV(String JSONInput,String filename) throws Exception {
        JSONObject jo = new JSONObject(JSONInput);
        String newString = jo.getJSONObject("data").toString();
        filename += "_" +counter;
        System.out.println(filename);
        counter++;
        convertJSONToCSV(newString,filename);
    }

    public static void convertJSONToCSV(String JSONInput,String filename) throws Exception {
        JFlat JSONParse = new JFlat(JSONInput);
        JSONParse.json2Sheet().headerSeparator("_").write2csv(filename + ".csv");
    }

}