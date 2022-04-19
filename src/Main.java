import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.microsoft.sqlserver.jdbc.StringUtils;

public class Main {
	// Driver Code
	public static void main(String[] args) {

		// Connecting to Database QuickFoodMS
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:sqlserver://localhost;database=QuickFoodMS; Trusted_Connection=True", "jono", "*****");
			// Create a direct line to the database for running our queries
			Statement statement = connection.createStatement();

			// Perpetual menu until closed.
			while (true) {
				System.out.println(
						"What would you like to do today?\n1. Place Order\n2. Enter New Customer\n3. Update Existing Customer Information\n4. Search Customers\n5. Search Orders\n0. Exit");
				// Ensuring user selection is a number
				int userSelection = enterNumber();
				// Calling functions from DatabaseFunctions according to user selection
				switch (userSelection) {
				case 1: // Placing Order

					System.out.println("Is this for a new or existing customer?\n1: New\n2: Existing");
					int newExistSelect = enterNumber();

					// Switch between creating new customer/directly placing order
					switch (newExistSelect) {
					case 1: // New Customer

						// Makes a new Customer Object
						Customer newEntry = new Customer();

						// Inserts customer details into Customer Table
						DatabaseFunctions.insertCusTable(newEntry, connection);

						// Creates object containing all order details
						OrderProcess newOrder = new OrderProcess(statement);

						// Inserts all order details into database
						DatabaseFunctions.insertOrderTable(newOrder, connection, newOrder.cusId);

						// OrderId from newest Order entry, for orderItem entries
						int newOrderId = DatabaseFunctions.getOrderId(statement);

						// Inserts all orderItem details into database
						DatabaseFunctions.insertOrderItemsTable(newOrder, connection, newOrderId);
						System.out.println("Order Successful!\nFinalising and printing invoice...");

						// Printing invoice
						PullDetails.getOrderDetails(statement, newOrderId);
						break;

					case 2: // Existing Customer

						// Creates object containing all order details
						OrderProcess newOrderExist = new OrderProcess(statement);

						// Inserts all order details into database
						DatabaseFunctions.insertOrderTable(newOrderExist, connection, newOrderExist.cusId);

						// OrderId from newest Order entry, for orderItem entries
						int newOrderIdExist = DatabaseFunctions.getOrderId(statement);

						// Inserts all orderItem details into database
						DatabaseFunctions.insertOrderItemsTable(newOrderExist, connection, newOrderIdExist);
						System.out.println("Order Successful!\nFinalising and printing invoice...");

						// Printing invoice
						PullDetails.getOrderDetails(statement, newOrderIdExist);
						break;

					default:
						break;
					}

					break;
				case 2: // Enter New Customer
					// Makes a new Customer Object
					Customer newEntry = new Customer();

					// Inserts customer details into Customer Table
					DatabaseFunctions.insertCusTable(newEntry, connection);
					break;

				case 3: // Update Existing Customer
					DatabaseFunctions.updateCustomer(statement);
					break;

				case 4: // Search Customers
					DatabaseFunctions.searchCustomer(statement);
					break;

				case 5: // Search Orders
					DatabaseFunctions.searchOrders(statement);
					break;

				default:
					System.out.println("Exiting...\nHave a good day!");
					// Close up our connections
					statement.close();
					connection.close();
					System.exit(0);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Global Methods

	// Ensures user input is a number
	public static Integer enterNumber() {

		String tempUserSelection;
		int userSelection;

		while (true) {
			try {
				tempUserSelection = Main.input.nextLine();
				int userSelectionNo = Integer.valueOf(tempUserSelection);

				if (StringUtils.isInteger(tempUserSelection)) {
					break;
				} else {
					System.out.println("Please enter a valid number: ");
				}

			} catch (Exception e) {
				System.out.println("Please enter a valid number: ");
			}
		}
		userSelection = Integer.parseInt(tempUserSelection);
		return userSelection;
	}

	// Capitalises each word in a string for consistency
	public static String upperAllWords(String lowerCaseWords) {

		char[] charArray = lowerCaseWords.toCharArray();
		String capitalisedWords = "";
		boolean foundSpace = true;

		for (int i = 0; i < charArray.length; i++) {
			// if the array element is a letter
			if (Character.isLetter(charArray[i])) {

				// check space is present before the letter
				if (foundSpace) {

					// change the letter into uppercase
					charArray[i] = Character.toUpperCase(charArray[i]);
					foundSpace = false;
				}
			} else {
				// if the new character is not character
				foundSpace = true;
			}
		}

		capitalisedWords = String.valueOf(charArray);
		return capitalisedWords;
	}

	// Scanner to receive string input
	public static final Scanner input = new Scanner(System.in);

}

// Style Guide: https://www.isbe.net/documents/sql_server_standards.pdf