package App;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

import Calculator.*;

import javax.net.ssl.*;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Client {


    public static void main( String args[] ) {

        Socket myClient = null;
        SSLSocket secureSocket = null;
        DataInputStream input = null;
        DataOutputStream output = null;

        try {

            // Secure or non secure connection?
            boolean secure = AppUtils.askForSecureConnection();

            // Start socket and streams
            if (secure) {

                // Construct KeyManager, which contains client cert and private key
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream("./demoCA/client/client1.jks"), "alumno".toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, "alumno".toCharArray());
                KeyManager[] keyManagers = kmf.getKeyManagers();

                // Construct trustManager, which contains CA cert
                KeyStore trustedStore = KeyStore.getInstance("JKS");
                trustedStore.load(new FileInputStream("./demoCA/cakeystore.jks"), "alumno".toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustedStore);
                TrustManager[] trustManagers = tmf.getTrustManagers();

                // Create SSL Context
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(keyManagers, trustManagers, null);

                // Create secure socket on port 8887
                SSLSocketFactory ssf = sc.getSocketFactory();
                secureSocket = (SSLSocket) ssf.createSocket("localhost", 8887);

                // Start SSL negotiation between server and client
                secureSocket.startHandshake();

                // Initialize streams
                input = new DataInputStream(secureSocket.getInputStream());
                output = new DataOutputStream(secureSocket.getOutputStream());

            }
            else {

                // Create non secure socket on port 9998
                myClient = new Socket ("localhost", 9998);

                // Initialize streams
                input = new DataInputStream(myClient.getInputStream());
                output = new DataOutputStream(myClient.getOutputStream());

            }


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
                    output.writeBytes("EXIT\n");
                    System.out.println("See you soon!");
                    break;
                }

                // Creation of the Calculator object
                Calculator calculator = AppUtils.createCalculator(name, operation);

                // Preparing the message
                String message = AppUtils.prepareMessage(xmlNoJson, calculator);

                // Sending message to the server
                output.write(message.getBytes());

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

            }

            input.close();
            output.close();

        } catch( NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException | IOException e ) {
            System.out.println( e );
        }
    }

}
