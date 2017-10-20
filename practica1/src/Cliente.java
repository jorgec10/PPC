/**
 * Created by Jorge Gallego Madrid on 11/10/2017.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {

    public static void main( String args[] ) {
        Socket miCliente;
        DataInputStream entrada;
        DataOutputStream salida;
        try {
            miCliente = new Socket ("localhost", 9999);
            entrada = new DataInputStream(miCliente.getInputStream());
            salida = new DataOutputStream( miCliente.getOutputStream() );

            // Leemos los operandos
            Scanner scanner = new Scanner(System.in);
            String nombre, operacion;
            System.out.println("Introduzca su nombre de usuario:");
            nombre = scanner.nextLine();
            nombre.trim();

            boolean fin = false;

            while (!fin) {

                System.out.println("Introduzca la operacion o EXIT para finalizar la conexion:");
                operacion = scanner.nextLine();

                if (operacion.equals("EXIT")) {
                    salida.writeBytes("EXIT\n");
                    fin = true;
                    break;
                }

                operacion.trim();
                salida.writeBytes(nombre + ' ' + operacion + "\n"); // Envío datos

                System.out.println("Resultado: " + entrada.readLine()); // Recibo el eco

            }
            entrada.close();
            salida.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
