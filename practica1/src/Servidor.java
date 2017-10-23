/**
 * Created by Jorge Gallego Madrid on 11/10/2017.
 */

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    public static Map<String, Double> valoresAns = new HashMap<>();

    public static synchronized Double getValorAns (String usuario) {

        Double valor = valoresAns.get(usuario);
        if (valor == null) return 0.0;
        else return valor;

    }

    public static synchronized void setValorAns (String usuario, Double valor) {

        valoresAns.put(usuario, valor);

    }

    public static void main( String args[] ) {
        ServerSocket s = null;
        Socket cliente = null;

        // Establecemos el servicio en el puerto 9999
        // No podemos elegir un puerto por debajo del 1023 si no somos
        // usuarios con los máximos privilegios (root)
        try {
            s = new ServerSocket( 9999 );
        } catch( IOException e ) {
            System.out.println( e );
        }
        // Creamos el objeto desde el cual atenderemos y aceptaremos
        // las conexiones de los clientes y abrimos los canales de
        // comunicación de entrada y salida
        System.out.println("Esperando solicitudes...");
        while (true) {
            try {
                cliente = s.accept();
                System.out.println("Conexion aceptada: " + cliente.getInetAddress() + ":" + cliente.getPort());
                new GestorPeticionThread(cliente).start();
            } catch( IOException e ) {
                System.out.println( e );
            }
        }
    }

}
