import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class
 */
public class Server {

    private static final int PORT_NUM = 10000;      //TODO: Handle this value with a proper user setting
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter sender;
    private static BufferedReader receiver;

    public static void main(String[] args) {
        System.out.println("[*] Server is starting up...");
        System.out.println("[*] Server is waiting for a client to connect...");
        try {
            serverSocket = new ServerSocket(PORT_NUM);
            clientSocket = serverSocket.accept();
            sender = new PrintWriter(clientSocket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] A client has connected to the server");
        test();
        shutdownServer();
    }

    private static void shutdownServer() {
        System.out.println("[*] Server is shutting down...");
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] Server has been shut down correctly");
    }

    private static void test() {
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
