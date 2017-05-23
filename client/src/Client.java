/**
 * Client class
 */
public class Client {

    private static Thread clientConnectionThread;

    public static void main(String[] args) {
        clientConnectionThread = new Thread(new ClientConnectionManager());
        clientConnectionThread.start();
    }
}
