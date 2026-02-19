package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TestController {

	@Autowired
	private EmailUtil emailUtil;
	
	@GetMapping("/email")
	public String hello() {
		return "email-form";
	}
	
	//이메일 전송처리
	@PostMapping("/send-email")
	public String sendEmail(@RequestParam(value="to") String to,
			@RequestParam(value="subject") String subject,
			@RequestParam(value="content") String content,
			RedirectAttributes redirectAttributes) {
		
		emailUtil.sendTextEmail(to, subject, content);	
		redirectAttributes.addFlashAttribute("success", "이메일 전송 완료");
		return "redirect:/email";
	}
	
	@PostMapping("/send-welcome-email")
	public String sendEmail(@RequestParam(value="userEmail") String userEmail,
			@RequestParam(value="userName") String userName,
			RedirectAttributes redirectAttributes) {
		
		emailUtil.sendWelcomeEmail(userEmail, userName);
		redirectAttributes.addFlashAttribute("success", "이메일 전송 완료");
		return "redirect:/email";
	}
	
}
