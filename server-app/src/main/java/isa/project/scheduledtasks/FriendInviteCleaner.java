package isa.project.scheduledtasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FriendInviteCleaner {

	@Scheduled(cron = "* 1/15 * * * ?")
	public void cleanFriendInvites() {
		System.out.println("Cleaning friend invites.");
	}
}
