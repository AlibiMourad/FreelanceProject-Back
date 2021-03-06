package com.freelance.app.controllers;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freelance.app.dto.UserDetailDto;
import com.freelance.app.dto.UserDto;
import com.freelance.app.dto.UserLoginDto;
import com.freelance.app.entities.User;
import com.freelance.app.security.JwtProvider;
import com.freelance.app.security.JwtResponse;
import com.freelance.app.security.ResponseMessage;
import com.freelance.app.services.IUserService;
import com.freelance.app.util.Routes;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(Routes.USER)
@CrossOrigin(value = "*")
public class UserController {

	private IUserService userService;
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;


	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws MessagingException {
		userService.createUser(userDto);
		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		response.setHeader("Authorization", jwt);
		return ResponseEntity
				.ok(new JwtResponse(jwt, "Bearer", userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
		userService.updatePasswordUser(userDto);
		return new ResponseEntity<>(new ResponseMessage("User password updated successfully!"), HttpStatus.OK);
	}
	
	@GetMapping("/details/{email}")
	public UserDetailDto getUserInfoByEmail(@PathVariable(value = "email") String email) {
		return userService.getUserInfoByEmail(email);
	}
	
	@GetMapping("/{companyId}")
	public List<User> getUsersByCompany(@PathVariable(value = "companyId") Long companyId) {
		return userService.getUsersByCompanyClient(companyId);
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable(value = "userId") Long userId) {
		userService.deleteUser(userId);
	}
	
}
