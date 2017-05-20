import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandler class
 */
public class ClientHandler implements Runnable {

    private Socket connection;
    private PrintWriter sender;
    private BufferedReader receiver;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            sender = new PrintWriter(connection.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] A client has connected to the server");
        test();
    }

    private void test() {
        String message;
        try {
            while ((message = receiver.readLine()) != null) {
                System.out.println("Server has received: " + message);
            }
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
