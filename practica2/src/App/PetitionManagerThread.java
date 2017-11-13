package App;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

import Calculator.*;
import Parser.*;

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
            // Inicializamos los streams
            sIn = new DataInputStream(s.getInputStream() );
            sOut = new PrintStream(s.getOutputStream());

            // Leemos la longitud del mensaje o EXIT
            String length = sIn.readLine();

            // Aceptamos peticiones hasta que el usuario cierre la conexión
            while (!length.equals("EXIT")) {

                int msgLength = Integer.valueOf(length);
                byte[] b = new byte[msgLength];
                sIn.read(b, 0, Integer.valueOf(msgLength));

                String content = new String(b);

                StringTokenizer stContent = new StringTokenizer(content, "~");
                String msgType = stContent.nextToken();
                String operation = stContent.nextToken();

                Calculator calculator;

                if (msgType.equals("xml")) {
                    calculator = XmlParser.parseXML(operation);
                }
                else {
                    calculator = JsonParser.parseJSON(operation);
                }

                System.out.println(calculator);

                // Leemos la longitud del proximo mensaje o EXIT
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
