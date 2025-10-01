package main.java.com.bankapp.database;

public record Person(int id, 
		String tc_number, 
		String first_name, 
		String last_name,
		String phone_number,
		float balance) 
{
	@Override
	public String toString() {
		return String.format("%s", first_name);
	}
}
