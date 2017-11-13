package Parser;

import Calculator.*;

import com.google.gson.*;

import java.io.*;

/**
 * Created by Jorge Gallego Madrid on 13/11/2017.
 */
public class JsonParser {

    public static Calculator parseJSON(String msg) {

        /*PrintWriter jsonFile = null;

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
        }*/

        System.out.println("<<" + msg + ">>");

        Gson gson = new Gson();
        Calculator c = gson.fromJson(msg, Calculator.class);

        return c;

    }

}
