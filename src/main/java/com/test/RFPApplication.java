package com.test;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

//import java.util.concurrent.Flow.Publisher;
//import java.util.concurrent.Flow.Subscriber;

import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RFPApplication {
	
	@RestController
	public static class controller {
		
		@GetMapping("/hello")
		public Publisher<String> hello(@RequestParam("name") String name) {
			return new Publisher<String>() {
				
				@Override
				public void subscribe(Subscriber<? super String> s) {
					s.onSubscribe(new Subscription() {
						
						@Override
						public void request(long n) {
							s.onNext("hello" + name);
							s.onComplete();
						}
						
						@Override
						public void cancel() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			};
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(RFPApplication.class, args);
	}

}
