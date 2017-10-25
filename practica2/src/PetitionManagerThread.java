import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

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
            // Recibimos los datos
            String texto = sIn.readLine();

            // Aceptamos peticiones hasta que el usuario cierre la conexión
            while (!texto.equals("EXIT")) {

                // Parseamos el nombre del cliente

                // Parseamos la operacion

                // Resolvemos

                // Guardamos el valor del resultado

                // Devolvemos el resultado

            }

            sIn.close();
            sOut.close();
            s.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
