package com.sdc.secondservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecondService {

	public void modifyUser(){
		log.error("modify User");
	}
}
