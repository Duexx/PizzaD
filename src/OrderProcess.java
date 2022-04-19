import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

// Receiving order details
// Getting information related to order from database
public class OrderProcess {

	// Attributes
	int cusId;
	Date ordDate;
	int cusRestaurantId;
	// Name from getRestaurantId
	static String restaurantName;
	int brandId;
	int driverId;
	String cusInstructions;
	String cusCity;
	String cusPizza;
	int pizzaId;
	Double pizzaPrice;

	// Populated during getPizza()
	static ArrayList<Integer> cusOrderQuantity = new ArrayList<Integer>();
	static ArrayList<String> cusOrderTypes = new ArrayList<String>();

	// For inserting into OrderItems table
	static ArrayList<Integer> cusPizzaIds = new ArrayList<Integer>();
	static ArrayList<Double> pizzaItemPrice = new ArrayList<Double>();

	public OrderProcess(Statement statement) {

		// Clearing the array-lists in the event of 2nd order (diff customer)
		cusOrderQuantity.clear();
		cusOrderTypes.clear();

		// Ordering Process Methods
		this.cusId = getCustomerId(statement);
		this.cusCity = getCity(statement, cusId);
		this.cusRestaurantId = getRestaurantId(statement, cusCity);
		this.brandId = getBrandId(statement, restaurantName);

		// Populates quantity and types array list
		this.cusPizza = getPizza();
		this.cusInstructions = getInstructions();

		// Populates pizzaIds and pizzaItemPrices arraylists
		this.pizzaId = getPizzaIds(statement, brandId);
		this.pizzaPrice = getPizzaPrice(statement, brandId);

		this.driverId = getDriverId(statement, cusCity);
		this.ordDate = getOrderDate();

	}

	// Get customerId, prints customer table, selected by Id
	public static Integer getCustomerId(Statement statement) {

		DatabaseFunctions.printAllCustomer(statement);

		System.out.println("\nSelect the ID of the customer placing the order:");
		int idSelection = Main.enterNumber();

		return idSelection;

	}

	// Receives the user's order, allows multiple orders
	// Stores the type and quantity of each pizza in an array-list to be used to
	// number of orderItems
	public static String getPizza() {
		int userSelectionNo;
		String userPizza = "";
		int orderAgain;

		while (true) {
			try {
				System.out.println("Please choose which pizza you'd like to order: ");
				System.out.println("Select 1, 2 or 3: \r\n");
				System.out.println("1: Pepperoni \r\n2: Hawaiian \r\n3: Bacon Avo");

				userSelectionNo = Main.enterNumber();
				int userQuantity = 0;
				orderAgain = 0;

				if (userSelectionNo == 1) {
					userPizza = "Pepperoni";
					// Receives the quantity of pizza (see below)
					userQuantity = getHowMany();
					System.out.println(userSelectionNo);

					// Both quantity and type are stored in an array list to be calculated
					// and inserted into database
					cusOrderQuantity.add(userQuantity);
					cusOrderTypes.add(userPizza);

				} else if (userSelectionNo == 2) {
					userPizza = "Hawaiian";
					userQuantity = getHowMany();
					System.out.println(userSelectionNo);

					cusOrderQuantity.add(userQuantity);
					cusOrderTypes.add(userPizza);

				} else if (userSelectionNo == 3) {
					userPizza = "Bacon Avo";
					userQuantity = getHowMany();
					System.out.println(userSelectionNo);

					cusOrderQuantity.add(userQuantity);
					cusOrderTypes.add(userPizza);

				} else if (userSelectionNo > 3 || userSelectionNo < 1) {
					// Int but not within range
					throw new Exception("Out of range! Please enter a valid number: ");
				}
				break;
			} catch (Exception e) {
				System.out.println("Please enter a valid number!");
			}
		}
		// Checks to see if the user wants to order another pizza,
		// Calls the getPizza function again if yes.
		System.out.println("Would you like to order another pizza?");
		System.out.println("1: Yes \r\n2: No");
		orderAgain = Main.enterNumber();

		if (orderAgain == 1) {
			getPizza();
		} else {
			// Assumes any other input means no
			System.out.println("Thank you for ordering!\r\n");
			return userPizza;
		}
		return userPizza;
	}

	// Receives additional user instructions to be added to invoice.
	public static String getInstructions() {
		String userInstructions = "";
		int userSelectionNo;

		System.out.println("Are there any additional instructions for you order?: ");
		System.out.println("1: Yes \r\n2: No");
		userSelectionNo = Main.enterNumber();

		// If 1, asks for user instructions
		if (userSelectionNo == 1) {
			System.out.println("Please enter your additional instructions: ");
			userInstructions = Main.input.nextLine();
		} else { // if anything else, returns "None"
			userInstructions = "None";
		}
		return userInstructions;
	}

	// Used in the getPizza method to determine quantity of pizza
	public static int getHowMany() {
		int userQuantityNo;
		// repeats until a number is given
		while (true) {
			try {
				System.out.println("How many of this pizza would you like?: ");
				userQuantityNo = Main.enterNumber();
				break;
			} catch (Exception e) {
				System.out.println("Please enter a valid number!");
			}
		}
		return userQuantityNo;
	}

