import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * Map class
 */
public abstract class Map {

	protected void sendPlayerMove(String destination) {
		Future send = Client.globalThreadPool.submit(new Sender(new Message(
				MessageType.SCENERY,
				Client.getPlayer(),
				MessageManager.createXML(
						new ArrayList<>(Arrays.asList(
								"header", "content"
						)),
						new ArrayList<>(Arrays.asList(
								"TRY_PLAYER_MOVE", destination
						))
				)
		), Client.connectionManager.getSendStream()));
	}

	protected abstract void populate(MapWindow map);

}
