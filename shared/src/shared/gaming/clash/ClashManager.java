package shared.gaming.clash;

/**
 * Manages all potentially data-racing critical operations during clashes.
 */
public class ClashManager {

	/**
	 * Internal field used for determining whether clash is enabled.
	 */
	private boolean clashEnabled;

	/**
	 * Internal field used for synchronizing doClash requests.
	 */
	private static boolean clashRequestAccepted;

	/**
	 * Internal field used for synchronizing attack requests.
	 */
	private static boolean attackRequestAccepted;

	/**
	 * Enables clash.
	 */
	public synchronized void enableClash() {
		clashEnabled = true;
	}

	/**
	 * Disables clash.
	 */
	public synchronized void disableClash() {
		clashEnabled = false;
	}

	/**
	 * Gets current internal clash status.
	 * @return True if clash is enabled, false if not.
	 */
	public synchronized boolean isClashEnabled() {
		return clashEnabled;
	}

	/**
	 * Accepts a new doClash request in a thread-safe manner.
	 */
	public static synchronized void acceptClashRequests() {
		clashRequestAccepted = true;
	}

	/**
	 * Denies a new doClash request in a thread-safe manner.
	 */
	public static synchronized void denyClashRequests() {
		clashRequestAccepted = false;
	}

	/**
	 * Checks if a doClash request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public static synchronized boolean isClashRequestAccepted() {
		return clashRequestAccepted;
	}

	/**
	 * Accepts a new attack request in a thread-safe manner.
	 */
	public static synchronized void acceptAttackRequests() {
		attackRequestAccepted = true;
	}

	/**
	 * Denies a new attack request in a thread-safe manner.
	 */
	public static synchronized void denyAttackRequests() {
		attackRequestAccepted = false;
	}

	/**
	 * Checks if an attack request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public static synchronized boolean isAttackRequestAccepted() {
		return attackRequestAccepted;
	}
}
