package Parser;

import Calculator.*;
import Parser.XmlSaxHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Jorge Gallego Madrid on 13/11/2017.
 */
public class XmlParser {

    public static Calculator parseXML(String msg) {

        // First we write the msg in a file
        PrintWriter xmlFile = null;
        try {
            xmlFile = new PrintWriter("./tmp.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter("./tmp.xml", false);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {

            SAXParser saxParser = saxParserFactory.newSAXParser();
            XmlSaxHandler handler = new XmlSaxHandler();
            saxParser.parse("./tmp.xml", handler);
            return handler.getCalculator();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
