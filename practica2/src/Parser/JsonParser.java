package Parser;

import Calculator.*;

import com.google.gson.*;

import java.io.*;

/**
 * Created by Jorge Gallego Madrid on 13/11/2017.
 */
public class JsonParser {

    public static Calculator parseCalculatorJSON(String msg) {

        PrintWriter jsonFile = null;

        try {
            jsonFile = new PrintWriter("./tmp.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter("./tmp.json", false);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader r = null;
        try {
            r = new InputStreamReader(new FileInputStream("./tmp.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Calculator c = gson.fromJson(msg, Calculator.class);

        return c;

    }

    public static Response parseResponseJSON (String msg) {

        Gson gson = new Gson();
        Response r = gson.fromJson(msg, Response.class);
        return r;

    }

}
