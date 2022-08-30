package com.example.demo.registration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PasswordResetRequest {
	
		private final String password;
		private final String confirmPassword;
		

	
}
