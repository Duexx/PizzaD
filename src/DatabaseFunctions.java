import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Searching, updating and inserting into the Database
public class DatabaseFunctions {

	// Insert Order details into Orders table, after OrderProcess,
	public static void insertOrderTable(OrderProcess newOrder, Connection connection, int customerId) {

		int selectedId = customerId;
		// Preparing statement with column names
		try {
			PreparedStatement pstmtOrders = connection.prepareStatement(
					"INSERT INTO Orders (CustomerId, OrderDate, RestaurantId, DriverId, AddInfo) VALUES (?, ?, ?, ?, ?)");

			// Setting values to be inserted into columns
			pstmtOrders.setInt(1, selectedId);
			pstmtOrders.setDate(2, newOrder.ordDate);
			pstmtOrders.setInt(3, newOrder.cusRestaurantId);
			pstmtOrders.setInt(4, newOrder.driverId);
			pstmtOrders.setString(5, newOrder.cusInstructions);

			// Executing prepared statement
			pstmtOrders.executeUpdate();

			System.out.println("New Order Added!\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Insert into OrderItems, itemiD, pizzaID, Quantity and Price
	public static void insertOrderItemsTable(OrderProcess newOrder, Connection connection, Integer orderId) {

		int selectedId = orderId;
		// Forms composite key with OrdersId
		int itemId = 1;

		// Preparing statement with column names
		for (int i = 0; i < OrderProcess.cusOrderTypes.size(); i++) {
			try {
				PreparedStatement pstmtOrders = connection.prepareStatement(
						"INSERT INTO OrderItem (OrdersId, ItemId, PizzaId, Quantity, Price) VALUES (?, ?, ?, ?, ?)");

				// Setting values to be inserted into columns
				pstmtOrders.setInt(1, selectedId);
				pstmtOrders.setInt(2, itemId);
				pstmtOrders.setInt(3, OrderProcess.cusPizzaIds.get(i));
				pstmtOrders.setInt(4, OrderProcess.cusOrderQuantity.get(i));

				// Price * Quantity to get Price
				pstmtOrders.setDouble(5, (OrderProcess.pizzaItemPrice.get(i) * OrderProcess.cusOrderQuantity.get(i)));

				// Executing prepared statement
				pstmtOrders.executeUpdate();
				// Increase itemId value for composite key
				itemId++;

				System.out.println("Row Added!\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println("OrderItem Table Updated!");
		}
	}

	// Returns a list of all customer
	public static void printAllCustomer(Statement statement) {

		System.out.println("----Customer List----");
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM Customer");
			while (results.next()) {
				System.out.println("CustomerId: " + results.getInt("CustomerId") + ", " + results.getString("FirstName")
						+ ", " + results.getString("LastName") + ", " + results.getString("Phone") + ", "
						+ results.getString("Email") + ", " + results.getString("Street") + ", "
						+ results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}
	}

	// Returns a list of customers, choose to search by ID or First Name.
	public static void searchCustomer(Statement statement) {

		// Displays all customer details
		printAllCustomer(statement);

		System.out.println("\nWould you like to search by ID or First Name?\n1. ID\n2. First Name");

		// Ensuring input is a number
		int idOrName = Main.enterNumber();
		switch (idOrName) {
		case 1: // Searching by ID
			System.out.println("\nEnter a corresponding CustomerId to focus on:");

			// Ensures a number is entered
			int userSelection = Main.enterNumber();
			// Used to check if user answer exists
			int inRange = rowLengthCustomer(statement);

			// if user input is not within range
			if (userSelection > inRange || userSelection < 1) {
				System.out.println("That entry is not on the list!");

			} else { // if it is found, prints the specific customer
				PrintingRows.printCustomer(statement, userSelection);
			}
			break;
		case 2: // Searching by First Name
			System.out.println("Please enter the name of the customer:");
			String userCustomerSearch = Main.input.nextLine();

			// Uppercase for consistency
			userCustomerSearch = Main.upperAllWords(userCustomerSearch);
			PrintingRows.printCustomerByName(statement, userCustomerSearch);
			break;
		case 0:
			System.out.println("Have a good day!");
			System.exit(0);
		}

	}

	// Returns a list of the orders
	public static void searchOrders(Statement statement) {
		int userSelection;
		System.out.println("----Orders List----");
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM Orders");
			while (results.next()) {

				System.out.println(
						"OrdersId: " + results.getInt("OrdersId") + ", " + "CustomerId: " + results.getInt("CustomerId")
								+ ", " + "OrderDate: " + results.getDate("OrderDate") + ", " + "RestaurantId: "
								+ results.getInt("RestaurantId") + ", " + "DriverId: " + results.getInt("DriverId"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		System.out.println("\nSelect the corresponding OrderId number for greater details: ");

		// Ensures a number is entered
		userSelection = Main.enterNumber();

		// if it is found, prints full order details
		try {
			PullDetails.getOrderDetails(statement, userSelection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("OrderId: " + userSelection + " is not on the list!");
		}

	}

	// Inserts newEntry object into Customer Table
	public static void insertCusTable(Customer newEntry, Connection connection) {

		// Preparing statement with column names
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"INSERT INTO Customer (FirstName, LastName, Phone, Email, Street, City) VALUES (?, ?, ?, ?, ?, ?)");

			// Setting values to be inserted into columns
			pstmt.setString(1, newEntry.cusFirstName);
			pstmt.setString(2, newEntry.cusLastName);
			pstmt.setString(3, newEntry.cusContactNo);
			pstmt.setString(4, newEntry.cusEmail);
			pstmt.setString(5, newEntry.cusAddress);
			pstmt.setString(6, newEntry.cusLocation);

			// Executing prepared statement
			pstmt.executeUpdate();

			System.out.println("New Customer Added!\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Returns a list of the customers, user inputs id of customer to be updated.
	// User chooses which value to update.
	public static void updateCustomer(Statement statement) {
		// Prints the customer table
		searchCustomer(statement);

		System.out.println("Enter the id of the customer you'd like to update: ");
		int userSelection;
		int idSelection;
		// Ensures the input is a number
		idSelection = Main.enterNumber();

		try {
			ResultSet results = statement
					.executeQuery("SELECT * FROM Customer WHERE CustomerId ='" + idSelection + "'");
			while (results.next()) {

				System.out.println(results.getInt("CustomerId") + ", " + results.getString("FirstName") + ", "
						+ results.getString("LastName") + ", " + results.getString("Phone") + ", "
						+ results.getString("Email") + ", " + results.getString("Street") + ", "
						+ results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		System.out.println(
				"Select which detail you would like to alter:\n1. First Name\n2. Last Name\n3. Phone\n4. Email\n5. Location\n0. Exit\" ");

		// Ensuring user selection is a number
		userSelection = Main.enterNumber();

		// Assigns corresponding column name for updating
		String colSelection;

		switch (userSelection) {
		case 1: // Alter first name
			colSelection = "FirstName";
			updateCustomerDetails(statement, idSelection, colSelection);
			break;
		case 2: // Alter last name
			colSelection = "LastName";
			updateCustomerDetails(statement, idSelection, colSelection);
			break;
		case 3: // Alter Phone number
			colSelection = "Phone";
			updateCustomerDetails(statement, idSelection, colSelection);

			break;
		case 4: // Alter Email
			colSelection = "Email";
			updateCustomerDetails(statement, idSelection, colSelection);

			break;
		case 5: // Alter Location, will break into City and Street
			colSelection = "Location";
			updateCustomerDetails(statement, idSelection, colSelection);

			break;
		case 0:
			System.out.println("Have a good day!");
			System.exit(0);
		}

	}

	// Returns column length of Customer table
	public static Integer rowLengthCustomer(Statement statement) {
		int columnLength = 0;

		try {
			ResultSet results1 = statement.executeQuery("SELECT COUNT(*) AS total FROM Customer");
			results1.next();
			columnLength = results1.getInt("total");

		} catch (SQLException e) {
			System.out.println("Table/Column does not exist");

		}
		return columnLength;
	}

	// Returns column length of Orders table
	public static Integer rowLengthOrders(Statement statement) {
		int columnLength = 0;

		try {
			ResultSet results1 = statement.executeQuery("SELECT COUNT(*) AS total FROM Orders");
			results1.next();
			columnLength = results1.getInt("total");

		} catch (SQLException e) {
			System.out.println("Table/Column does not exist");

		}
		return columnLength;
	}

	// Get orderId, only for last entry!
	public static Integer getOrderId(Statement statement) {
		int orderId = 0;

		try {
			// Newest orderId will have highest value, order descending, gets top entry
			ResultSet results = statement.executeQuery("SELECT TOP 1 * FROM Orders ORDER BY OrdersId DESC");
			while (results.next()) {
				orderId = results.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		return orderId;
	}

	// Updating Customer details according to selection
	public static void updateCustomerDetails(Statement statement, int customerId, String colSelection) {

		int idSelection = customerId;
		String colName = colSelection;
		int rowsAffected;

		System.out.println(idSelection);
		System.out.println(colName);

		// City change will almost always necessitate a street change
		if (colName.equals("Location")) {

			System.out.println("Please enter the new City name: ");
			String newCityName = Main.input.nextLine();
			// Uppercase to ensure consistency
			newCityName = Main.upperAllWords(newCityName);

			System.out.println("Please enter the new Street name: ");
			String newStreetName = Main.input.nextLine();
			newStreetName = Main.upperAllWords(newStreetName);

			try {
				// Updating City
				rowsAffected = statement.executeUpdate(
						"UPDATE Customer SET City = '" + newCityName + "' WHERE CustomerId ='" + idSelection + "'");

				// Updating Street
				rowsAffected = statement.executeUpdate(
						"UPDATE Customer SET Street = '" + newStreetName + "' WHERE CustomerId ='" + idSelection + "'");

				System.out.println("Update Sucessful!");

			} catch (SQLException e) {
				System.out.println("That entry does not exist!");
			}

		} else { // Updating other values that aren't city/street

			System.out.println("Please enter the updated entry: ");
			String updatedValue = Main.input.nextLine();

			// Uppercase to ensure consistency
			updatedValue = Main.upperAllWords(updatedValue);

			try {
				rowsAffected = statement.executeUpdate("UPDATE Customer SET " + colName + "= '" + updatedValue
						+ "' WHERE CustomerId ='" + idSelection + "'");
				System.out.println("Update Sucessful!");

			} catch (SQLException e) {
				System.out.println("That entry does not exist!");
			}
		}
	}

}
