package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.valves.rewrite.InternalRewriteMap.Escape;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulerEx {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				
				@Override
				public void request(long n) {
					log.info("request");
					sub.onNext(1);
					sub.onNext(2);
					sub.onNext(3);
					sub.onNext(4);
					sub.onNext(5);
					sub.onComplete();
				}
				
				@Override
				public void cancel() {
					
				}
			});
		};
		
		
//		Publisher<Integer> subOnPub = sub -> {
//			// 1번에 1개의 쓰레드만 동작 보장
//			ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
//				public String getThreadNamePrefix() { return "subOn-";}
//			});
//			es.execute(() -> pub.subscribe(sub));
//		};
		
		
		// Subscriber 가 컨슘하는데 느린경우에
		Publisher<Integer> pubOnPub = sub -> {
			pub.subscribe(new Subscriber<Integer>() {
				
				ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
					public String getThreadNamePrefix() { return "pubOn-";}
				});

				@Override
				public void onSubscribe(Subscription s) {
					// TODO Auto-generated method stub
					sub.onSubscribe(s);
				}

				@Override
				public void onNext(Integer i) {
					es.execute(()-> sub.onNext(i) );
				}

				@Override
				public void onError(Throwable t) {
					es.execute(()-> sub.onError(t) );
					es.shutdown();
				}

				@Override
				public void onComplete() {
					es.execute(()-> sub.onComplete());
					es.shutdown();
				}
				
			});
		};
		
		pubOnPub.subscribe(new Subscriber<Integer>() {
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
		
		log.info("exit");
		
	}
}
