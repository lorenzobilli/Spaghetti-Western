package server.scheduler;

import shared.gaming.Player;

public interface Scheduler {
	public void enableScheduler();
	public void disableScheduler();
	public boolean isSchedulerEnabled();
	public void addScheduledElement(Player scheduledPlayer);
	public void removeScheduledElement(Player scheduledPlayer);
	public void scheduleNext();
}
