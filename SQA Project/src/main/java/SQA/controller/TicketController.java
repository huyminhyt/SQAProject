package SQA.controller;

import java.util.ArrayList;

import SQA.model.Booking;
import SQA.model.Ticket;
import SQA.model.User;
import SQA.repository.TicketRepository;
import SQA.service.TicketService;
import SQA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import SQA.service.EmailService;

@Controller
public class TicketController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/purchaseConfirm")
	public String purchase(Model model, @ModelAttribute Booking booking) {
		User user = userService.findUser();
		model.addAttribute("email", user.getEmail());
		boolean purchaseRes = ticketService.purchase(user, booking);

		if (purchaseRes) {
			model.addAttribute("numOfSeats", booking.getNumOfSeats());
			model.addAttribute("passenger", booking.getPassenger());
			model.addAttribute("totalPrice", booking.getPrice() + 1);
			model.addAttribute("departureDate", booking.getDepartureDate());
			model.addAttribute("departureTrip", booking.getDepartureTrip());
			String returnDate = booking.getReturnDate();
			String isRound = (returnDate != null && !returnDate.isEmpty()) ? "Y" : "N";
			model.addAttribute("round", isRound);
			if (isRound.equals("Y")) {
				model.addAttribute("returnDate", returnDate);
				model.addAttribute("returnTrip", booking.getReturnTrip());
			}
			String msg = "Thanks for your booking! Here is the ticket details:";
			String content = emailService.bookingMailBuilder(booking, "emailTemplateBook", msg);
			emailService.sendMail(user.getEmail(),"CUSR Ticket Booking Confirmation", content);
			return "purchaseConfirm";
		} else {
			String msg = "Sorry, we could not proceed with the following booking. Please try your search again!";
			String content = emailService.bookingMailBuilder(booking, "emailTemplateBook", msg);
			emailService.sendMail(user.getEmail(),"CUSR Ticket Booking Fail", content);
			return "puchaseFail";
		}
	}

	@GetMapping("/ticketCancel")
	public String cancel(@RequestParam("id") long ticketId, Model model) {
		Ticket ticket = ticketRepository.findOne(ticketId);
		model.addAttribute("numOfSeats", ticket.getNumOfSeats());
		model.addAttribute("passenger", ticket.getPassenger());
		model.addAttribute("totalPrice", ticket.getPrice());
		model.addAttribute("departureDate", ticket.getDepartDate());
		model.addAttribute("departureTime", ticket.getDepartSegment1DepartTime());
		model.addAttribute("departureStation", ticket.getDepartStation());
		model.addAttribute("arrivalStation", ticket.getArrivalStation());
		String returnDate = ticket.getReturnDate();
		String isRound = (returnDate != null && !returnDate.isEmpty()) ? "Y" : "N";
		model.addAttribute("round", isRound);
		if (isRound.equals("Y")) {
			model.addAttribute("returnDate", returnDate);
			model.addAttribute("returnTime", ticket.getReturnSegment1DepartTime());
		}
		User user = userService.findUser();
		if (ticketService.cancel(ticketId)) {
			String msg = "The following booking has been successfully cancelled!";
			model.addAttribute("message", msg);
			String content = emailService.ticketMailBuilder(ticketId, "emailTemplateCancel", msg);
			emailService.sendMail(user.getEmail(),"CUSR Ticket Cancellation Confirmation", content);
		} else {
			String msg = "The following booking cannot be cancelled. Please cancel your ticket(s) earlier.";
			model.addAttribute("message", msg);
			String content = emailService.ticketMailBuilder(ticketId, "emailTemplateCancel", msg);
			emailService.sendMail(user.getEmail(),"CUSR Ticket Cancellation Fail", content);			
		}
		return "ticketCancel";
	}
	
	@GetMapping("/tickets")
	public String showUserTickets(Model model) {
		long userId = userService.findUser().getUserId();
		ArrayList<Ticket> qRes = ticketRepository.findTicketsByUserId(userId);
		ArrayList<Ticket> ticketList = new ArrayList<>();
		for(Ticket ticket : qRes) {
			if(ticketService.timeCheck(ticket.getDepartDate(), ticket.getDepartSegment1ArrivalTime(), 0))
				ticketList.add(ticket);
		}
		model.addAttribute("ticketList", ticketList);
		return "usertickets";
	}
}
