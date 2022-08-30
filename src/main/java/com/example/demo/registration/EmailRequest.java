package com.example.demo.registration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class EmailRequest {
	private  String email;
	public EmailRequest(String email){
		this.email = email;
	}

}


