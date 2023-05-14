package SQA.service;

import SQA.model.Booking;
import SQA.model.User;

public interface TicketService {
	
	public boolean purchase(User user, Booking booking);
	public boolean cancel(long ticketId);
	public boolean timeCheck (String date, String time, int diff); 
	
}