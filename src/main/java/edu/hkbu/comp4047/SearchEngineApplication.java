package edu.hkbu.comp4047;

import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import webCrawler.WebCrawler;

@SpringBootApplication
public class SearchEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchEngineApplication.class, args);

		WebCrawler w = new WebCrawler();
		try {
			w.initialization();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		w.run();
	}
}
