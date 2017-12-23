package App;

import java.util.Scanner;
import java.util.StringTokenizer;

import Calculator.Calculator;

public class Utils {

    static Scanner scanner = new Scanner(System.in);

    /**
     * Read and return an username from console
     */
    public static String readUserName() {

        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        return name.trim();

    }

    /**
     * Ask for the message format.
     * True if XML, false if JSON
     */
    public static boolean askMessageFormat () {

        System.out.println("For the next query, XML or JSON");
        String answer = scanner.nextLine();
        return answer.equalsIgnoreCase("xml");

    }

    /**
     * Ask for the query and read it from console
     */
    public static String readOperation () {

        System.out.println("Enter the query or EXIT to end the connection:");
        String operation = scanner.nextLine();
        return operation.trim();

    }

    /**
     * Create a Calculator object
     * @param name User name
     * @param operation Operation of the Calculator object, format: operand1operatoroperand2 (1+1, 2*3)
     * @return Calculator object
     */
    public static Calculator createCalculator (String name, String operation) {

        StringTokenizer st = new StringTokenizer(operation, "+-*/");
        String op1 = st.nextToken();
        op1 = op1.replaceAll("\\s", "");
        String op2 = st.nextToken();
        op2 = op2.replaceAll("\\s", "");
        String op = "" + operation.charAt(operation.indexOf(op2, op1.length())-1);

        return new Calculator(name, op1, op, op2, null);

    }

    /**
     * Prepare the message to send it to the server
     * @param xmlNoJson Message type
     * @param calculator Calculator object to be sent
     * @return Message in string
     */
    public static String prepareMessage (boolean xmlNoJson, Calculator calculator) {

        String content = xmlNoJson ? ("xml" + "~" + calculator.toXML()) : ("json" + "~" + calculator.toJSON());
        return Integer.toString(content.length()) + "\n" + content;

    }

}
