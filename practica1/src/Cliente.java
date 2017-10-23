/**
 * Created by Jorge Gallego Madrid on 11/10/2017.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

import static com.oracle.jrockit.jfr.ContentType.Bytes;

public class Cliente {

    public static void main( String args[] ) {
        Socket miCliente;
        DataInputStream entrada;
        DataOutputStream salida;
        try {
            // Inicializamos el socket y los streams
            miCliente = new Socket ("localhost", 9999);
            entrada = new DataInputStream(miCliente.getInputStream());
            salida = new DataOutputStream( miCliente.getOutputStream() );

            // Leemos el nombre de usuario
            Scanner scanner = new Scanner(System.in);
            String nombre, operacion;
            System.out.println("Introduzca su nombre de usuario:");
            nombre = scanner.nextLine();
            nombre.trim();

            boolean fin = false;

            // Se pueden introducir operaciones hasta que el cliente quiera cerrar la conexión
            while (!fin) {

                System.out.println("Introduzca la operacion o EXIT para finalizar la conexion:");
                operacion = scanner.nextLine();

                // Si se introduce "exit" finalizamos
                if (operacion.equals("EXIT")) {
                    salida.writeBytes("EXIT\n");
                    fin = true;
                    break;
                }

                // Obtenemos la operacion y la enviamos junto con el nombre de usuario
                operacion.trim();
                String envio = nombre + ":" + operacion + "\n";
                salida.write(envio.getBytes());

                // Imprimimos el resultado
                System.out.println("Resultado: " + entrada.readLine());

            }

            entrada.close();
            salida.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
