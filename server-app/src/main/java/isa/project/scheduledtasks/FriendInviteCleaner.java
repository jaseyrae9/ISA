package isa.project.scheduledtasks;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import isa.project.model.aircompany.Flight;
import isa.project.model.users.FriendInvite;
import isa.project.service.users.FriendInvitesService;

@Component
public class FriendInviteCleaner {
	@Autowired
	private FriendInvitesService friendInvitesService;

	@Scheduled(cron = "0 0/15 * * * ?")
	public void cleanFriendInvites(){
		System.out.println("Started cleaning friend invites.");
		List<FriendInvite> pendingInvites = friendInvitesService.getAllPendingInvites();
		Date now = new Date();
		Date threeHoursLater = DateUtils.addHours(now, 3);
		Date threeDaysAgo = DateUtils.addDays(now, -3);
		
		for(FriendInvite friendInvite:pendingInvites) {
			Flight flight = friendInvite.getTicketReservation().getFlightReservation().getFlight();
			if(flight.getStartDateAndTime().before(threeHoursLater) || friendInvite.getSent().before(threeDaysAgo)) {
				friendInvitesService.refuseInvite(friendInvite);
			}
		}
	}
}
