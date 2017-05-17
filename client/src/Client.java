import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client class
 */
public class Client {

    private static final String HOSTNAME = "localhost";     //TODO: Handle this value with a proper user setting
    private static final int PORT_NUM = 10000;      //TODO: Handle this value with a proper user setting
    private static Socket socket;
    private static PrintWriter sender;
    private static BufferedReader receiver;

    public static void main(String[] args) {
        System.out.println("[*] Launching client session...");
        try {
            socket = new Socket(HOSTNAME, PORT_NUM);
            sender = new PrintWriter(socket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] Connected successfully with the server");
        test();
    }

    private static void test() {
        String message = "Mufasa in casa";
        sender.println(message);
    }
}
