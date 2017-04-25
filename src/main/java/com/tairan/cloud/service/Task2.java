package com.tairan.cloud.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task2 {
	
	@Scheduled(cron="0 15 17 * * ?") 
	public void execute(){
		System.out.println("task2");
	}
}
