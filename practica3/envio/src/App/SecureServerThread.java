package App;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SecureServerThread extends Thread {

    // Server socket
    ServerSocket s;

    // Client socket
    Socket client = null;

    public SecureServerThread(ServerSocket s) {
        this.s = s;
    }

    public void run() {

        while (true) {
            try {
                client = s.accept();
                System.out.println("Secure connection accepted: " + client.getInetAddress() + ":" + client.getPort());
                new PetitionManagerThread(client).start();
            } catch( IOException e ) {
                System.out.println( e );
            }
        }

    }
}
