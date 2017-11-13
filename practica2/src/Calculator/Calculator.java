package Calculator;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Calculator {

    private String user;
    private Operand operand1;
    private Operator operator;
    private Operand operand2;

    public Calculator (String user, Operand op1, Operator operator, Operand op2) {
        this.user = user;
        this.operand1 = op1;
        this.operator = operator;
        this.operand2 = op2;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Operand getOperand1() {
        return operand1;
    }

    public void setOperand1(Operand operand1) {
        this.operand1 = operand1;
    }

    public Operand getOperand2() {
        return operand2;
    }

    public void setOperand2(Operand operand2) {
        this.operand2 = operand2;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return  user + " - " + operand1.toString() + " - " + operand2.toString() + " - " + operator.name();
    }

    public String toXML() {
        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<calculator>\n" +
                "   <user>" + user + "</user>\n" +
                "   <operand1>" + operand1.toString() + "</operand1>\n" +
                "   <operator>" + operator.name() + "</operator>\n" +
                "   <operand1>" + operand2.toString() + "</operand2>\n" +
                "</calculator>";

    }

    public String toJSON() {
        return  "{\n" +
                "   \"user\":\"" + user + "\",\n" +
                "   \"operand1\":\"" + operand1.toString() + "\",\n" +
                "   \"operator\":\"" + operator.name() + "\",\n" +
                "   \"operand2\":\"" + operand2.toString() + "\"\n" +
                "}";
    }
}
