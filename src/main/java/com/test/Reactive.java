package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class Reactive {


	public static void main(String[] args) throws IOException {
		
		Publisher<Integer> pub = iterPub(Stream.iterate(1, a->a+1).limit(29).collect(Collectors.toList()));
//		Publisher<Integer> mapPub = mapPub(pub, s -> s*10);
		
		Publisher<String> mapPub = mapPub(pub, s -> "["+ s + "]");
//		Publisher<Integer> sumPub = sumPub(pub);
//		Publisher<Integer> reducePub = reducePub(pub, 0, (a, b)-> a+b);
		
		Publisher<String> reducePub = reducePub(pub, "", (a, b)-> a+ "-" +b);
		mapPub.subscribe(logSub());
		pub.subscribe(logSub());
//		sumPub.subscribe(logSub());
		reducePub.subscribe(logSub());
		
		
	}
	
	private static <T, R> Publisher<R> reducePub(Publisher<T> pub, R init
	, BiFunction<R, T, R> bf){
		return new Publisher<R>() {
			@Override
			public void subscribe(Subscriber<? super R> sub) {
				pub.subscribe(new DelegateSub<T, R>(sub) {
					R result = init;
					@Override
					public void onNext(T i) {
						result = bf.apply(result, i);
					}
					
					@Override
					public void onComplete() {
						sub.onNext(result);
						sub.onComplete();
					}
				});
			}
		};
	}
	
	
	
//	private static Publisher<Integer> reducePub(Publisher<Integer> pub, int init
//			, BiFunction<Integer, Integer, Integer> bf){
//		return new Publisher<Integer>() {
//			
//			@Override
//			public void subscribe(Subscriber<? super Integer> subscriber) {
//				pub.subscribe(new DelegateSub(subscriber) {
//					int result = init;
//					@Override
//					public void onNext(Integer i) {
//						result = bf.apply(result, i);
//					}
//					
//					@Override
//					public void onComplete() {
//						sub.onNext(result);
//						sub.onComplete();
//					}
//				});
//				
//			}
//		};
//	}
	
	
//	private static Publisher<Integer> sumPub(Publisher<Integer> pub){
//		return new Publisher<Integer>() {
//			
//			@Override
//			public void subscribe(Subscriber<? super Integer> sub) {
//				pub.subscribe(new DelegateSub(sub) {
//					int sum =0;
//					@Override
//					public void onNext(Integer i) {
//						// TODO Auto-generated method stub
//						sum += i;
//					}
//					
//					@Override
//					public void onComplete() {
//						sub.onNext(sum);
//						sub.onComplete();
//					}
//				});
//			}
//		};
//	}
	
	
	// T -> R 
	private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f) {
		return new Publisher<R>() {
			
			@Override
			public void subscribe(Subscriber<? super R> sub) {
				pub.subscribe(new DelegateSub<T, R>(sub) {
					@Override
					public void onNext(T i) {
						sub.onNext(f.apply(i));
					}
				});
			}
		};
	}
	
	/**
	 *  
	 * @param iter
	 * @return
	 */
	private static Publisher<Integer> iterPub(final List<Integer> iter){
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				sub.onSubscribe(new Subscription() {
					
					@Override
					public void request(long n) {
						try {
							iter.forEach(s-> sub.onNext(s));
							sub.onComplete();
						} catch(Throwable t) {
							sub.onError(t);
						}
					}
					@Override
					public void cancel() {
						
					}
				});
			}
		};
	}
	
	
	private static <T> Subscriber<T> logSub(){
		return new Subscriber<T>() {
			@Override
			public void onSubscribe(Subscription subscription) {
				subscription.request(Long.MAX_VALUE);
			}
			
			@Override
			public void onNext(T i) {
				System.out.println("onNext : "+i);
			}
			
			
			@Override
			public void onError(Throwable throwable) {
				System.out.println("onError : "+throwable);
			}
			
			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		};
	}
	
	
}
