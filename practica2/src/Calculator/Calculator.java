package Calculator;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Calculator {

    private String user;
    private String operand1;
    private String operator;
    private String operand2;

    public Calculator (String user, String operand1, String operator, String operand2) {
        this.user = user;
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    public String getUser() {
        return user;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperator() {
        return operator;
    }

    public String getOperand2() {
        return operand2;
    }

    @Override
    public String toString() {
        return  user + ": " + operand1 + operator + operand2;
    }

    public String toXML() {
        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<calculator>\n" +
                "   <user>" + user + "</user>\n" +
                "   <operand1>" + operand1 + "</operand1>\n" +
                "   <operator>" + operator + "</operator>\n" +
                "   <operand1>" + operand2 + "</operand2>\n" +
                "</calculator>";

    }

    public String toJSON() {
        return  "{\n" +
                "   \"user\":\"" + user + "\",\n" +
                "   \"operand1\":\"" + operand1 + "\",\n" +
                "   \"operator\":\"" + operator+ "\",\n" +
                "   \"operand2\":\"" + operand2 + "\"\n" +
                "}";
    }
}
