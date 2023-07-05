package com.sdc.secondservice.service;


import com.sdc.secondservice.service.history.UserHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecondService {

	private final UserHistoryService userHistoryService;

	public void addUser(){
		log.error("add User");
		userHistoryService.addHistory();
	}


	public void modifyUser(){
		log.error("modify User");
	}
}
