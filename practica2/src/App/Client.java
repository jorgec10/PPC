package App;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Calculator.*;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Client {


    public static void main( String args[] ) {

        Socket myClient;
        DataInputStream input;
        DataOutputStream output;

        try {
            // Start socket and streams
            myClient = new Socket ("localhost", 9999);
            input = new DataInputStream(myClient.getInputStream());
            output = new DataOutputStream(myClient.getOutputStream());

            // Read user name
            String name = AppUtils.readUserName();

            // Ask for queries while the client wants to
            while (true) {

                //------------------- Sending the query -------------------

                // Ask for the message format of the query
                boolean xmlNoJson = AppUtils.askMessageFormat();

                // Read the query from the user
                String operation = AppUtils.readOperation();

                // If the user introduce EXIT, quit the app
                if (operation.equalsIgnoreCase("EXIT")) {
                    output.writeBytes("EXIT\n");
                    System.out.println("See you soon!");
                    break;
                }

                // Creation of the Calculator object
                Calculator calculator = AppUtils.createCalculator(name, operation);

                // Preparing the message
                String message = AppUtils.prepareMessage(xmlNoJson, calculator);

                // Sending message to the server
                output.write(message.getBytes());

                //------------------- Receiving response -------------------

                // Read the length of the message
                int msgLength = Integer.valueOf(input.readLine());

                // Read the message
                byte[] b = new byte[msgLength];
                input.read(b, 0, msgLength);

                // Compose the string of the message
                String responseMsg = new String(b);

                // Parse response
                Calculator response = AppUtils.parseResponse(responseMsg);

                // Show the client the result
                System.out.println("Result: " + response.getResult());

            }

            input.close();
            output.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
