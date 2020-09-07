package com.example.oauth.controller;

import com.example.oauth.pojo.User;
import com.example.oauth.repository.UserRepository;
import com.example.oauth.util.UserHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@FrameworkEndpoint
public class AuthController  {

	@Autowired
	UserRepository mUserRepository;

	@Autowired
	PasswordEncoder mPasswordEncoder;

	@Autowired
	private DefaultTokenServices defaultTokenServices;

	@RequestMapping(method = RequestMethod.DELETE, value = "/logout")
	@ResponseBody
	public String revokeToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.contains("Bearer")){
			String tokenId = authorization.substring("Bearer".length()+1);

			defaultTokenServices.revokeToken(tokenId);
		}
		return "success";
	}

	@JsonIgnore
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String createUser(@RequestParam String email,
										@RequestParam String name,
										@RequestParam String password) {

		User tmpUser = mUserRepository.findByEmail(email);

		if (tmpUser != null) {
			return "Email already exist";
		}

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(mPasswordEncoder.encode(password));
		user.setRole(UserHelper.ROLE_USER);
		mUserRepository.save(user);

		return "User created";
	}
}