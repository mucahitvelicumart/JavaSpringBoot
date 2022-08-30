package com.example.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.user.UserRole;
import com.example.demo.user.UserService;

import lombok.AllArgsConstructor;


@SuppressWarnings("deprecation")
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers("/api/v1/**")
					.permitAll()
					.antMatchers("/", "/home")
					.hasAuthority(UserRole.USER.toString())
					
				.anyRequest()
				.authenticated().and()
				.formLogin()
					.loginPage("/login")
					.permitAll()
					.and()
				.logout()
					.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider ();
		provider.setPasswordEncoder(bCryptPasswordEncoder);
		provider.setUserDetailsService(userService);
		return provider;
		
	}
	
	

}
