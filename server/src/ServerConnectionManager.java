import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * ServerConnectionManager class
 */
public class ServerConnectionManager implements Runnable {

    private final int PORT_NUMBER = 10000;
    private ServerSocket socket;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ArrayList<Thread> clientThreads = new ArrayList<>();
    private volatile boolean keepServerAlive = true;
    private static boolean sessionRunning = false;
    private static SceneryManager sessionScenery;

    @Override
    public void run() {
        try {
            socket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        acceptIncomingConnections();
    }

    public boolean isSessionRunning() {
        return sessionRunning;
    }

    public void setSessionRunning(boolean running) {
        sessionRunning = running;
    }

    private void acceptIncomingConnections() {
            Server.consolePrintLine("[*] Server is ready for connection requests");
        while (keepServerAlive) {
            try {
                clientHandlers.add(new ClientHandler(socket.accept()));
                clientThreads.add(new Thread(clientHandlers.get(clientHandlers.size() - 1)));
                clientThreads.get(clientThreads.size() - 1).start();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    public boolean sendMessageToPlayer(Player player, Message message) {
        for (ClientHandler connectedPlayer : clientHandlers) {
            if (connectedPlayer.getConnectedPlayer().getName().equals(player.getName())) {
                Future send = Server.globalThreadPool.submit(new Sender(message, connectedPlayer.getSendStream()));
                return true;
            }
        }
        return false;
    }

    public void sendMessageToTeam(Player player, Message message) {
        for (ClientHandler connectedPlayer : clientHandlers) {
            if (connectedPlayer.getConnectedPlayer().getTeam().equals(player.getTeam())) {
                // Avoid sending message also to the original sender
                if (!connectedPlayer.getConnectedPlayer().getName().equals(player.getName())) {
                    Future send = Server.globalThreadPool.submit(new Sender(message, connectedPlayer.getSendStream()));
                }
            }
        }
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler connectedClient : clientHandlers) {
            Future send = Server.globalThreadPool.submit(new Sender(message, connectedClient.getSendStream()));
        }
    }

    public void chooseScenery() {
        if (PlayerManager.getConnectedUsersNumber() <= 10) {
            sessionScenery = new SceneryManager(new DefaultScenery(
                    "shared/assets/default_scenery.jpg"
            ));  //FIXME: DefaultScenery() is temporary!
            Server.connectionManager.broadcastMessage(new Message(
                    MessageType.SCENERY,
                    new Player("SERVER", Player.Team.SERVER),
                    MessageManager.createXML(
                            new ArrayList<>(Arrays.asList(
                                    "header", "content"
                            )),
                            new ArrayList<>(Arrays.asList(
                                    "CHOOSEN_SCENERY", "DefaultScenery" //FIXME: DefaultScenery value is temporary!
                            ))
                    )
            ));
        } else if (PlayerManager.getConnectedUsersNumber() > 10 && PlayerManager.getConnectedUsersNumber() <= 20) {
            sessionScenery = new SceneryManager(new DefaultScenery(
                    "shared/assets/default_scenery.jpg"
            ));  //FIXME: DefaultScenery() is temporary!
            Server.connectionManager.broadcastMessage(new Message(
                    MessageType.SCENERY,
                    new Player("SERVER", Player.Team.SERVER),
                    MessageManager.createXML(
                            new ArrayList<>(Arrays.asList(
                                    "header", "content"
                            )),
                            new ArrayList<>(Arrays.asList(
                                    "CHOOSEN_SCENERY", "DefaultScenery" //FIXME: DefaultScenery value is temporary!
                            ))
                    )
            ));
        } else if (PlayerManager.getConnectedUsersNumber() > 20 && PlayerManager.getConnectedUsersNumber() <= 30) {
            sessionScenery = new SceneryManager(new DefaultScenery(
                    "shared/assets/default_scenery.jpg"
            ));  //FIXME: DefaultScenery() is temporary!
            Server.connectionManager.broadcastMessage(new Message(
                    MessageType.SCENERY,
                    new Player("SERVER", Player.Team.SERVER),
                    MessageManager.createXML(
                            new ArrayList<>(Arrays.asList(
                                    "header", "content"
                            )),
                            new ArrayList<>(Arrays.asList(
                                    "CHOOSEN_SCENERY", "DefaultScenery" //FIXME: DefaultScenery value is temporary!
                            ))
                    )
            ));
        }
    }

    public void shutdown() {
        keepServerAlive = false;
        executeServerShutdown();
    }

    private void executeServerShutdown() {
        Server.consolePrintLine("[!] Initiating server shutdown");
        Server.consolePrintLine("[*] Sending terminating messages to all clients...");
        for (ClientHandler handler : clientHandlers) {
            handler.terminateUserConnection();
        }
        for (Thread thread : clientThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        Server.consolePrintLine("[*] Server is shutting down...");
        try {
            socket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] Server has been shut down correctly");
    }
}
