package client;

import shared.communication.Receiver;
import shared.communication.Sender;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * client.ClientConnectionManager class
 */
public class ClientConnectionManager implements Runnable {

	/**
	 * Defines hostname on which the client will attempt to connect.
	 */
	private final String HOSTNAME = "localhost";

	/**
	 * Defines port number on which the client will attempt to connect.
	 */
    private final int PORT_NUMBER = 10000;

	/**
	 * Socket used by the client to establish connection with the server.
	 */
	private Socket socket;

	/**
	 * Stream used by the client to send messages.
	 */
    private PrintWriter sendStream;

	/**
	 * Stream used by the client to receive messages.
	 */
	private BufferedReader receiveStream;

	/**
	 * Initializes the internal socket and sending/receiving streams, then passes control to methods for actual
	 * connection initialization and enters in the "big loop" (listens for messages and sends responses accordingly).
	 * When the client exists from the loop, this method will initiate the shutdown routine.
	 */
    @Override
    public void run() {
        try {
            socket = new Socket(HOSTNAME, PORT_NUMBER);
            sendStream = new PrintWriter(socket.getOutputStream(), true);
            receiveStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }

        initUserConnection();

        // Send message for start the timer and create the waiting countdown
	    Client.globalThreadPool.submit(new Sender(new Message(
			    Message.Type.TIME,
			    Client.getPlayer(),
			    MessageManager.createXML(new MessageTable("header", "WAIT_START_REQUEST"))
	    ), getSendStream()));
	    Client.clientWindow.createWaitingCountdown();

        talkWithServer();
        shutdownClient();
    }

	/**
	 * Gets the sending stream in a thread-safe manner.
	 * @return Reference to the send stream.
	 */
	public synchronized PrintWriter getSendStream() {
        return sendStream;
    }

	/**
	 * Gets the receiving stream in a thread-safe manner.
	 * @return Reference to the receiving stream.
	 */
	public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

    private void invokeLoginDialog() {
	    try {
		    SwingUtilities.invokeAndWait(() -> Client.clientWindow.createLoginDialog());
	    } catch (InterruptedException | InvocationTargetException e) {
		    e.getMessage();
		    e.getCause();
		    e.printStackTrace();
	    }
	}

    private boolean checkConnectionResponse(Message message) {
		if (message == null) {
			throw new InvalidParameterException("Response message cannot be null");
		}

		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "ACCEPTED":
				// Show success message dialog
				JOptionPane.showMessageDialog(
						null,
						"Successfully registered as: " + Client.getPlayer().getName(),"Success!",
						JOptionPane.INFORMATION_MESSAGE
				);
				return true;
			case "ALREADY_CONNECTED":
				// Show error message dialog
				JOptionPane.showMessageDialog(
						null,
						"The chosen username already exist.","Failed!",
						JOptionPane.ERROR_MESSAGE
				);
				return false;
			case "MAX_NUM_REACHED":
				JOptionPane.showMessageDialog(
						null,
						"Max number of players reached. Please try again later.","Failed!",
						JOptionPane.ERROR_MESSAGE
				);
				return false;
			case "SESSION_RUNNING":
				// Show session running message dialog
				JOptionPane.showMessageDialog(
						null,
						"A play session is already running. Please try again later.","Failed!",
						JOptionPane.ERROR_MESSAGE
				);
				return false;
			default:
				throw new InvalidParameterException("Unknown message given");
		}
    }

	/**
	 * Initiate user connection with the server. An attempt is done until a username is accepted by the server.
	 * At the end of this method the client will be connected with the server with a unique username choosen by the
	 * user. If the client is the first to connect to the server, it will also cause the login timer to start.
	 */
	private void initUserConnection() {
		while (true) {
			// Generate the login dialog for username retrieving
			invokeLoginDialog();

			// Send a manual session start request message
			Client.globalThreadPool.submit(new Sender(new Message(
					Message.Type.SESSION,
					Client.getPlayer(),
					MessageManager.createXML(new MessageTable("header", "SESSION_START_REQUEST"))
			), getSendStream()));

			try {
				// Wait for a message from the server, accept only session-related messages as responses
				Message response;
				do {
					Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
					response = receive.get();
				} while (response.getType() != Message.Type.SESSION);
				// Wait for response message and pass it to the handler
				Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(response));
				// Retrieve generated message from handler, check if server has accepted the chosen username
				if (checkConnectionResponse(handle.get())) {
					break;
				}
			} catch (InterruptedException | ExecutionException e) {
				e.getMessage();
				e.getCause();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Listens for messages coming from the server synchronously. When a message comes from the server, it passes it
	 * synchronously to an handler. Then the response message is retrieved synchronously from the handler and sent
	 * asynchronously to the server. Finally, the client enters the listening state again, and the loop repeats.
	 */
	private void talkWithServer() {
        while (true) {  //TODO: implement here loop exit for correct client shutdown
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(receive.get()));
                // Retrieve generated message from handler and send it back
                Message message = handle.get();
                if (message != null) {
                    Client.globalThreadPool.submit(new Sender(message, getSendStream()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

	/**
	 * Sends a message to the server to notify that the current client will disconnect from the system, then shuts down
	 * the socket manually if auto-closing system fails.
	 */
	private void shutdownClient() {
        System.out.println("[*] Terminating current client session...");
        Message terminateCurrentSession = new Message(
                Message.Type.SESSION,
                Client.getPlayer(),
		        MessageManager.createXML(new MessageTable("header", "SESSION_STOP_REQUEST"))
        );
        try {
            Future send = Client.globalThreadPool.submit(new Sender(terminateCurrentSession, getSendStream()));
            send.get();
        } catch (InterruptedException | ExecutionException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        if (!socket.isClosed()) {   // Never trust autocloseable objects :D
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        System.out.println("[*] Session terminated correctly");
    }
}
