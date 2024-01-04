package com.sdc.secondservice.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadImplements implements Runnable{
	@Override
	public void run() {
		log.error("Start thread");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		log.error("End Thread");
	}
}
