import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// Used to print the full invoice,
public class PullDetails {

	// Attributes
	static int ordersId; // Gives CustomerId, OrderDate, RestaurantId, DriverId
	static int customerId;
	static Date orderDate;
	static int restaurantId;
	static int driverId;
	static String addInstructions;

	// Pizza Attributes

	// May have multiple rows/entries, held temporarily (Composite key of Orders
	// and OrderItem)
	static int tempItemId;
	static int tempPizzaId;
	static int tempItemQuantity;
	static Double tempSubTotal;
	static Double grandTotal;

	// Add multiple entries from more than 1 row
	static ArrayList<Integer> itemIds = new ArrayList<Integer>();
	static ArrayList<Integer> pizzaQuantity = new ArrayList<Integer>();
	static ArrayList<Integer> pizzaIds = new ArrayList<Integer>();
	static ArrayList<Double> pizzaSubPriceTotal = new ArrayList<Double>();

	// Methods

	// Prints all details on orderId given.
	public static void getOrderDetails(Statement statement, Integer orderNumber) {

		// Clearing Arraylists and grand total
		pizzaIds.clear();
		itemIds.clear();
		pizzaQuantity.clear();
		pizzaSubPriceTotal.clear();
		grandTotal = 0.00;

		int idSelection = orderNumber;
		int userSelection;

		// Getting Foreign Ids from Orders, assigns to variable, to be printed.
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM Orders WHERE OrdersId= '" + idSelection + "'");
			while (results.next()) {

				// Assigns Order foreign keys to be used to get other table details.
				customerId = results.getInt("CustomerId");
				orderDate = results.getDate("OrderDate");
				restaurantId = results.getInt("RestaurantId");
				driverId = results.getInt("DriverId");
				addInstructions = results.getString("AddInfo");
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		// System.out.println("Select the corresponding OrderId number for greater
		// details: ");
		// userSelection = Main.enterNumber();

		// Getting Foreign Ids from Orders, assigns to variable, to be printed.
		try {
			ResultSet rsOrderItems = statement
					.executeQuery("SELECT * FROM OrderItem WHERE OrdersId= '" + idSelection + "'");
			while (rsOrderItems.next()) {

				// Assigns Order foreign keys to be used to get other table details.
				tempItemId = rsOrderItems.getInt("ItemId");
				tempPizzaId = rsOrderItems.getInt("PizzaId");
				tempItemQuantity = rsOrderItems.getInt("Quantity");
				tempSubTotal = rsOrderItems.getDouble("Price");

				// Adds to array list due to multiple rows
				itemIds.add(tempItemId);
				pizzaIds.add(tempPizzaId);
				pizzaQuantity.add(tempItemQuantity);
				pizzaSubPriceTotal.add(tempSubTotal);
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		// ----------- Printing all details. -----------
		System.out.println("\nPrinting Full Order Details:\n");

		// Printing the details of the customer
		System.out.println("----Customer Details----");
		PrintingRows.printCustomer(statement, customerId);

		System.out.println("\n");

		// Printing Pizza details, according to number of itemIds
		System.out.println("----Order Details----");
		for (int i = 0; i < itemIds.size(); i++) {
			System.out.println("Item: " + itemIds.get(i));
			PrintingRows.printPizza(statement, pizzaIds.get(i));
			System.out.println("Quantity: " + pizzaQuantity.get(i));
			System.out.println("Total for Item: " + pizzaSubPriceTotal.get(i));

			// Adding sub totals to give grand total
			grandTotal = grandTotal + pizzaSubPriceTotal.get(i);
			System.out.println("-----");
		}
		System.out.println("Additional Instructions: " + addInstructions);
		System.out.println("Total: R" + grandTotal);

		System.out.println("\n----Restaurant Details----");
		// Printing Restaurant Details
		PrintingRows.printRestaurant(statement, restaurantId);

		System.out.println("\n----Driver Details----");
		// Printing Driver Details
		PrintingRows.printDriver(statement, driverId);
	}
}
