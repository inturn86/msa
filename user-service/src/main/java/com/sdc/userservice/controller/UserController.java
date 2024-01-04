package com.sdc.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.userservice.dto.UserDto;
import com.sdc.userservice.repository.entity.UserEntity;
import com.sdc.userservice.service.UserService;
import com.sdc.userservice.vo.Greeting;
import com.sdc.userservice.vo.RequestUser;
import com.sdc.userservice.vo.ResponseUser;

@RestController
@RequestMapping("/")
public class UserController {

	private Environment evn;

	private UserService userService;

	@Autowired
	public UserController(Environment evn, UserService userService) {
		this.evn = evn;
		this.userService = userService;
	}

	@Autowired
	private Greeting greeting;

	@GetMapping("/health_check")
	@Timed(value = "users.status", longTask = true)
	public String status() {

		return String.format("Test It's Working in User Service"
				+ ", port(local.server.port)=" + evn.getProperty("local.server.port")
				+ ", port(server.port)=" + evn.getProperty("server.port")
				+ ", token secret=" + evn.getProperty("token.secret")
				+ ", token expiration=" + evn.getProperty("token.expiration_time")
		);
	}

	@GetMapping("/welcome")
	@Timed(value = "users.webcome", longTask = true)
	public String welcome() {
		return greeting.getMessage();
	}

	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = mapper.map(user, UserDto.class);
		UserDto returnUserDto = userService.createUser(userDto);

		ResponseUser res = mapper.map(returnUserDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers(){
		Iterable<UserEntity> userList = userService.getUserByAll();

		List<ResponseUser> result = new ArrayList<ResponseUser>();

		for(UserEntity user : userList) {

			result.add(new ModelMapper().map(user, ResponseUser.class));
		}

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUserByUserId(@PathVariable String userId){

		return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(userService.getUserByUserId(userId),  ResponseUser.class) );

	}
}
