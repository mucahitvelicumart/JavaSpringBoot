package com.example.demo.registration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UserService;


import lombok.AllArgsConstructor;



@RestController
@RequestMapping(path = "api/v1/accounts")
@AllArgsConstructor
public class RegistrationController {
	
	private final UserService userService;
	
	@Autowired
	private final RegistrationService registrationService;
	
	@PostMapping(path = "register")
	public String register(@RequestBody RegistrationRequest request) {
		return registrationService.register(request);
	}
	
	@GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
	
	@PostMapping(path = "forgot")
	public String forgot(@RequestBody EmailRequest request) {
		return userService.forgotPassword(request.getEmail());
	}
	
	@PostMapping(path = "reset-password")
	public String resetPassword(@RequestBody  PasswordResetRequest request,
								@RequestParam("token") String token) {
		
		return userService.resetPassword(request.getPassword(),request.getConfirmPassword(),token);
		
	}

}
