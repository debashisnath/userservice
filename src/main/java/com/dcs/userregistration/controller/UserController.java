/**
 * 
 */
package com.dcs.userregistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.model.User;
import com.dcs.userregistration.service.UserService;

/**
 * @author Debashis
 *
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/api/user/register")
	public ResponseEntity<?> saveUserProfile(@RequestBody User user) throws ProfileAlreadyExistsException{
		
		try {
			String message = userService.saveUser(user);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		}
		catch(ProfileAlreadyExistsException e){
			return new ResponseEntity<String>("Profile Already Exists", HttpStatus.CONFLICT);
		}
	}
	
}
