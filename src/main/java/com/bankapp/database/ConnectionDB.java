package main.java.com.bankapp.database;

import javax.swing.SwingWorker;
import main.java.com.bankapp.encryptionfunc.PasswordEncyrption;

import java.sql.*;
import java.util.ArrayList;

public final class ConnectionDB {
	
	
	/*
	 * 23.09.2025 - 2:21 AM
	 * 
	 * 
	 * BEHAVIOR OF THE THIS CODE IS NOT GOOD ENOUGH
	 * THERE ARE A LOT OF PROBLEM ABOUT CONNECTION FOR GETTING INFORMATIONS
	 * FOR EXAMPLE: WE TRY TO GET INFORMATION ABOUT PERSON THREE TIMES FOR DIFFERENT INFORMATION ABOUT PERSON
	 * 
	 * IT SHOULDN'T DO, BUT IN THIS CASE THERE IS NOT KINDA PROBLEM.
	 * 
	 * */
	
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "pOc!Xce12.cepov1qa"; 
    private static boolean isConnected = false;
    private static Person person = null;
    private static Balances balanceOfPerson = null;
    private static ArrayList<Transactions> transactionList = new ArrayList<>();
    
    // IP is changable, because of the AWS server.
    private static final String DB_URL = 
            "jdbc:mariadb://YOUR_IP:3306/banking_system";
    private static Connection connection = null;

    public interface ConnectionCallback {
        void onConnectionResult(boolean success);
    }
    
    public interface DetailedConnectionCallback extends ConnectionCallback {
        void onStatusUpdate(String status);
    }
    
	public static void connectWithCallback (ConnectionCallback callBack) {
    	SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String> () {
    		@Override
    		protected Boolean doInBackground() throws Exception {
    			try {
    				publish("Resolving database connection...");
    				
    				Class.forName("org.mariadb.jdbc.Driver");
    				publish("Reload drivers for connecting to database...");
    				
    				java.util.Properties props = new java.util.Properties();
    				props.setProperty("user", DB_USERNAME);
    				props.setProperty("password", DB_PASSWORD);
    				props.setProperty("useSSL", "false");
    				props.setProperty("allowPublicKeyRetrieval", "true");
    				props.setProperty("serverTimezone", "UTC");
    				props.setProperty("connectiıonTimeout", "15000");
    				props.setProperty("socketTimeout", "15000");
    				
    				publish("Connecting to database...");
    				
    				connection = DriverManager.getConnection(DB_URL, props);
    				
    				publish("Connection testing...");
    				
    				if(connection != null && !connection.isClosed()) {
    					try (Statement stmt = connection.createStatement()){
    						ResultSet rs = stmt.executeQuery("SELECT 1");
    						if (rs.next()) {
    							publish("Connection successful!");
    							isConnected = true; // Bağlantı başarılı olduğunda set et
    							return true;
    						}
    					}
    				}
    				
    				return false;
    				
    			} catch (ClassNotFoundException e) {
    				publish("JDBC Driver not found: " + e.getMessage());
    				System.err.println("JDBC Driver not found: " + e.getMessage());
    				return false;
    			} catch (SQLException e) {
    				publish("Database connection failed!");
    				System.err.println("Database connection failed: " + e.getMessage());
    				System.err.println("Error Code: " + e.getErrorCode());
    				System.err.println("SQL State: " + e.getSQLState());
    				return false;
    			} catch (Exception e) {
    				publish("An unexpected error occurred: " + e.getMessage());
    				System.err.println("An unexpected error occurred: " + e.getMessage());
    				return false;
    			}
    		}
    		
    		@Override
    		protected void process(java.util.List<String> chunks) {
    			if (!chunks.isEmpty() && callBack instanceof DetailedConnectionCallback) {
    				((DetailedConnectionCallback) callBack).onStatusUpdate(chunks.get(chunks.size() - 1));
    			}
    		}
    		
    		@Override
    		protected void done() {
    			try {
    				boolean successfulConnection = get();
    				callBack.onConnectionResult(successfulConnection);
    			} catch (Exception e) {
    				System.err.println("Error in connection worker: " + e.getMessage());
    				callBack.onConnectionResult(false);
    			}
    		}
    	};
    	
    	worker.execute();
    		
    }
	
