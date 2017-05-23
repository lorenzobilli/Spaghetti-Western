/**
 * Server class
 */
public class Server {

    private static Thread serverConnectionThread;

    public static void main(String[] args) {
        System.out.println("[*] Server is starting up...");
        serverConnectionThread = new Thread(new ServerConnectionManager());
        serverConnectionThread.start();
    }
}
