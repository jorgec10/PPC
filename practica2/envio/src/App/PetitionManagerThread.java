package App;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import Calculator.*;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class PetitionManagerThread extends Thread{

    Socket s;

    public PetitionManagerThread (Socket s ) {
        this.s = s;
    }

    public void run() {
        DataInputStream sIn;
        PrintStream sOut;
        try {
            // Start streams
            sIn = new DataInputStream(s.getInputStream() );
            sOut = new PrintStream(s.getOutputStream());

            // Read message length or EXIT
            String length = sIn.readLine();

            // Accept queries until the user closes the connection
            while (!length.equals("EXIT")) {

                // Read message length
                int msgLength = Integer.valueOf(length);

                // Read message
                byte[] b = new byte[msgLength];
                sIn.read(b, 0, msgLength);

                // Compose the string of the message
                String content = new String(b);

                // Parse message
                Calculator calculator = AppUtils.parseResponse(content);
                String msgType = AppUtils.getMessageType(content);

                // Print calculator received
                System.out.println(calculator);

                // Calculate result of the query
                double result = AppUtils.calculateResult(calculator);

                // Store the result
                Server.setValorAns(calculator.getUser(), result);

                // Preparing response
                calculator.setResult(String.valueOf(result));
                String send = AppUtils.prepareMessage(msgType.equalsIgnoreCase("xml"), calculator);

                // Send the response
                sOut.write(send.getBytes());

                // Read length of the next query or EXIT
                length = sIn.readLine();
            }

            sIn.close();
            sOut.close();
            s.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
