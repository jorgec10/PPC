package App;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.StringTokenizer;

import Calculator.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class PetitionManagerThread extends Thread{


    public PetitionManagerThread () {}

    public void run() {

        // Mail credentials
        String servermail = "servidor.p4.ppc@gmail.com";
        String clientmail = "cliente.p4.ppc@gmail.com";
        String mailpwd = "alumnoppc";

        try {

            // IMAP session to retrieve mails
            Properties imapprops = new Properties();
            imapprops.load(new FileInputStream(new File("./imap.properties")));
            Session imapSession = Session.getDefaultInstance(imapprops, null);

            // SMTP session to respond client
            Properties smtpprops = new Properties();
            smtpprops.put("mail.smtp.auth", "true");
            smtpprops.put("mail.smtp.starttls.enable", "true");
            smtpprops.put("mail.smtp.host", "smtp.gmail.com");
            smtpprops.put("mail.smtp.port", "587");
            Session session = Session.getInstance(smtpprops,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(clientmail, mailpwd);
                        }
                    });

            // Check new mails every 5 secs
            while (true) {

                // Connect to the store via IMAP
                Store store = imapSession.getStore("imaps");
                store.connect("smtp.gmail.com", servermail, mailpwd);
                Folder inbox = store.getFolder("inbox");

                // Open inbox with write perms
                inbox.open(Folder.READ_WRITE);

                // Check unread messages
                if (inbox.getUnreadMessageCount() > 0) {

                    // Get unread messages
                    Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                    // Response each unread message
                    for (Message message : messages) {

                        // Get the content of the mail
                        String content = (String) message.getContent();

                        // Get message type and parse the mail
                        String msgType = message.getSubject();
                        Calculator calculator = AppUtils.parseResponse(content, msgType);

                        // Print calculator received
                        System.out.println(calculator);

                        // Calculate result of the query
                        double result = AppUtils.calculateResult(calculator);

                        // Store the result
                        Server.setValorAns(calculator.getUser(), result);

                        // Preparing response
                        calculator.setResult(String.valueOf(result));
                        String body = AppUtils.prepareMessage(msgType.equalsIgnoreCase("xml"), calculator);

                        // Compose response mail
                        Message mail = new MimeMessage(session);
                        mail.setFrom(new InternetAddress(servermail));
                        mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(clientmail));

                        // Set subject
                        if (msgType.equalsIgnoreCase("xml"))
                            mail.setSubject("xml");
                        else
                            mail.setSubject("json");

                        // Set body
                        mail.setText(body);

                        // Sent mail
                        Transport.send(mail);

                    }
                }

                // Close connection
                inbox.close(true);
                store.close();

                // Wait 5 secs
                Thread.sleep(5000);

            }


        } catch (IOException | MessagingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
