package com.sdc.secondservice.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadExeucteService {

	public static void main(String[] args) {
		ThreadExtends threadExtends = new ThreadExtends();

//		log.error("Main Start");
//		threadExtends.start();
//		log.error("Main End");

		ThreadImplements ip = new ThreadImplements();
		Thread threads = new Thread(ip);
		threads.start();

	}
}
