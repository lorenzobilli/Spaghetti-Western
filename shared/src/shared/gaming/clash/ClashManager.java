/*
 *  Project: "Spaghetti Western"
 *
 *
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017-2018 Lorenzo Billi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *	documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *	permit persons to whom the Software is	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *	the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *	WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *	OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *	OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
	 * Internal field used for determining whether clash is running.
	 */
	private boolean clashRunning;

	/**
	 * Internal field used for synchronizing clash requests.
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
	 * Signals the start of a clash.
	 */
	public synchronized void signalClashStart() {
		clashRunning = true;
	}

	/**
	 * Signals the end of a clash.
	 */
	public synchronized void signalClashEnding() {
		clashRunning = false;
	}

	/**
	 * Checks if a clash is running.
	 * @return True if a clash is running, false if not.
	 */
	public synchronized boolean isClashRunning() {
		return clashRunning;
	}

	/**
	 * Gets current internal clash status.
	 * @return True if clash is enabled, false if not.
	 */
	public synchronized boolean isClashEnabled() {
		return clashEnabled;
	}

	/**
	 * Accepts a new clash request in a thread-safe manner.
	 */
	public synchronized void acceptClashRequests() {
		clashRequestAccepted = true;
	}

	/**
	 * Denies a new clash request in a thread-safe manner.
	 */
	public synchronized void denyClashRequests() {
		clashRequestAccepted = false;
	}

	/**
	 * Checks if a clash request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public synchronized boolean isClashRequestAccepted() {
		return clashRequestAccepted;
	}

	/**
	 * Accepts a new attack request in a thread-safe manner.
	 */
	public synchronized void acceptAttackRequests() {
		attackRequestAccepted = true;
	}

	/**
	 * Denies a new attack request in a thread-safe manner.
	 */
	public synchronized void denyAttackRequests() {
		attackRequestAccepted = false;
	}

	/**
	 * Checks if an attack request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public synchronized boolean isAttackRequestAccepted() {
		return attackRequestAccepted;
	}
}
