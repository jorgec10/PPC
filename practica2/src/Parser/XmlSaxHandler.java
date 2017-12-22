package Parser;

import Calculator.Calculator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlSaxHandler extends DefaultHandler{

    private Calculator calc = null;

    boolean bUser = false;
    boolean bOp1 = false;
    boolean bOp = false;
    boolean bOp2 = false;

    @Override
    public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("calculator"))
            calc = new Calculator();
        if (qName.equalsIgnoreCase("user"))
            bUser = true;
        if (qName.equalsIgnoreCase("operand1"))
            bOp1 = true;
        if (qName.equalsIgnoreCase("operator"))
            bOp = true;
        if (qName.equalsIgnoreCase("operand2"))
            bOp2 = true;
    }

    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException {

    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException {

        if (bUser) {
            String user = new String(ch, start, length);
            calc.setUser(user);
            bUser = false;
        }
        if (bOp1) {
            String operand1 = new String(ch, start, length);
            calc.setOperand1(operand1);
            bOp1 = false;
        }
        if (bOp) {
            String operator = new String(ch, start, length);
            calc.setOperator(operator);
            bOp = false;
        }
        if (bOp2) {
            String operand2 = new String(ch, start, length);
            calc.setOperand2(operand2);
            bOp2 = false;
        }

    }

    public Calculator getCalculator() {
        return calc;
    }
}
