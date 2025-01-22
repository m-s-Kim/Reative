package com.test.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureEx {
	public static void main(String[] args) throws InterruptedException{
		ExecutorService es = Executors.newCachedThreadPool();
		
		es.execute(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("dfdfdf");
		});
		
		
		
		
		
		
		System.out.println("exit");
	}
}
