package Parser;

import Calculator.*;

import com.google.gson.*;

import java.io.*;

/**
 * Created by Jorge Gallego Madrid on 13/11/2017.
 */
public class JsonParser {

    private static int fileId = 0;

    /**
     * Parse a message string in JSON to a Calculator object
     * @param msg JSON string
     * @return Calculator object
     */
    public static Calculator parseCalculatorJSON(String msg) {

        PrintWriter jsonFile = null;
        String fileName = "./files/" + Integer.toString(fileId) + ".json";

        try {
            jsonFile = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Calculator c = gson.fromJson(msg, Calculator.class);

        fileId++;

        return c;

    }

}
