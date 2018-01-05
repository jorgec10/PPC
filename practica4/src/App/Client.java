package App;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import Calculator.*;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Client {


    public static void main( String args[] ) {

        // Mail credentials
        String usermail = "cliente.p4.ppc@gmail.com";
        String servermail = "servidor.p4.ppc@gmail.com";
        String mailpwd = "alumnoppc";

        // SMPT session to send mails
        Properties smtpprops = new Properties();
        smtpprops.put("mail.smtp.auth", "true");
        smtpprops.put("mail.smtp.starttls.enable", "true");
        smtpprops.put("mail.smtp.host", "smtp.gmail.com");
        smtpprops.put("mail.smtp.port", "587");
        Session session = Session.getInstance(smtpprops,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usermail, mailpwd);
                    }
                });

        // IMAP session to retrieve mails
        Properties imapprops = new Properties();
        try {
            imapprops.load(new FileInputStream(new File("./imap.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session imapSession = Session.getDefaultInstance(imapprops, null);


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
            try {

                // Compose query mail
                Message mail = new MimeMessage(session);
                mail.setFrom(new InternetAddress(usermail));
                mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(servermail));

                // Set subject
                if (xmlNoJson)
                    mail.setSubject("xml");
                else
                    mail.setSubject("json");

                // Set body
                mail.setText(message);

                // Send mail
                Transport.send(mail);

                //------------------- Receiving response -------------------


                // Wait response
                while (true) {

                    Store store = imapSession.getStore("imaps");
                    store.connect("smtp.gmail.com", usermail, mailpwd);
                    Folder inbox = store.getFolder("inbox");
                    inbox.open(Folder.READ_WRITE);

                    // Open inbox with write perms
                    if (inbox.getUnreadMessageCount() > 0) {

                        // Get unread messages
                        Message [] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                        // Get responses of the queries
                        for (Message m : messages) {

                            // Get the body
                            String content = (String) m.getContent();

                            // Get msg type
                            String msgType = m.getSubject();

                            // Parse body
                            Calculator response = AppUtils.parseResponse(content, msgType);

                            // Show result
                            System.out.println("Result: " + response.getOperand1() +
                                    response.getOperator() + response.getOperand2() +
                                    "= " + response.getResult());

                        }

                        // Close connection
                        inbox.close(true);
                        store.close();
                        break;
                    }

                    // Close connection
                    inbox.close(true);
                    store.close();


                }


            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }


        }

    }

}
