/**
 * Server class
 */
public class Server {

    private static Thread serverConnectionThread;

    public static void main(String[] args) {
        System.out.println("[*] Server is starting up...");
        MainWindow mainWindow = new MainWindow("Spaghetti Western server");
        serverConnectionThread = new Thread(new ServerConnectionManager());
        serverConnectionThread.start();
    }
}
