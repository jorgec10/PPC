package Calculator;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Operand {

    private String operand;

    public Operand(String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    @Override
    public String toString() {
        return operand;
    }
}
