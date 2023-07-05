package com.sdc.secondservice.service.history;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;

@Slf4j
@Service
public class UserHistoryService {

	public String getDetail() {
		return "user";
	}
	public void addHistory() {
		log.error("insert add History");
	}
}
