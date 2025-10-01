package main.java.com.bankapp.database;

public record Balances(float balance,
		float credit_limit,
		float debts
		) 
{
	@Override
	public String toString() {
		return String.format("Balance: %.2f, Credit Limit: %.2f, Debts: %.2f");
	}
	
}
