package com.test.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntervalEx {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				int no = 0 ;
				boolean cancelled = false;
				
				@Override
				public void request(long n) {
					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.scheduleAtFixedRate( () -> {
						if(cancelled) {
							exec.shutdown();
							return;
						}
						sub.onNext(no++);
					}, 0, 500, TimeUnit.MILLISECONDS);
				}
				
				@Override
				public void cancel() {
					cancelled = true;
					
				}
			});
		};
		
		Publisher<Integer> takepub = sub -> {
			pub.subscribe(new Subscriber<Integer>() {
				int count = 0;
				Subscription subsc;
				@Override
				public void onSubscribe(Subscription s) {
					// TODO Auto-generated method stub
					log.info("onSubscribe");
					subsc = s;
					sub.onSubscribe(s);
				}

				@Override
				public void onNext(Integer t) {
					log.info("onNext: {}", t);
					if(++count > 4) {
						subsc.cancel();
					}
					
				}

				@Override
				public void onError(Throwable t) {
					log.info("onError: {}", t);
					
				}

				@Override
				public void onComplete() {
					log.info("onComplete");
				}
			});
		};
		
		
		takepub.subscribe(new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				// TODO Auto-generated method stub
				log.info("onSubscribe");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer t) {
				// TODO Auto-generated method stub
				log.info("onNext: {}", t);
				
			}

			@Override
			public void onError(Throwable t) {
				log.info("onError: {}", t);
				
			}

			@Override
			public void onComplete() {
				log.info("onComplete");
			}
		});
		
		
		
		
		
		
	}
}
