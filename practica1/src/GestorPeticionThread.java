/**
 * Created by Jorge Gallego Madrid on 11/10/2017.
 */

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class GestorPeticionThread extends Thread{

    Socket s;

    public GestorPeticionThread (Socket s ) {
        this.s = s;
    }

    public void run() {
        DataInputStream sIn;
        PrintStream sOut;
        try {
            sIn = new DataInputStream(s.getInputStream() );
            sOut = new PrintStream(s.getOutputStream());
            String texto = sIn.readLine(); // Recibo datos

            while (!texto.equals("EXIT")) {

                // Parseamos el nombre del cliente
                StringTokenizer stNombre = new StringTokenizer(texto, " ");
                String nombre = stNombre.nextToken();
                String operacion = stNombre.nextToken();
                System.out.println("Nombre del cliente: " + nombre);

                // Parseamos la operacion
                System.out.println("Calculando " + operacion);
                StringTokenizer st = new StringTokenizer(operacion, "+-*/");
                String op1 = st.nextToken();
                String op2 = st.nextToken();

                Double doubleOp1;
                Double doubleOp2;

                if (op1.equals("ans"))
                    doubleOp1 = Servidor.getValorAns(nombre);
                else
                    doubleOp1 = Double.parseDouble(op1);

                if (op2.equals("ans"))
                    doubleOp2 = Servidor.getValorAns(nombre);
                else
                    doubleOp2 = Double.parseDouble(op2);

                char op = operacion.charAt(operacion.indexOf(op2, op1.length())-1);
                Double resultado = 0.0;
                switch (op) {
                    case '+':
                        resultado = doubleOp1 + doubleOp2;
                        break;
                    case '-':
                        resultado = doubleOp1 - doubleOp2;
                        break;
                    case '*':
                        resultado = doubleOp1 * doubleOp2;
                        break;
                    case '/':
                        resultado = doubleOp1 / doubleOp2;
                        break;
                }

                // Guardamos el valor del resultado
                Servidor.setValorAns(nombre, resultado);

                sOut.println(Double.toString(resultado)); // Replico datos

                texto = sIn.readLine();

            }

            sIn.close();
            sOut.close();
            s.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
