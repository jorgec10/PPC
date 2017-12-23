package App;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

import Calculator.*;
import Parser.JsonParser;
import Parser.XmlParser;
import App.Utils;

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
            String name = Utils.readUserName();

            // Ask for queries while the client wants to
            while (true) {

                //------------------- Sending the query -------------------

                // Ask for the message format of the query
                boolean xmlNoJson = Utils.askMessageFormat();

                // Read the query from the user
                String operation = Utils.readOperation();

                // If the user introduce EXIT, quit the app
                if (operation.equalsIgnoreCase("EXIT")) {
                    output.writeBytes("EXIT\n");
                    System.out.println("See you soon!");
                    break;
                }

                // Creation of the Calculator object
                Calculator calculator = Utils.createCalculator(name, operation);

                // Preparing the message
                String message = Utils.prepareMessage(xmlNoJson, calculator);

                // Sending message to the server
                output.write(message.getBytes());


                //------------------- Receiving response -------------------

                // Imprimimos el resultado

                String length = input.readLine();
                int msgLength = Integer.valueOf(length);
                byte[] b = new byte[msgLength];
                input.read(b, 0, Integer.valueOf(msgLength));
                String response = new String(b);

                StringTokenizer stContent = new StringTokenizer(response, "~");
                String msgType = stContent.nextToken();
                String result = stContent.nextToken();

                if (msgType.equals("xml")) {
                    Calculator r = XmlParser.parseXML(result);
                    System.out.println("Resultado: " + r.getResult());
                }
                else {
                    Calculator r = JsonParser.parseCalculatorJSON(result);
                    System.out.println("Resultado: " + r.getResult());
                }

            }

            input.close();
            output.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
