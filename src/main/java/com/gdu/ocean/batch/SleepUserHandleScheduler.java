package com.gdu.ocean.batch;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.ocean.service.UsersService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableScheduling
@Component
public class SleepUserHandleScheduler {

	private final UsersService usersService;
	
	// 매일 저녁 9시
	@Scheduled(cron="0 0 21 1/1 * ?")
	public void execute() {
		usersService.sleepUsersHandle();
	}
}
