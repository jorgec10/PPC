package App;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Calculator.*;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Client {


    public static void main( String args[] ) {

        // Read user name
        String name = AppUtils.readUserName();

        // Ask for queries while the client wants to
        while (true) {

            //------------------- Sending the query -------------------

            // Ask for the message format of the query
            boolean xmlNoJson = AppUtils.askMessageFormat();

            // Read the query from the user
            String operation = AppUtils.readOperation();

            // If the user introduce EXIT, quit the app
            if (operation.equalsIgnoreCase("EXIT")) {
                //output.writeBytes("EXIT\n");
                System.out.println("See you soon!");
                break;
            }

            // Creation of the Calculator object
            Calculator calculator = AppUtils.createCalculator(name, operation);

            // Preparing the message
            String message = AppUtils.prepareMessage(xmlNoJson, calculator);

            // Sending message to the server

            String usermail = "cliente.p4.ppc@gmail.com";
            String mailpwd = "alumnoppc";
            String servermail = "servidor.p4.ppc@gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(usermail, mailpwd);
                        }
                    });

            try {

                Message mail = new MimeMessage(session);
                mail.setFrom(new InternetAddress(usermail));
                mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse("joaquin.maria.gallego.ruiz@gmail.com"));
                mail.setSubject(name);
                mail.setText(message);

                Transport.send(mail);

                System.out.println("Mail sent");

            } catch (MessagingException e) {
                e.printStackTrace();
            }

/*
            //------------------- Receiving response -------------------

            // Read the length of the message
            int msgLength = Integer.valueOf(input.readLine());

            // Read the message
            byte[] b = new byte[msgLength];
            input.read(b, 0, msgLength);

            // Compose the string of the message
            String responseMsg = new String(b);

            // Parse response
            Calculator response = AppUtils.parseResponse(responseMsg);

            // Show the client the result
            System.out.println("Result: " + response.getResult());
*/

        }

    }

}
