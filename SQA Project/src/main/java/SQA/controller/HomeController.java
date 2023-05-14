package SQA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
	//show home page for get request on home url.
	@GetMapping("/")
	public String login(Model model) {
		return "userlogin";
	}
	
	@RequestMapping("/registration")
	public String registration(Model model) {
		return "userreg";
	}
	


}