package SQA.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import SQA.model.AllTrainContent;
import SQA.model.SystemStat;
import SQA.model.Train;
import SQA.service.AdminService;
import SQA.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value={"trainContent"})
public class SystemController {
	
	private static String datePattern = "yyyy-MM-dd";
	
	@Autowired
    AdminService adminService;
	
	@Autowired
    SystemService systemService;
	
	@GetMapping("/systemreports")
	public String populateTrainInfo(Model model) {
		List<Train> trains = adminService.showTrainCapacity();
		
		model.addAttribute("trainContent", new AllTrainContent(trains));
		
		return "system";
		
	}
	
	@PostMapping(value ="/trainReservation", params = "trainPerDay")
	public String cancelTrain(Model model, @RequestParam("trainBound") String trainBound, @RequestParam("trainDate") String date) {
		String percentString = systemService.getTrainReservationPercent(trainBound, date);
		model.addAttribute("trainBound", trainBound);
		model.addAttribute("trainDate", date);
		model.addAttribute("percent", percentString);
		return "system";
	}
	
	@PostMapping(value ="/trainReservation", params = "dateRange")
	public String dateRange(@ModelAttribute("trainContent") AllTrainContent Alltrain,
							@RequestParam("startDate") String startDate,
							@RequestParam("endDate") String endDate, Model model) {
		List<Train> trains = Alltrain.getTrains();
		String[] dates = getDays(startDate, endDate);
		System.out.println(dates.length);
		String[] rates = new String[dates.length];
		Map<String,String> results = new TreeMap<>();
		for (int i = 0; i < dates.length; i++) {
			rates[i] = systemService.getDailyPercent(trains, dates[i]);
			results.put(dates[i], rates[i]);
		}
		
		model.addAttribute("results", results);
		
		return "system";
	}
	
	@PostMapping(value ="/trainReservation", params = "reserve")
	public String showStatistic(@ModelAttribute("trainContent") AllTrainContent Alltrain,
								@RequestParam("reserveDate") String reserveDate,
								Model model) {
		int numOfSearch = systemService.getTotalSearchNumber(reserveDate);
		model.addAttribute("numberOfTotalSearch", "Total Number of Search on " + reserveDate +"  is: " + numOfSearch);
		
		if (numOfSearch != 0) {
			SystemStat[] report = systemService.getRequestStat(reserveDate);
			model.addAttribute("report", report);
		}
		return "system";
	}
	
	
	
	private String[] getDays(String startDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		Date start = null;
		Date end = null;
		try {
			start = format.parse(startDate);
			end = format.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = (int)((end.getTime()-start.getTime())/(24*60*60*1000));
		String[] dates = new String[days+1];
		dates[0] = startDate;
		for (int i = 1; i < dates.length; i++) {
			start.setTime(start.getTime()+24*60*60*1000);
			dates[i] = format.format(start);
		}
		return dates;
	}

}
