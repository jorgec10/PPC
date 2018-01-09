package App;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jorge Gallego Madrid on 25/10/2017.
 */
public class Server {

    // HashMap to store associations between a client and his last query
    public static Map<String, Double> valuesAns = new HashMap<>();

    /**
     * Static method to obtain the last result of a client, given his user name. On the first connection, its 0
     * @param user user name
     * @return The value stored
     */
    public static synchronized Double getValueAns(String user) {

        Double value = valuesAns.get(user);
        if (value == null) return 0.0;
        else return value;

    }

    /**
     * Method to set the last result of a client
     * @param user User name
     * @param value Value of the result
     */
    public static synchronized void setValorAns (String user, Double value) {

        valuesAns.put(user, value);

    }

    public static void main( String args[] ) {

        // Non secure socket
        ServerSocket s = null;

        // Secure socket
        ServerSocket secureSocket = null;

        // Secure socket factory
        SSLServerSocketFactory serverFactory = null;

        try {

            // Non secure service on port 9998
            s = new ServerSocket( 9998 );

            // Secure service on port 8887
            // Construct KeyManager, which contains server cert and private key
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("./demoCA/server/server.jks"), "alumno".toCharArray());
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
            serverFactory = sc.getServerSocketFactory();
            secureSocket = serverFactory.createServerSocket(8887);

        } catch( NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException | IOException e ) {
            System.out.println( e );
        }

        // Waiting for client connections
        System.out.println("Server running. Waiting for queries...");

        // Launch both threads to accept connections simultaneously
        new NonSecureServerThread(s).start();
        new SecureServerThread(secureSocket).start();

    }

}
