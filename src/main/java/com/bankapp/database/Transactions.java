package main.java.com.bankapp.database;

import java.sql.Date;


public record Transactions(String transaction_type,
		float amount,
		String describtion,
		Date timestamp) {}
