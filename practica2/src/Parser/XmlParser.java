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

    private static int fileId = 0;

    /**
     * Parse a message string in XML to a Calculator object
     * @param msg XML string
     * @return Calculator object
     */
    public static Calculator parseXML(String msg) {

        // First we write the msg in a file
        PrintWriter xmlFile = null;
        String fileName = "./files/" + Integer.toString(fileId) + ".xml";

        try {
            xmlFile = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, false);
            writer.write(msg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse the string with SAX
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {

            SAXParser saxParser = saxParserFactory.newSAXParser();
            XmlSaxHandler handler = new XmlSaxHandler();
            saxParser.parse(fileName, handler);
            fileId++;
            return handler.getCalculator();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
