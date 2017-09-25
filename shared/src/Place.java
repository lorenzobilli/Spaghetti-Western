import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Place class
 */
public class Place {

    private final int MAX_GOOD_PLAYERS = 3;
    private final int MAX_BAD_PLAYERS = 3;

    private String placeName;
    private List<Player> goodPlayers;
    private List<Player> badPlayers;
    private boolean clashEnabled = false;

    public Place(String placeName) {
        if (placeName == null) {
            throw new InvalidParameterException("Place name cannot be null");
        }
        if (placeName.equals("")) {
            throw new InvalidParameterException("Place name cannot be empty");
        }
        this.placeName = placeName;
        goodPlayers = new ArrayList<>();
        badPlayers = new ArrayList<>();
    }

    public String getPlaceName() {
        return placeName;
    }

    public boolean addPlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Player to be added cannot be null");
        }

        boolean playerAdded = false;
        switch (player.getTeam()) {
            case GOOD:
                if (goodPlayers.size() < MAX_GOOD_PLAYERS) {
                    goodPlayers.add(player);
                    checkClash();
                    playerAdded = true;
                }
                break;
            case BAD:
                if (badPlayers.size() < MAX_BAD_PLAYERS) {
                    badPlayers.add(player);
                    checkClash();
                    playerAdded = true;
                }
                break;
        }
        return playerAdded;
    }

    public boolean removePlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Player to be removed cannot be null");
        }

        boolean playerRemoved = false;
        switch (player.getTeam()) {
            case GOOD:
                if (goodPlayers.remove(player)) {
                    checkClash();
                    playerRemoved = true;
                }
                break;
            case BAD:
                if (badPlayers.remove(player)) {
                    checkClash();
                    playerRemoved = true;
                }
                break;
        }
        return playerRemoved;
    }

    public boolean isPlayerPresent(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Player to be checked cannot be null");
        }

        boolean playerFound = false;
        switch (player.getTeam()) {
            case GOOD:
                if (goodPlayers.contains(player)) {
                    playerFound = true;
                }
                break;
            case BAD:
                if (badPlayers.contains(player)) {
                    playerFound = true;
                }
                break;
        }
        return playerFound;
    }

    public boolean getClashStatus() {
        return clashEnabled;
    }

    private void checkClash() {
        clashEnabled = goodPlayers.size() > 0 && badPlayers.size() > 0;
    }
}
