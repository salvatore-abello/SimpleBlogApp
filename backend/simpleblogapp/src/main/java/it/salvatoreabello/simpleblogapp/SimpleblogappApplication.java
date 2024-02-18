package it.salvatoreabello.simpleblogapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SimpleblogappApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimpleblogappApplication.class, args);
	}
}