	public static boolean login(String tc, String password) {
		String hashedPassword = PasswordEncyrption.hashFunction(password);
		
		String sqlQuery = "SELECT password FROM user_credentials WHERE tc_number = ?";
		String sqlUpdate = "UPDATE user_credentials SET last_login = CURRENT_TIMESTAMP, is_active = 1 WHERE tc_number = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, tc);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String storedHashedPassword = rs.getString("password");
				
				if(storedHashedPassword.equals(hashedPassword)) {
					try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
						updateStmt.setString(1, tc);
						updateStmt.executeUpdate();
					}
					person = getPersonInformation(tc);
					balanceOfPerson = getPersonBalanceInformation(tc);
					getTransactionsInformation(tc);
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
    }

	protected static Connection getConnection() {
        return connection;
    }
    
    protected static Boolean getIsConnected() {
		return isConnected;
	}
	
	public static Person getPerson() {
		return person;
	}
	
	public static void getNewPersonInformation() {
		if (person != null) {
			getPersonInformation(person.tc_number());
		}
	}
	
	public static ArrayList<Transactions> getTransaction() {
		return transactionList;
	}
	
	public static void getNewTransaction() {
		if (person != null) {
			getTransactionsInformation(person.tc_number());
		}
	}
	
	private static void setNullPerson() {
		System.out.println("Setting person to null.");
		System.out.println("Previous, Person: " + person);
		person = null;
		transactionList.clear();
		System.out.println("Now, Person: " + person);
	}
		
	
	private static Balances getPersonBalanceInformation(String tc) {
		String sqlGet = "SELECT balance, credit_limit, debts FROM user_details WHERE tc_number = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sqlGet)){
			pstmt.setString(0,tc);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				System.out.println("Balance Informantion retrieved successfully.");
				System.out.println("Balance: " + rs.getFloat("balance") + ", Credit Limit: " + rs.getFloat("credit_limit")
				+ ", Debts: " + rs.getFloat("debts"));
				return new Balances(rs.getFloat("balance"), rs.getFloat("credit_limit"), rs.getFloat("debts"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	
	private static Person getPersonInformation(String tc) {
		String sqlGet = "SELECT id, tc_number, first_name, last_name, phone, balance FROM user_details WHERE tc_number = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sqlGet)) {
			pstmt.setString(1, tc);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("User information retrieved successfully.");
				System.out.println("ID: " + rs.getInt("id") + ", TC: " + rs.getString("tc_number") + ", Name: "
						+ rs.getString("first_name") + " " + rs.getString("last_name") + ", Phone: "
						+ rs.getString("phone") + ", Balance: " + rs.getInt("balance"));
				
				return new Person(rs.getInt("id"), rs.getString("tc_number"), rs.getString("first_name"),
						rs.getString("last_name"), rs.getString("phone"), rs.getInt("balance"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  null;
	}

	private static void getTransactionsInformation(String tc) {
		transactionList.clear();
		String sqlGet = "SELECT id, tc_number, transaction_type, amount, description, transaction_date FROM transactions WHERE tc_number = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sqlGet)){
			pstmt.setString(1, tc);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("User transaction informations retrieved successfully.");
				System.out.println("ID: " + rs.getInt("id") + ", " + "Transaction Type: " + 
									rs.getString("transaction_type") + ", " + "Amount: " + rs.getFloat("amount") + 
									", " + "Description: " + rs.getString("description") + ", " + "Transaction Date: " +
									rs.getDate("transaction_date"));
				transactionList.add(new Transactions(rs.getString("transaction_type"), 
										rs.getFloat("amount"), rs.getString("description"),
										rs.getDate("transaction_date")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void reConnect() {
		connectWithCallback(new ConnectionCallback() {
			@Override
			public void onConnectionResult(boolean success) {
				setNullPerson();
				if (success) {
					isConnected = true;
					System.out.println("Reconnection successful!");
				} else {
					isConnected = false;
					System.err.println("Reconnection failed!");
				}
			}
		});
	}
	
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
            	setNullPerson();
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}