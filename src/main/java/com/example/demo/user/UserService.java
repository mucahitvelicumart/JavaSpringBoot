package com.example.demo.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.email.EmailSender;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenRepository;
import com.example.demo.registration.token.ConfirmationTokenService;
import com.example.demo.registration.token.TokenType;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
	private final static String USER_NOT_FOUND_MSG = "user with eMail %s not found";
	
	private final static String RESET_SUBJECT = "RESET YOUR PASSWORD";
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final EmailSender emailSender;

	@Override
	public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {

		return userRepository.findByEmail(eMail)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, eMail)));
	}

	public String signUpUser(User user) {
		boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
		if (userExists) {
			throw new IllegalStateException("email already taken");
		}

		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		userRepository.save(user);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15), user, TokenType.REGISTER);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		return token;

	}

	public String forgotPassword(String email) {
		boolean userExists = userRepository.findByEmail(email).isPresent();
		if (!userExists) {
			throw new IllegalStateException("There is no user with this Email");
		}
		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(5), userRepository.findByEmail(email).get(), TokenType.RESET);
		
		String link = "http://localhost:9090/api/v1/changepassword?token=" + token;
		emailSender.send(email,buildEmail(" ",link),RESET_SUBJECT);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		return token;
	}
	public String resetPassword(String password,String confirmPassword,String token) {
		
		User user = confirmationTokenRepository.findByToken(token).get().getUser();
		if(user == null) {
			return "Token is not found";
		}
		else if (confirmationTokenRepository.findByToken(token).get().getTokenType()!=TokenType.RESET) {
			return "Token is not RESET token";
		}
		else if (!(password.equals(confirmPassword))) {
			return "Passwords do not match";
		}
		else {
			String encodedPassword = bCryptPasswordEncoder.encode(password);
			userRepository.changePassword(user.getEmail(), encodedPassword);
		}

		return token;
		
	}
	

	public int enableUser(String email) {
		return userRepository.enableUser(email);
	}
	private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">  Please click on the below link to reset your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Reset Password</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
