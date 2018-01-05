package App;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Server {

    // HashMap to store associations between a client and his last query
    public static Map<String, Double> valuesAns = new HashMap<>();

    /**
     * Static method to obtain the last result of a client, given his user name. On the first connection, its 0
     * @param user user name
     * @return The value stored
     */
    public static synchronized Double getValueAns(String user) {

        Double value = valuesAns.get(user);
        if (value == null) return 0.0;
        else return value;

    }

    /**
     * Method to set the last result of a client
     * @param user User name
     * @param value Value of the result
     */
    public static synchronized void setValorAns (String user, Double value) {

        valuesAns.put(user, value);

    }

    public static void main( String args[] ) {

        System.out.println("Server running. Waiting for queries...");
        new PetitionManagerThread().start();

    }

}






