	// Receives from the user what restaurant they want to order from
	public static Integer getRestaurantId(Statement statement, String cusCity) {
		String citySelection = cusCity;
		String userRestaurant = "";
		restaurantName = "";
		int userSelectionNo;

		// Getting Restaurant name to get corresponding id in Restaurant table
		while (true) {
			try {
				System.out.println(
						"Please enter which restaurant you'd like to order from:\r\nSelect 1, 2 or 3: \r\n1: Romans\r\n2: Debonairs\r\n3: Panarottis ");

				// Ensures input is a number
				userSelectionNo = Main.enterNumber();

				if (userSelectionNo == 1) {
					userRestaurant = "Romans";
					break;
				} else if (userSelectionNo == 2) {
					userRestaurant = "Debonairs";
					break;
				} else if (userSelectionNo == 3) {
					userRestaurant = "Panarottis";
					break;
				} else if (userSelectionNo > 3 || userSelectionNo < 1) {
					System.out.println("Please enter a valid number!");
				}

			} catch (Exception e) {
				System.out.println("Please enter a valid number!");
			}
		}

		// for finding pizza and brand id
		restaurantName = userRestaurant;

		// RestaurantId to be returned for insert into Orders
		int userRestaurantId = 0;

		// Restaurant Selection based on Name and City
		try {
			ResultSet results = statement.executeQuery("SELECT RestaurantId FROM Restaurant WHERE City= '"
					+ citySelection + "' AND RestaurantName= '" + userRestaurant + "'");
			while (results.next()) {

				userRestaurantId = results.getInt("RestaurantId");
				return userRestaurantId;
			}

		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}
		return userRestaurantId;
	}

	// Get brandId for Pizza, and OrderItem table
	public static Integer getBrandId(Statement statement, String restaurantName) {
		int brandId = 0;

		String userRestaurant;

		// Restaurant Selection based on Name and City
		try {
			ResultSet results = statement
					.executeQuery("SELECT BrandId FROM Brand WHERE BrandName= '" + restaurantName + "'");
			while (results.next()) {

				brandId = results.getInt("BrandId");
				return brandId;
			}

		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		return brandId;
	}

	// Returns the city name, based on where the Customer lives.
	public static String getCity(Statement statement, Integer cusId) {

		int idSelection = cusId;
		String cusCity = "";
		// Getting City from Customer, used to match driver and correct Restaurant
		try {
			ResultSet results = statement
					.executeQuery("SELECT City FROM Customer WHERE CustomerId= '" + idSelection + "'");
			while (results.next()) {

				cusCity = results.getString("City");
				return cusCity;
			}
		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}
		return cusCity;
	}

	// Get the local date, converts to sql date
	public static Date getOrderDate() {

		LocalDate OrderDate = java.time.LocalDate.now();
		Date sqlDate = java.sql.Date.valueOf(OrderDate);

		return sqlDate;
	}

	// Get driver Id based on mutual City and lowest value(Load)
	public static Integer getDriverId(Statement statement, String cusCity) {
		int driverId = 0;
		String citySelection = cusCity;
		int lowestDriverLoad = 0;

		// Getting the lowest load based on city (drivers in same city), used to return
		// driverId
		try { //
			ResultSet results = statement
					.executeQuery("SELECT MIN(TotalOrders) FROM Driver WHERE City= '" + citySelection + "'");
			while (results.next()) {

				lowestDriverLoad = results.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		// Getting driver Id, based on lowest load and city.
		try {
			ResultSet results = statement
					.executeQuery("SELECT DriverId FROM Driver WHERE TotalOrders= '" + lowestDriverLoad + "'");
			while (results.next()) {

				driverId = results.getInt("DriverId");
			}

		} catch (SQLException e) {
			System.out.println("That entry does not exist!");
		}

		return driverId;

	}

	// Get pizzaIds adds to arraylist, based on BrandId and PizzaName
	public static Integer getPizzaIds(Statement statement, Integer brandId) {
		int pizzaId = 0;
		int pizzaBrandId = brandId;

		// Adds pizzaIds according to number of pizza types
		for (int i = 0; i < cusOrderTypes.size(); i++) {
			try {
				ResultSet results = statement.executeQuery("SELECT PizzaId FROM Pizza WHERE PizzaName= '"
						+ cusOrderTypes.get(i) + "' AND BrandId= '" + pizzaBrandId + "'");
				while (results.next()) {

					pizzaId = results.getInt("PizzaId");
					cusPizzaIds.add(pizzaId);
				}
			} catch (SQLException e) {
				System.out.println("That entry does not exist!");
			}
		}
		return pizzaId;
	}

	public static Double getPizzaPrice(Statement statement, Integer brandId) {
		double pizzaPrice = 0;
		int pizzaBrandId = brandId;

		// Adds pizzaIds according to number of pizza types
		for (int i = 0; i < cusOrderTypes.size(); i++) {
			try {
				// Selection of price based on Name and Brand
				ResultSet results = statement.executeQuery("SELECT Price FROM Pizza WHERE PizzaName= '"
						+ cusOrderTypes.get(i) + "' AND BrandId= '" + pizzaBrandId + "'");
				while (results.next()) {
					// Adds to arraylist for calculations (with quantity)
					pizzaPrice = results.getDouble("Price");
					pizzaItemPrice.add(pizzaPrice);
				}
			} catch (SQLException e) {
				System.out.println("That entry does not exist!");
			}
		}
		return pizzaPrice;
	}
}
