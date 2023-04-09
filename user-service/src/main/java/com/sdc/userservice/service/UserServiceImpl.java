package com.sdc.userservice.service;

import com.sdc.userservice.dto.UserDto;
import com.sdc.userservice.repository.UserRepository;
import com.sdc.userservice.repository.entity.UserEntity;
import com.sdc.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder encoder;

	private final Environment env;

	private final RestTemplate restTemplate;

	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());

		ModelMapper mapper = new ModelMapper();

		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));

		userRepository.save(userEntity);

		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

		return returnUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		// TODO Auto-generated method stub

		UserEntity user = userRepository.findByUserId(userId);

		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}

		UserDto userDto = new ModelMapper().map(user, UserDto.class);

		String orderUrl = String.format(env.getProperty("order_service.url"), userId);
		/* Using as rest template */
		ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
		});

		List<ResponseOrder> ordersList = orderListResponse.getBody();
		userDto.setOrders(ordersList);

		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if(userEntity == null) {
			throw new UsernameNotFoundException(username);
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		return new ModelMapper().map(userRepository.findByEmail(email), UserDto.class);
	}

}
