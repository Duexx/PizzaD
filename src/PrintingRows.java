import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Primarily used in PullDetails: Printing lists; Printing full invoice
public class PrintingRows {

	// Printing the id selected row from Customer
	public static void printCustomer(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {
			System.out.println("\n----Customer Details----");
			ResultSet results = statement
					.executeQuery("SELECT * FROM Customer WHERE CustomerId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println("CustomerId: " + results.getInt("CustomerId") + "\nCustomer: "
						+ results.getString("FirstName") + " " + results.getString("LastName") + "\nPhone Number: "
						+ results.getString("Phone") + "\nEmail: " + results.getString("Email") + "\nStreet: "
						+ results.getString("Street") + "\nCity: " + results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the First name selected row from Customer
	public static void printCustomerByName(Statement statement, String userInput) {

		String nameSelection;
		nameSelection = userInput;

		try {
			System.out.println("\n----Customer Details----");
			ResultSet results = statement
					.executeQuery("SELECT * FROM Customer WHERE FirstName = '" + nameSelection + "'");
			while (results.next()) {
				System.out.println("CustomerId: " + results.getInt("CustomerId") + "\nCustomer: "
						+ results.getString("FirstName") + " " + results.getString("LastName") + "\nPhone Number: "
						+ results.getString("Phone") + "\nEmail: " + results.getString("Email") + "\nStreet: "
						+ results.getString("Street") + "\nCity: " + results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row from Driver
	public static void printDriver(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement.executeQuery("SELECT * FROM Driver WHERE DriverId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println("Driver: " + results.getString("FirstName") + " " + results.getString("LastName")
						+ " From: " + results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row from Pizza
	public static void printPizza(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement.executeQuery("SELECT * FROM Pizza WHERE PizzaId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println(
						"Pizza: " + results.getString("PizzaName") + ", Price: " + results.getBigDecimal("Price"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row from Restaurant
	public static void printRestaurant(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement
					.executeQuery("SELECT * FROM Restaurant WHERE RestaurantId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println("Restaurant Name: " + results.getString("RestaurantName") + ", Phone Number: "
						+ results.getString("Phone") + ", From: " + results.getString("City"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row from Brand
	public static void printBrand(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement.executeQuery("SELECT * FROM Brand WHERE BrandId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println(results.getInt("BrandId") + ", " + results.getString("BrandName"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row from Orders
	public static void printOrder(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement.executeQuery("SELECT * FROM Orders WHERE OrdersId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println("OrdersId: " + results.getInt("OrdersId") + ", " + "CustomerId: "
						+ results.getInt("CustomerId") + ", " + "OrderDate: " + results.getDate("OrderDate") + ", "
						+ "RestaurantId: " + results.getInt("RestaurantId") + ", " + "DriverId: "
						+ results.getInt("DriverId") + ", " + "AddInfo: " + results.getString("AddInfo"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}

	// Printing the id selected row(s) from OrderItem
	public static void printOrderItems(Statement statement, Integer userSelection) {
		int idSelection;
		idSelection = userSelection;

		try {

			ResultSet results = statement
					.executeQuery("SELECT * FROM OrderItem WHERE OrdersId = '" + idSelection + "'");
			while (results.next()) {
				System.out.println(
						"OrdersId: " + results.getInt("OrdersId") + ", " + "ItemId: " + results.getInt("ItemId") + ", "
								+ "PizzaId: " + results.getInt("PizzaId") + ", " + "Quantity: "
								+ results.getInt("Quantity") + ", " + "Price: " + results.getBigDecimal("Price"));
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

	}
}
