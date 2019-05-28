package com.workorder.ticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.workorder.ticket")
@PropertySource({ "classpath:application-test.properties" })
@MapperScan("com.workorder.ticket.persistence.dao")
public class TicketLauncher {
	public static void main(String[] args) {
		SpringApplication.run(TicketLauncher.class, args);
	}
}
