package SQA.controller;

import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import SQA.model.Request;
import SQA.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MeasurementInterceptor implements HandlerInterceptor {
	
	private static String datePattern = "yyyy-MM-dd";
	
	@Autowired
    RequestRepository requestRepo;
	
	
	@Override
	public boolean preHandle (HttpServletRequest request, 
					HttpServletResponse response, Object handler) throws Exception {
		long startTime =  new Date().getTime();
		request.setAttribute("startTime", startTime);
		return true;
	}
	
	@Override
	public void postHandle (HttpServletRequest request, HttpServletResponse response, 
						Object handler, ModelAndView modelAndView) throws Exception {
		long startTime = (Long)request.getAttribute("startTime");
		request.removeAttribute("startTime");
		
		long endTime = new Date().getTime();
		long timeCost = endTime - startTime;
		
		String numberOfConnections = (String)request.getAttribute("numberOfConnections");
		addRequestInDB(numberOfConnections, timeCost);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
						Object handler, Exception ex) throws Exception {
	}

	private void addRequestInDB(String numberOfConnections, long timeCost){
		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		Date current = new Date();
		String currentDate = format.format(current);
		requestRepo.save(new Request(numberOfConnections, currentDate, timeCost));
	}
	

}
