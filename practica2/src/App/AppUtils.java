package App;

import java.util.Scanner;
import java.util.StringTokenizer;

import Calculator.Calculator;
import Parser.JsonParser;
import Parser.XmlParser;

public class AppUtils {

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

    /**
     * Parse a response message and create a Calculator object
     * @param message String message
     * @return Calculator object
     */
    public static Calculator parseResponse (String message) {

        StringTokenizer stContent = new StringTokenizer(message, "~");
        String msgType = stContent.nextToken();
        String result = stContent.nextToken();

        Calculator r;
        if (msgType.equalsIgnoreCase("xml"))
            r = XmlParser.parseXML(result);
        else
            r = JsonParser.parseCalculatorJSON(result);

        return r;

    }

    public static String getMessageType (String message) {

        StringTokenizer stContent = new StringTokenizer(message, "~");
        String msgType = stContent.nextToken();

        return msgType.equalsIgnoreCase("xml") ? "xml" : "json";

    }

    public static double calculateResult (Calculator calculator) {

        double operand1, operand2;
        if (calculator.getOperand1().equals("ans"))
            operand1 = Server.getValueAns(calculator.getUser());
        else
            operand1 = Double.valueOf(calculator.getOperand1());

        if (calculator.getOperand2().equals("ans"))
            operand2 = Server.getValueAns(calculator.getUser());
        else
            operand2 = Double.valueOf(calculator.getOperand2());

        double answer = 0;
        switch (calculator.getOperator()) {
            case "+":
                answer = operand1 + operand2;
                break;
            case "-":
                answer = operand1 - operand2;
                break;
            case "*":
                answer = operand1 * operand2;
                break;
            case "/":
                answer = operand1 / operand2;
                break;
        }

        return answer;

    }

}
