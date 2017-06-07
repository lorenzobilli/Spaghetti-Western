/**
 * Server class
 */
public class Server {

    private static MainWindow serverWindow;
    private static Thread serverConnectionThread;

    public static void main(String[] args) {
        serverWindow = new MainWindow("Spaghetti Western server");
        serverConnectionThread = new Thread(new ServerConnectionManager());
        serverConnectionThread.start();
    }

    public static void consolePrint(String message) {
        serverWindow.appendText(message);
    }

    public static void consolePrintLine(String message) {
        serverWindow.appendText(message + "\n");
    }
}
