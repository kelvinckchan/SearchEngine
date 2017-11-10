package edu.hkbu.comp4047;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dataStorage.DataStore;
import webCrawler.WebCrawler;

@SpringBootApplication
public class SearchEngineApplication {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.err.println("Press 1 to Run WebCrawler, 2 to Input Data from File: ");
			int i = sc.nextInt();
			if (i == 1) {
				WebCrawler w = new WebCrawler();
				System.out.println("Enter Value of X(MaxUnprocessedQueueSize): ");
				int MaxUnprocessedQueueSize = sc.nextInt();
				System.out.println("Enter Value of Y(MaxProcessedURLPoolSize): ");
				int MaxProcessedURLPoolSize = sc.nextInt();
				System.out.println("Enter Initial URL: ");
				String InitialURL = sc.next();

				System.out.println("Debug(Y/N): ");
				String Debug = sc.next();

				System.out.println("Export data(Y/N): ");
				String Export = sc.next();

				w.initialization(MaxProcessedURLPoolSize, MaxUnprocessedQueueSize, InitialURL,
						Debug.equals("Y") ? true : false, Export.equals("Y") ? true : false);
				// w.initialization(100, 10, "http://www.hkbu.edu.hk/tch/main/index.jsp", false,
				// false);
				System.out.println("Any Key to continue: ");
				try {
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				w.run();
				break;
			} else if (i == 2) {
				DataStore ds = new DataStore();
				ds.input();
				break;
			} else {
				continue;
			}
		}
		System.err.println("Start Spring Server...");
		SpringApplication.run(SearchEngineApplication.class, args);

	}
}
