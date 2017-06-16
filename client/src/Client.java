/**
 * Client class
 */
public class Client {

    private static MainWindow clientWindow;
    private static Thread clientConnectionThread;

    public static void main(String[] args) {
        clientWindow = new MainWindow("Spaghetti Western");
        clientConnectionThread = new Thread(new ClientConnectionManager());
        clientConnectionThread.start();
    }
}
