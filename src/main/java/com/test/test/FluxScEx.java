package com.test.test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FluxScEx {
	public static void main(String[] args) {
//		Flux.range(1, 10)
//			.publishOn(Schedulers.newSingle("pub"))
//			.log()
//			.subscribeOn(Schedulers.newSingle("sub"))
//			.subscribe(System.out::println);
		
		Flux.interval(Duration.ofMillis(200))
		    .take(10)
			.subscribe(System.out::println);
		
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
