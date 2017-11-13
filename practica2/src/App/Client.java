package App;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

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
            // Inicializamos el socket y los streams
            myClient = new Socket ("localhost", 9999);
            input = new DataInputStream(myClient.getInputStream());
            output = new DataOutputStream(myClient.getOutputStream());

            // Leemos el nombre de usuario
            Scanner scanner = new Scanner(System.in);
            String name, operation;
            System.out.println("Introduzca su nombre de usuario:");
            name = scanner.nextLine();
            name.trim();

            boolean fin = false;

            boolean xmlNoJson = true;

            // Se pueden introducir operaciones hasta que el cliente quiera cerrar la conexión
            while (!fin) {

                System.out.println("xml or json?");
                String answer = scanner.nextLine();
                xmlNoJson = answer.equals("xml");

                System.out.println("Introduzca la operacion o EXIT para finalizar la conexion:");
                operation = scanner.nextLine();

                // Si se introduce "exit" finalizamos
                if (operation.equals("EXIT")) {
                    output.writeBytes("EXIT\n");
                    fin = true;
                    break;
                }

                // Obtenemos la operacion y la enviamos junto con el nombre de usuario
                operation.trim();

                StringTokenizer st = new StringTokenizer(operation, "+-*/");
                String op1 = st.nextToken();
                op1 = op1.replaceAll("\\s", "");
                String op2 = st.nextToken();
                op2 = op2.replaceAll("\\s", "");
                char op = operation.charAt(operation.indexOf(op2, op1.length())-1);
                Operator operator = Operator.addition;
                switch (op) {
                    case '-':
                        operator = Operator.subtraction;
                        break;
                    case '*':
                        operator = Operator.multiplication;
                        break;
                    case '/':
                        operator = Operator.division;
                        break;
                }

                Calculator calculator = new Calculator(name, new Operand(op1), operator, new Operand(op2));

                // Preparamos para el envio
                String content;
                int contentLength;
                String envio;
                if (xmlNoJson) {
                    content = "xml" + "~" + calculator.toXML();
                    contentLength = content.length();
                    envio = Integer.toString(contentLength) + "\n" + content;
                }
                else {
                    content = "json" + "~" + calculator.toJSON();
                    contentLength = content.length();
                    envio = Integer.toString(contentLength) + "\n" + content;
                }

                // Enviamos
                output.write(envio.getBytes());

                // Imprimimos el resultado
                //System.out.println("Resultado: " + input.readLine());

            }

            input.close();
            output.close();

        } catch( IOException e ) {
            System.out.println( e );
        }
    }

}
